package com.shsnc.base.user.bean;

import com.shsnc.base.authorization.bean.AuthorizationRole;

import java.util.List;

/**
 *
 */
public class UserInfo {

    private Long userId;
    private String username;
    private String alias;
    private String mobile;
    private String email;
    private Integer internal;
    private Integer status;
    private List<Long> roleIds;
    private List<Group> groups;
    private List<AuthorizationRole> roles;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getInternal() {
        return internal;
    }

    public void setInternal(Integer internal) {
        this.internal = internal;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Long> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Long> roleIds) {
        this.roleIds = roleIds;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public List<AuthorizationRole> getRoles() {
        return roles;
    }

    public void setRoles(List<AuthorizationRole> roles) {
        this.roles = roles;
    }
}
