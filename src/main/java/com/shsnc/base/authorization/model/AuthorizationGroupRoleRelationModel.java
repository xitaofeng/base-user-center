package com.shsnc.base.authorization.model;

import java.io.Serializable;

/**
 * Created by Elena on 2017/6/6.
 */
public class AuthorizationGroupRoleRelationModel implements Serializable {

    private Long id;

    private Long groupId;

    private Long roleId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}
