package com.shsnc.base.authorization.model;

import java.io.Serializable;

/**
 * Created by Elena on 2017/6/6.
 */
public class AuthorizationRoleRelationModel implements Serializable {

    private Long id;

    private Long authorizationId;

    private Long roleId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAuthorizationId() {
        return authorizationId;
    }

    public void setAuthorizationId(Long authorizationId) {
        this.authorizationId = authorizationId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}
