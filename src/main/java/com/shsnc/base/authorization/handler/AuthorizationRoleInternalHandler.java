package com.shsnc.base.authorization.handler;

import com.shsnc.api.core.RequestHandler;
import com.shsnc.api.core.annotation.RequestMapper;
import com.shsnc.api.core.validation.Validate;
import com.shsnc.base.authorization.service.AuthorizationRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;


@Component
@RequestMapper("/authorization/internal/role")
public class AuthorizationRoleInternalHandler implements RequestHandler {

    @Autowired
    private AuthorizationRoleService authorizationRoleService;

    @RequestMapper("/is/admin")
    @Validate
    public boolean isAdmin(@NotNull Long userId) throws Exception {
        return authorizationRoleService.isAdmin(userId);
    }

}
