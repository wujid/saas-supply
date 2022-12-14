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
        logger.info("[??????????????????]---????????????{}", JSON.toJSONString(request));
        // ????????????????????????
        this.validateUser(request);
        // ???????????????????????????
        // ????????????ID
        final long avatarId = IdUtil.getSnowflakeNextId();
        final UserPo userPo = this.completeUserInfo(request);
        userPo.setAvatarId(avatarId);
        userRepository.save(userPo);
        // ????????????????????????
        final Set<Long> roleIds = request.getRoleIds();
        if (CollectionUtil.isEmpty(roleIds)) {
            logger.info("????????????,?????????{}?????????????????????", request.getAccount());
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

        // ???????????????????????????????????????
        fileClient.updateAttachmentRelation(request.getAttachmentId(), avatarId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(UserRequest request) {
        logger.info("[??????????????????]---????????????{}", JSON.toJSONString(request));
        // ????????????????????????
        this.validateUser(request);
        if (null == request.getAvatarId()) {
            final long avatarId = IdUtil.getSnowflakeNextId();
            request.setAvatarId(avatarId);
        }
        // ??????????????????????????????????????????
        final UserPo userPo = UserCvt.INSTANCE.requestToPo(request);
        final String password = userPo.getPassword();
        if (StrUtil.isNotBlank(password)) {
            final String encode = passwordEncoder.encode(password);
            userPo.setPassword(encode);
        }
        userRepository.updateById(userPo);

        final Long userId = request.getId();
        // ????????????????????????
        if (CollectionUtil.isNotEmpty(request.getRoleIds())) {
            // ????????????????????????
            UserRolePo userRolePo = new UserRolePo();
            userPo.setStatus(Constant.STATUS_DEL);
            UserRoleRequest userRoleRequest = new UserRoleRequest();
            userRoleRequest.setUserId(userId);
            userRoleRepository.updateByParams(userRolePo, userRoleRequest);
            // ??????
            List<UserRolePo> userRolePoList = new ArrayList<>();
            for (Long roleId : request.getRoleIds()) {
                UserRolePo userRole = new UserRolePo();
                userRole.setUserId(userId);
                userRole.setRoleId(roleId);
                userRolePoList.add(userRole);
            }
            userRoleRepository.saveBatch(userRolePoList);
        }
        // ??????redis????????????
        final String redisKey = Constant.CURRENT_USER + userId;
        redisUtil.deleteObject(redisKey);

        // ????????????
        fileClient.updateAttachmentRelation(request.getAttachmentId(), request.getAvatarId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetPassword(Long userId, String password) {
        logger.info("[????????????]---????????????????????????ID???{},????????????{}", userId, password);
        final String encode = passwordEncoder.encode(password);
        UserPo userPo = new UserPo();
        userPo.setId(userId);
        userPo.setPassword(encode);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delUser(Long userId) {
        logger.info("[????????????]---????????????????????????ID???{}", userId);
        // ???????????????????????????
        UserPo userPo = new UserPo();
        userPo.setId(userId);
        userPo.setStatus(Constant.STATUS_DEL);
        userRepository.updateById(userPo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void freezeUser(Long userId) {
        logger.info("[????????????]---????????????????????????ID???{}", userId);
        // ???????????????????????????
        UserPo userPo = new UserPo();
        userPo.setId(userId);
        userPo.setBusinessStatus(BusinessStatusEnum.IN_FREEZE.getStatus());
        userRepository.updateById(userPo);
        // ??????redis????????????
        final String redisKey = Constant.CURRENT_USER + userId;
        redisUtil.deleteObject(redisKey);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void activeUser(Long userId) {
        logger.info("[????????????]---????????????????????????ID???{}", userId);
        // ???????????????????????????
        UserPo userPo = new UserPo();
        userPo.setId(userId);
        userPo.setBusinessStatus(BusinessStatusEnum.IN_ACTIVE.getStatus());
        userRepository.updateById(userPo);
        // ??????redis????????????
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
        logger.info("[??????????????????]---???????????????{}", JSON.toJSONString(request));
        UserPo userPo = userRepository.getByParams(request);
        return UserCvt.INSTANCE.poToResponse(userPo);
    }

    @Override
    public UserResponse getManageUserByClientId(String clientId) {
        logger.info("[???????????????????????????]---???????????????ID???{}", clientId);
        TenantRequest tenantRequest = new TenantRequest();
        tenantRequest.setClientId(clientId);
        tenantRequest.setStatus(Constant.STATUS_NOT_DEL);
        final TenantPo tenant = tenantRepository.getByParams(tenantRequest);
        if (null == tenant) {
            return null;
        }
        final Long tenantId = tenant.getId();

        // ?????????????????????
        UserRequest userRequest = new UserRequest();
        userRequest.setIsManage(true);
        userRequest.setTenantId(tenantId);
        userRequest.setStatus(Constant.STATUS_NOT_DEL);
        UserPo userPo = userRepository.getByParams(userRequest);
        return UserCvt.INSTANCE.poToResponse(userPo);
    }

    /**
     * @description ?????????????????????????????????.
     * @author wjd
     * @date 2022/9/7
     * @param request ??????????????????
     * @return ??????????????????
     */
    private UserPo completeUserInfo(UserRequest request) {
        // ??????
        final UserPo userPo = UserCvt.INSTANCE.requestToPo(request);
        // ????????????
        final String encode = passwordEncoder.encode(request.getPassword());
        userPo.setPassword(encode);
        // ?????????????????????????????????
        if (null == userPo.getBusinessStatus()) {
            userPo.setBusinessStatus(BusinessStatusEnum.IN_ACTIVE.getStatus());
        }
        // ?????????????????????
        if (null == userPo.getIsManage()) {
            userPo.setIsManage(false);
        }
        if (null != userPo.getOrgId()) {
            return userPo;
        }
        // ????????????ID??????,???????????????????????????,??????????????????????????????????????????????????????????????????
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
      * @description ????????????????????????.
      * @author wjd
      * @date 2022/8/4
      * @param userList ????????????
      */
    private void getUserPageDetail(List<UserResponse> userList) {
        // ????????????ID???
        final Set<Long> userIds = userList.stream().map(UserResponse::getId).collect(Collectors.toSet());
        // ????????????ID???????????????????????????????????????ID????????????
        Map<Long, Set<Long>> userToRoleMap = new HashMap<>();
        UserRoleRequest userRoleRequest = new UserRoleRequest();
        userRoleRequest.setUserIds(userIds);
        userRoleRequest.setStatus(Constant.STATUS_NOT_DEL);
        final List<UserRolePo> userRolePoList = userRoleRepository.getListByParams(userRoleRequest);
        if (CollectionUtil.isNotEmpty(userRolePoList)) {
            userToRoleMap = userRolePoList.stream()
                    .collect(Collectors.groupingBy(UserRolePo::getUserId, Collectors.mapping(UserRolePo::getRoleId, Collectors.toSet())));
        }
        // ????????????ID??????????????????ID???????????????ID???????????????????????????
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
      * @description ??????/??????????????????&?????????????????????.
      * @author wjd
      * @date 2022/11/5
      * @param request ????????????????????????
      */
    private void validateUser(UserRequest request) {
        UserRequest userRequest = new UserRequest();
        userRequest.setTenantId(request.getTenantId());
        userRequest.setAccount(request.getAccount());
        userRequest.setStatus(Constant.STATUS_NOT_DEL);
        userRequest.setNeId(request.getId());
        // ????????????????????????
        Long count = userRepository.getCountByParams(userRequest);
        if (count > 0) {
            logger.warn("??????{}?????????", request.getAccount());
            throw new ApiException("???????????????!");
        }
        // ???????????????
        if (StrUtil.isBlank(request.getTelephone())) {
            return;
        }
        userRequest.setAccount(null);
        userRequest.setTelephone(request.getTelephone());
        count = userRepository.getCountByParams(userRequest);
        if (count > 0) {
            logger.warn("?????????{}?????????", request.getTelephone());
            throw new ApiException("??????????????????!");
        }
    }

}
