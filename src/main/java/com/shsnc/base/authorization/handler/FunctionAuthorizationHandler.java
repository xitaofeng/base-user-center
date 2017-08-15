package com.shsnc.base.authorization.handler;

import com.shsnc.api.core.RequestHandler;
import com.shsnc.api.core.ThreadContext;
import com.shsnc.api.core.annotation.Authentication;
import com.shsnc.api.core.annotation.LoginRequired;
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
    @Authentication("BASE_USER_CENTER_AUTHORIZATION_FUNCTION")
    public boolean roleBatchAuthorization(@NotNull Long roleId, @NotEmpty List<Long> authorizationIdList) throws BizException {
        return authorizationRoleRelationService.roleBatchAuthorization(roleId, authorizationIdList);
    }

    @RequestMapper("/code")
    @Authentication("BASE_USER_CENTER_AUTHORIZATION_FUNCTION_CODE")
    public boolean roleBatchAuthorizationCode(@NotNull Long roleId, @NotEmpty List<String> authorizationCodeList) throws BizException {
        return authorizationRoleRelationService.roleBatchAuthorizationCode(roleId, authorizationCodeList);
    }


    /**
     * 获取角色已有功能权限
     *
     * @param roleId
     * @return
     */
    @RequestMapper("/role/have/list")
    /*@Authentication("BASE_USER_CENTER_AUTHORIZATION_FUNCTION_ROLE_HAVE_LIST")*/
    @LoginRequired
    public List<String> roleHaveAuthorizationList(@NotNull Long roleId) {
        return authorizationRoleRelationService.getAuthorizationIdByRoleId(roleId);
    }

    /**
     * 获取用户已有功能权限
     *
     * @return
     */
    @RequestMapper("/user/have/list")
    /*@Authentication("BASE_USER_CENTER_AUTHORIZATION_FUNCTION_USER_HAVE_LIST")*/
    @LoginRequired
    public List<String> userHaveAuthorizationList() throws BizException {
        return authorizationRoleRelationService.getAuthorizationCodeByUserId(ThreadContext.getUserInfo().getUserId());
    }

}
