package com.shsnc.base.authorization.handler;

import com.shsnc.api.core.RequestHandler;
import com.shsnc.api.core.annotation.RequestMapper;
import com.shsnc.base.authorization.service.AuthorizationRoleRelationService;
import com.shsnc.base.authorization.service.AuthorizationRoleService;
import com.shsnc.base.util.config.BizException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

/**
 * 权限内部接口地址
 */
@Component
@RequestMapper("/authorization/internal")
public class AuthorizationInternalHandler implements RequestHandler {

    private Logger logger = LoggerFactory.getLogger(AuthorizationInternalHandler.class);

    @Autowired
    private AuthorizationRoleRelationService authorizationRoleRelationService;

    @Autowired
    private AuthorizationRoleService authorizationRoleService;

    /**
     * 判断权限是否拥有该用户
     * @param userId
     * @return
     */
    @RequestMapper("/user/have")
    public boolean userHaveAuthorization(@NotNull Long userId, @NotNull String authorizationCode) throws BizException {
        if(authorizationRoleService.isSuperAdmin(userId)){
            return true;
        }
        return authorizationRoleRelationService.userHaveAuthorization(userId, authorizationCode);
    }
}
