package com.supply.system.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.supply.common.api.FileClient;
import com.supply.common.constant.BusinessStatusEnum;
import com.supply.common.constant.Constant;
import com.supply.common.exception.ApiException;
import com.supply.common.util.CommonUtil;
import com.supply.common.util.RedisUtil;
import com.supply.system.cvt.UserCvt;
import com.supply.system.model.po.OrgPo;
import com.supply.system.model.po.TenantPo;
import com.supply.system.model.po.UserPo;
import com.supply.system.model.po.UserRolePo;
import com.supply.system.model.request.OrgRequest;
import com.supply.system.model.request.TenantRequest;
import com.supply.system.model.request.UserRequest;
import com.supply.system.model.request.UserRoleRequest;
import com.supply.system.model.response.UserResponse;
import com.supply.system.repository.IOrgRepository;
import com.supply.system.repository.ITenantRepository;
import com.supply.system.repository.IUserRepository;
import com.supply.system.repository.IUserRoleRepository;
import com.supply.system.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author wjd
 * @description
 * @date 2022-07-07
 */
@Service
public class UserServiceImpl implements IUserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final IUserRepository userRepository;

    private final ITenantRepository tenantRepository;

    private final IUserRoleRepository userRoleRepository;

    private final IOrgRepository orgRepository;

    private final RedisUtil redisUtil;

    private final BCryptPasswordEncoder passwordEncoder;

    private final FileClient fileClient;

    public UserServiceImpl(IUserRepository userRepository, ITenantRepository tenantRepository,
                           IUserRoleRepository userRoleRepository, IOrgRepository orgRepository,
                           RedisUtil redisUtil, BCryptPasswordEncoder passwordEncoder,
                           FileClient fileClient) {
        this.userRepository = userRepository;
        this.tenantRepository = tenantRepository;
        this.userRoleRepository = userRoleRepository;
        this.orgRepository = orgRepository;
        this.redisUtil = redisUtil;
        this.passwordEncoder = passwordEncoder;
        this.fileClient = fileClient;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addUser(UserRequest request) {
        logger.info("[新增用户信息]---信息体为{}", JSON.toJSONString(request));
        // 验证账号是否存在
        this.validateUser(request);
        // 完善用户信息并保存
        // 生成头像ID
        final long avatarId = IdUtil.getSnowflakeNextId();
        final UserPo userPo = this.completeUserInfo(request);
        userPo.setAvatarId(avatarId);
        userRepository.save(userPo);
        // 保存角色关联关系
        final Set<Long> roleIds = request.getRoleIds();
        if (CollectionUtil.isEmpty(roleIds)) {
            logger.info("用户注册,该用户{}无对应角色信息", request.getAccount());
            return;
        }
        List<UserRolePo> userRolePoList = new ArrayList<>();
        final Long userId = userPo.getId();
        for (Long roleId : roleIds) {
            UserRolePo userRolePo = new UserRolePo();
            userRolePo.setUserId(userId);
            userRolePo.setRoleId(roleId);
            userRolePoList.add(userRolePo);
        }
        userRoleRepository.saveBatch(userRolePoList);

        // 更新头像附件和用户关联关系
        fileClient.updateAttachmentRelation(request.getAttachmentId(), avatarId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(UserRequest request) {
        logger.info("[修改用户信息]---信息体为{}", JSON.toJSONString(request));
        // 验证账号是否存在
        this.validateUser(request);
        if (null == request.getAvatarId()) {
            final long avatarId = IdUtil.getSnowflakeNextId();
            request.setAvatarId(avatarId);
        }
        // 对密码进行加密并修改用户信息
        final UserPo userPo = UserCvt.INSTANCE.requestToPo(request);
        final String password = userPo.getPassword();
        if (StrUtil.isNotBlank(password)) {
            final String encode = passwordEncoder.encode(password);
            userPo.setPassword(encode);
        }
        userRepository.updateById(userPo);

        final Long userId = request.getId();
        // 保存角色关联关系
        if (CollectionUtil.isNotEmpty(request.getRoleIds())) {
            // 删除历史关联关系
            UserRolePo userRolePo = new UserRolePo();
            userPo.setStatus(Constant.STATUS_DEL);
            UserRoleRequest userRoleRequest = new UserRoleRequest();
            userRoleRequest.setUserId(userId);
            userRoleRepository.updateByParams(userRolePo, userRoleRequest);
            // 新增
            List<UserRolePo> userRolePoList = new ArrayList<>();
            for (Long roleId : request.getRoleIds()) {
                UserRolePo userRole = new UserRolePo();
                userRole.setUserId(userId);
                userRole.setRoleId(roleId);
                userRolePoList.add(userRole);
            }
            userRoleRepository.saveBatch(userRolePoList);
        }
        // 删除redis缓存信息
        final String redisKey = Constant.CURRENT_USER + userId;
        redisUtil.deleteObject(redisKey);

        // 更新头像
        fileClient.updateAttachmentRelation(request.getAttachmentId(), request.getAvatarId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetPassword(Long userId, String password) {
        logger.info("[重置密码]---其中待重置的用户ID为{},新密码为{}", userId, password);
        final String encode = passwordEncoder.encode(password);
        UserPo userPo = new UserPo();
        userPo.setId(userId);
        userPo.setPassword(encode);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delUser(Long userId) {
        logger.info("[用户删除]---其中待删除的用户ID为{}", userId);
        // 修改状态为删除状态
        UserPo userPo = new UserPo();
        userPo.setId(userId);
        userPo.setStatus(Constant.STATUS_DEL);
        userRepository.updateById(userPo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void freezeUser(Long userId) {
        logger.info("[用户冻结]---其中待冻结的用户ID为{}", userId);
        // 修改状态为冻结状态
        UserPo userPo = new UserPo();
        userPo.setId(userId);
        userPo.setBusinessStatus(BusinessStatusEnum.IN_FREEZE.getStatus());
        userRepository.updateById(userPo);
        // 删除redis缓存信息
        final String redisKey = Constant.CURRENT_USER + userId;
        redisUtil.deleteObject(redisKey);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void activeUser(Long userId) {
        logger.info("[用户激活]---其中待激活的用户ID为{}", userId);
        // 修改状态为冻结状态
        UserPo userPo = new UserPo();
        userPo.setId(userId);
        userPo.setBusinessStatus(BusinessStatusEnum.IN_ACTIVE.getStatus());
        userRepository.updateById(userPo);
        // 删除redis缓存信息
        final String redisKey = Constant.CURRENT_USER + userId;
        redisUtil.deleteObject(redisKey);
    }

    @Override
    public IPage<UserResponse> getUserPage(UserRequest request) {
        Page<UserPo> page = new Page<>(request.getPageIndex(), request.getPageSize());
        final Page<UserPo> poPage = userRepository.getPageByParams(page, request);
        if (poPage.getTotal() <= 0) {
            return new Page<>(request.getPageIndex(), request.getPageSize());
        }
        final List<UserPo> poList = poPage.getRecords();
        final List<UserResponse> responseList = UserCvt.INSTANCE.poToResponseBatch(poList);
        this.getUserPageDetail(responseList);
        return CommonUtil.pageCvt(responseList, poPage);
    }

    @Override
    public List<UserResponse> getUserListByParams(UserRequest request) {
        final List<UserPo> list = userRepository.getListByParams(request);
        return UserCvt.INSTANCE.poToResponseBatch(list);
    }

    @Override
    public UserResponse getUseByParams(UserRequest request) {
        logger.info("[获取用户信息]---其中参数为{}", JSON.toJSONString(request));
        UserPo userPo = userRepository.getByParams(request);
        return UserCvt.INSTANCE.poToResponse(userPo);
    }

    @Override
    public UserResponse getManageUserByClientId(String clientId) {
        logger.info("[获取管理员用户信息]---其中客户端ID为{}", clientId);
        TenantRequest tenantRequest = new TenantRequest();
        tenantRequest.setClientId(clientId);
        tenantRequest.setStatus(Constant.STATUS_NOT_DEL);
        final TenantPo tenant = tenantRepository.getByParams(tenantRequest);
        if (null == tenant) {
            return null;
        }
        final Long tenantId = tenant.getId();

        // 查询管理员用户
        UserRequest userRequest = new UserRequest();
        userRequest.setIsManage(true);
        userRequest.setTenantId(tenantId);
        userRequest.setStatus(Constant.STATUS_NOT_DEL);
        UserPo userPo = userRepository.getByParams(userRequest);
        return UserCvt.INSTANCE.poToResponse(userPo);
    }

    /**
     * @description 新增用户时完善用户信息.
     * @author wjd
     * @date 2022/9/7
     * @param request 待新增的用户
     * @return 完善后的用户
     */
    private UserPo completeUserInfo(UserRequest request) {
        // 转换
        final UserPo userPo = UserCvt.INSTANCE.requestToPo(request);
        // 密码加密
        final String encode = passwordEncoder.encode(request.getPassword());
        userPo.setPassword(encode);
        // 业务状态默认为正常状态
        if (null == userPo.getBusinessStatus()) {
            userPo.setBusinessStatus(BusinessStatusEnum.IN_ACTIVE.getStatus());
        }
        // 默认为非管理员
        if (null == userPo.getIsManage()) {
            userPo.setIsManage(false);
        }
        if (null != userPo.getOrgId()) {
            return userPo;
        }
        // 如果公司ID为空,则查询是否存在公司,存在则使用当前租户的主公司作为当前用户的公司
        final Long tenantId = userPo.getTenantId();
        OrgRequest orgRequest = new OrgRequest();
        orgRequest.setTenantId(tenantId);
        orgRequest.setIsMain(true);
        orgRequest.setStatus(Constant.STATUS_NOT_DEL);
        final OrgPo orgPo = orgRepository.getByParams(orgRequest);
        if (null != orgPo) {
            userPo.setOrgId(orgPo.getId());
        }
        return userPo;
    }

    /**
      * @description 用户分页信息填补.
      * @author wjd
      * @date 2022/8/4
      * @param userList 用户信息
      */
    private void getUserPageDetail(List<UserResponse> userList) {
        // 获取用户ID集
        final Set<Long> userIds = userList.stream().map(UserResponse::getId).collect(Collectors.toSet());
        // 根据用户ID集查询角色信息集并根据用户ID进行分组
        Map<Long, Set<Long>> userToRoleMap = new HashMap<>();
        UserRoleRequest userRoleRequest = new UserRoleRequest();
        userRoleRequest.setUserIds(userIds);
        userRoleRequest.setStatus(Constant.STATUS_NOT_DEL);
        final List<UserRolePo> userRolePoList = userRoleRepository.getListByParams(userRoleRequest);
        if (CollectionUtil.isNotEmpty(userRolePoList)) {
            userToRoleMap = userRolePoList.stream()
                    .collect(Collectors.groupingBy(UserRolePo::getUserId, Collectors.mapping(UserRolePo::getRoleId, Collectors.toSet())));
        }
        // 获取部门ID集并根据部门ID集获取部门ID对应的部门信息映射
        final Set<Long> deptIds = userList.stream().filter(user -> null != user.getDepartId()).map(UserResponse::getDepartId).collect(Collectors.toSet());
        final Map<Long, OrgPo> orgMap = orgRepository.getMapByIds(deptIds);


        for (UserResponse user : userList) {
            final Long userId = user.getId();
            if (userToRoleMap.containsKey(userId)) {
                final Set<Long> roleIds = userToRoleMap.get(userId);
                user.setRoleIds(roleIds);
            }
            final Long departId = user.getDepartId();
            if (orgMap.containsKey(departId)) {
                final OrgPo orgPo = orgMap.get(departId);
                user.setDepartName(orgPo.getName());
            }
        }
    }

    /**
      * @description 新增/修改验证账号&手机号是否存在.
      * @author wjd
      * @date 2022/11/5
      * @param request 待验证的实体信息
      */
    private void validateUser(UserRequest request) {
        UserRequest userRequest = new UserRequest();
        userRequest.setTenantId(request.getTenantId());
        userRequest.setAccount(request.getAccount());
        userRequest.setStatus(Constant.STATUS_NOT_DEL);
        userRequest.setNeId(request.getId());
        // 验证账号是否存在
        Long count = userRepository.getCountByParams(userRequest);
        if (count > 0) {
            logger.warn("账号{}已存在", request.getAccount());
            throw new ApiException("账号已存在!");
        }
        // 手机号验证
        if (StrUtil.isBlank(request.getTelephone())) {
            return;
        }
        userRequest.setAccount(null);
        userRequest.setTelephone(request.getTelephone());
        count = userRepository.getCountByParams(userRequest);
        if (count > 0) {
            logger.warn("手机号{}已存在", request.getTelephone());
            throw new ApiException("手机号已存在!");
        }
    }

}
