package com.supply.system.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.google.code.kaptcha.Producer;
import com.supply.common.constant.Constant;
import com.supply.common.constant.GrantTypeEnum;
import com.supply.common.exception.ApiException;
import com.supply.common.model.Result;
import com.supply.common.model.response.auth.AuthTokenResponse;
import com.supply.common.util.RedisUtil;
import com.supply.system.api.AuthClient;
import com.supply.system.constant.ResourceTypeEnum;
import com.supply.system.cvt.ResourceCvt;
import com.supply.system.cvt.RoleCvt;
import com.supply.system.cvt.TenantCvt;
import com.supply.system.cvt.UserCvt;
import com.supply.system.model.po.ResourcePo;
import com.supply.system.model.po.RolePo;
import com.supply.system.model.po.TenantPo;
import com.supply.system.model.po.UserPo;
import com.supply.system.model.po.UserThirdPo;
import com.supply.system.model.request.LoginRequest;
import com.supply.system.model.request.TenantRequest;
import com.supply.system.model.request.UserThirdRequest;
import com.supply.system.model.response.ResourceResponse;
import com.supply.system.model.response.RoleResponse;
import com.supply.system.model.response.TenantResponse;
import com.supply.system.model.response.UserInfoResponse;
import com.supply.system.model.response.UserResponse;
import com.supply.system.model.response.UserThirdResponse;
import com.supply.system.repository.IResourceRepository;
import com.supply.system.repository.IRoleRepository;
import com.supply.system.repository.ITenantRepository;
import com.supply.system.repository.IUserRepository;
import com.supply.system.repository.IUserThirdRepository;
import com.supply.system.service.ILoginService;
import com.supply.system.util.WeCatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author wjd
 * @description
 * @date 2022-08-11
 */
@Service
public class LoginServiceImpl implements ILoginService {
    private static final Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

    private final ITenantRepository tenantRepository;

    private final IUserRepository userRepository;

    private final IRoleRepository roleRepository;

    private final IResourceRepository resourceRepository;

    private final AuthClient authClient;

    private final RedisUtil redisUtil;

    private final Producer producer;

    private final IUserThirdRepository userThirdRepository;

    private final WeCatUtil weCatUtil;

    private final HttpServletResponse response;

    public LoginServiceImpl(ITenantRepository tenantRepository, IUserRepository userRepository,
                            IRoleRepository roleRepository, IResourceRepository resourceRepository,
                            AuthClient authClient, RedisUtil redisUtil, Producer producer,
                            IUserThirdRepository userThirdRepository, WeCatUtil weCatUtil, HttpServletResponse response) {
        this.tenantRepository = tenantRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.resourceRepository = resourceRepository;
        this.authClient = authClient;
        this.redisUtil = redisUtil;
        this.producer = producer;
        this.userThirdRepository = userThirdRepository;
        this.weCatUtil = weCatUtil;
        this.response = response;
    }


    @Override
    public AuthTokenResponse login(LoginRequest request) {
        logger.info("[用户登录]---其中登录信息为{}", JSON.toJSONString(request));
        // 验证码验证
        final String captcha = request.getCaptcha();
        final String code = captcha.toLowerCase();
        final String realKey = code + request.getKey();
        final Object checkCode = redisUtil.getCacheObject(realKey);
        if (null == checkCode || !StrUtil.equals(checkCode.toString(), code)) {
            throw new ApiException("验证码错误");
        }
        // 租户验证
        final TenantPo tenantPo = this.validateTenant(request.getTenantCode());
        // 组装信息
        return this.validateAuth(Constant.LOGIN_TYPE_PWD, tenantPo, request.getUserName(), request.getPassword());
    }

    @Override
    public AuthTokenResponse loginByWeCat(String code) {
        // 根据授权码获取开放平台ID
        final UserThirdResponse userThirdInfo = weCatUtil.getOpenInfoByCode(code);
        final String openId = userThirdInfo.getOpenId();
        // 根据授权码查询绑定的用户信息
        UserThirdRequest userThirdRequest = new UserThirdRequest();
        userThirdRequest.setOpenId(openId);
        userThirdRequest.setStatus(Constant.STATUS_NOT_DEL);
        final UserThirdPo userThirdPo = userThirdRepository.getByParams(userThirdRequest);
        if (null == userThirdPo) {
            final String message = "该微信号尚未绑定账号,请先进行绑定!";
            throw new ApiException(message);
        }
        // 根据租户ID查询租户信息
        final Long tenantId = userThirdPo.getTenantId();
        final TenantPo tenantPo = tenantRepository.getById(tenantId);
        // 根据用户ID查询用户信息
        final Long userId = userThirdPo.getUserId();
        final UserPo userPo = userRepository.getById(userId);
        // 权限验证: 第三方授权登录统一转换成账号密码去验证服务验证
        return this.validateAuth(Constant.LOGIN_TYPE_THIRD, tenantPo, userPo.getAccount(), Constant.DEFAULT_PASSWORD);
    }

    @Override
    public void logout(Long userId) {
        logger.info("[用户退出]---待退出的用户为{}", userId);
        // 调用auth移除相应的token
        // 删除redis缓存信息
        final String redisKey = Constant.CURRENT_USER + userId;
        redisUtil.deleteObject(redisKey);
    }

    @Override
    public void generateCaptcha(String key) {
        final String text = producer.createText();
        final BufferedImage image = producer.createImage(text);
        try {
            ServletOutputStream out = response.getOutputStream();
            response.setStatus(203);
            response.setContentType("image/png");
            ImageIO.write(image, "png", out);
        } catch (IOException e) {
            final String errorMessage = "生成验证码异常!";
            logger.error(errorMessage, e);
            throw new ApiException(errorMessage);
        }
        // 写入redis
        final String code = text.toLowerCase();
        final String realKey = code + key;
        redisUtil.setCacheObject(realKey, code, 60L, TimeUnit.SECONDS);
    }

    @Override
    public UserInfoResponse getUserInfo(Long userId) {
        logger.info("[获取用户详细信息]---其中用户ID为{}", userId);
        UserInfoResponse response = new UserInfoResponse();

        // 获取用户基本信息
        final UserPo userPo = userRepository.getById(userId);
        if (null == userPo) {
            final String message = StrUtil.format("根据用户ID{}未查询到用户信息", userId);
            logger.error(message);
            throw new ApiException(message);
        }
        final UserResponse userResponse = UserCvt.INSTANCE.poToResponse(userPo);
        response.setUserResponse(userResponse);

        // 租户信息
        final Long tenantId = userPo.getTenantId();
        final TenantPo tenantPo = tenantRepository.getById(tenantId);
        final TenantResponse tenantResponse = TenantCvt.INSTANCE.poToResponse(tenantPo);
        response.setTenantResponse(tenantResponse);

        // 角色信息
        final List<RolePo> rolePoList = roleRepository.getRoleByUserId(userId);
        if (CollectionUtil.isEmpty(rolePoList)) {
            return response;
        }
        final List<RoleResponse> roleResponseList = RoleCvt.INSTANCE.poToResponseBatch(rolePoList);
        response.setRoleResponseList(roleResponseList);

        // 资源信息
        final List<Long> roleIdList = rolePoList.stream().map(RolePo::getId).collect(Collectors.toList());
        final List<ResourcePo> resourcePoList = resourceRepository.getByRoleIdList(roleIdList, null);
        if (CollectionUtil.isEmpty(resourcePoList)) {
            return response;
        }
        final List<ResourceResponse> resourceResponseList = ResourceCvt.INSTANCE.poToResponseBatch(resourcePoList);

        // 菜单树结构信息
        final List<ResourceResponse> menuResponseList = this.getMenuTree(resourceResponseList);
        response.setMenuResponseList(menuResponseList);

        // 按钮信息
        final List<ResourceResponse> buttonList = resourceResponseList.stream()
                .filter(resource -> resource.getType() == ResourceTypeEnum.BUTTON.getType())
                .collect(Collectors.toList());
        response.setButtonResponseList(buttonList);
        return response;
    }

    /**
      * @description 密码模式权限验证.
      * @author wjd
      * @date 2022/12/8
      * @param loginType 登录方式
      * @param tenantPo 租户信息
      * @param account 账号
      * @param password 用户密码
      * @return 权限token
      */
    private AuthTokenResponse validateAuth(int loginType, TenantPo tenantPo, String account, String password) {
        // 组装信息
        Map<String, String> parameters = new HashMap<>();
        parameters.put("client_id", tenantPo.getClientId());
        parameters.put("client_secret", tenantPo.getCode());
        parameters.put("grant_type", GrantTypeEnum.PASSWORD.getCode());
        parameters.put("username", account);
        parameters.put("password", password);
        final Result<AuthTokenResponse> result = authClient.postAccessToken(parameters, loginType, tenantPo.getId());
        if (!result.isOk()) {
            logger.error("[用户登录]---验证异常!");
            throw new ApiException(result.getMessage());
        }
        final AuthTokenResponse data = result.getData();
        data.setTenantCode(tenantPo.getCode());
        return data;
    }

    /**
      * @description 根据租户编码验证租户.
      * @author wjd
      * @date 2022/11/4
      * @param tenantCode 租户编码
      * @return 租户信息
      */
    private TenantPo validateTenant(String tenantCode) {
        TenantRequest tenantRequest = new TenantRequest();
        tenantRequest.setCode(tenantCode);
        tenantRequest.setStatus(Constant.STATUS_NOT_DEL);
        final TenantPo tenantPo = tenantRepository.getByParams(tenantRequest);
        if (null == tenantPo) {
            logger.error("[用户登录]---根据租户CODE{}未查询到租户信息", tenantCode);
            throw new ApiException("该编码不存在!");
        }
        return tenantPo;
    }

    /**
     * @description 根据资源信息集获取对应的菜单集并组装成树结构.
     * @author wjd
     * @date 2022/7/28
     * @param resourceResponseList 资源信息集
     * @return 树结构的菜单集.
     */
    private List<ResourceResponse> getMenuTree(List<ResourceResponse> resourceResponseList) {
        // 查询出所有菜单集
        final List<ResourceResponse> menuList = resourceResponseList.stream()
                .filter(resource -> resource.getType() == ResourceTypeEnum.MENU.getType())
                .collect(Collectors.toList());
        if (CollectionUtil.isEmpty(menuList)) {
            return resourceResponseList;
        }
        // 组装树结构中菜单根节点
        List<ResourceResponse> responseList = menuList.stream()
                .filter(menu -> menu.getParentId() == 0L).collect(Collectors.toList());
        // 组装树结构中叶子菜单按钮
        this.getLeafMenu(responseList, menuList);
        return responseList;
    }

    /**
     * @description 组装菜单的子节点.
     * @author wjd
     * @date 2022/7/28
     * @param rootMenuList 根节点信息
     * @param menuList 菜单信息集
     */
    private void getLeafMenu(List<ResourceResponse> rootMenuList, List<ResourceResponse> menuList) {
        if (CollectionUtil.isEmpty(rootMenuList)) {
            return;
        }
        for (ResourceResponse menuResponse : rootMenuList) {
            final Long parentId = menuResponse.getId();
            // 根据id查询出子菜单集
            final List<ResourceResponse> childrenMenuList = menuList.stream()
                    .filter(menu -> Objects.equals(menu.getParentId(), parentId))
                    .collect(Collectors.toList());
            menuResponse.setChildrenList(childrenMenuList);
            this.getLeafMenu(menuResponse.getChildrenList(), menuList);
        }
    }
}
