package com.shsnc.base.authorization.handler;

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
@RequestMapper("/function/authorization")
public class FunctionAuthorizationHandler {

    @Autowired
    private AuthorizationRoleRelationService authorizationRoleRelationService;

    @RequestMapper("")
    public boolean roleBatchAuthorization(@NotNull Integer roleId, @NotEmpty List<Integer> authorizationIdList) throws BizException {
        return authorizationRoleRelationService.roleBatchAuthorization(roleId,authorizationIdList);
    }


    /**
     * 获取角色已有功能权限
     * @param roleId
     * @return
     */
    @RequestMapper("role/have")
    public List<Integer> roleHaveAuthorizationList(@NotNull Integer roleId) {
        return authorizationRoleRelationService.getAuthorizationIdByRoleId(roleId);
    }


    /**
     * 获取用户已有功能权限
     * @param userId
     * @return
     */
    @RequestMapper("user/have")
    public List<Integer> userHaveAuthorizationList(@NotNull Integer userId) {
        return authorizationRoleRelationService.getAuthorizationIdByUserId(userId);
    }
}
