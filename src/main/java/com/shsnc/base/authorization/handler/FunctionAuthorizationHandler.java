package com.shsnc.base.authorization.handler;

import com.shsnc.api.core.RequestHandler;
import com.shsnc.api.core.annotation.RequestMapper;
import com.shsnc.base.authorization.service.AuthorizationRoleRelationService;
import com.shsnc.base.util.config.BizException;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by Elena on 2017/6/7.
 * 功能权限处理器
 */
@Component
@RequestMapper("/authorization/function")
public class FunctionAuthorizationHandler implements RequestHandler {

    @Autowired
    private AuthorizationRoleRelationService authorizationRoleRelationService;

    @RequestMapper("")
    public boolean roleBatchAuthorization(@NotNull Long roleId, @NotEmpty List<Long> authorizationIdList) throws BizException {
        return authorizationRoleRelationService.roleBatchAuthorization(roleId, authorizationIdList);
    }


    /**
     * 获取角色已有功能权限
     *
     * @param roleId
     * @return
     */
    @RequestMapper("/role/have")
    public List<Integer> roleHaveAuthorizationList(@NotNull Long roleId) {
        return authorizationRoleRelationService.getAuthorizationIdByRoleId(roleId);
    }

    /**
     * 获取用户已有功能权限
     *
     * @param userId
     * @return
     */
    @RequestMapper("/user/have/list")
    public List<String> userHaveAuthorizationList(@NotNull Long userId) throws BizException {
        return authorizationRoleRelationService.getAuthorizationCodeByUserId(userId);
    }

    /**
     * 判断权限是否拥有该用户
     * @param userId
     * @return
     */
    @RequestMapper("/user/have")
    public boolean userHaveAuthorization(@NotNull Long userId, @NotNull String authorizationCode) throws BizException {
        return authorizationRoleRelationService.userHaveAuthorization(userId, authorizationCode);
    }


}
