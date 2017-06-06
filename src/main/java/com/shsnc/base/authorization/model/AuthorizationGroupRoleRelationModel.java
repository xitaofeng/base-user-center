package com.shsnc.base.authorization.model;

import java.io.Serializable;

/**
 * Created by Elena on 2017/6/6.
 */
public class AuthorizationGroupRoleRelationModel implements Serializable {

    private Integer id;

    private Integer groupId;

    private Integer roleId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
}
