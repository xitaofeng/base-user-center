package com.shsnc.base.user.model;

import com.shsnc.base.authorization.model.AuthorizationRoleModel;

import java.util.List;

public class UserInfoModel {

    private Long userId;

    private String username;

    private String password;

    private String alias;

    private String mobile;

    private String email;

    private Integer internal;

    private Integer status;

    private Long createTime;

    private Long attemptTime;

    private String attemptIp;

    private Integer isDelete;

    private Long organizationId;
    private OrganizationModel organization;

    private Long defaultGroupId;
    private GroupModel defaultGroup;
    private List<Long> groupIds;
    private List<GroupModel> groups;

    private List<Long> roleIds;
    private List<AuthorizationRoleModel> roles;

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
        this.username = username == null ? null : username.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias == null ? null : alias.trim();
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
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

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getAttemptTime() {
        return attemptTime;
    }

    public void setAttemptTime(Long attemptTime) {
        this.attemptTime = attemptTime;
    }

    public String getAttemptIp() {
        return attemptIp;
    }

    public void setAttemptIp(String attemptIp) {
        this.attemptIp = attemptIp == null ? null : attemptIp.trim();
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public OrganizationModel getOrganization() {
        return organization;
    }

    public void setOrganization(OrganizationModel organization) {
        this.organization = organization;
    }

    public List<Long> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Long> roleIds) {
        this.roleIds = roleIds;
    }

    public List<AuthorizationRoleModel> getRoles() {
        return roles;
    }

    public void setRoles(List<AuthorizationRoleModel> roles) {
        this.roles = roles;
    }

    public List<Long> getGroupIds() {
        return groupIds;
    }

    public void setGroupIds(List<Long> groupIds) {
        this.groupIds = groupIds;
    }

    public List<GroupModel> getGroups() {
        return groups;
    }

    public void setGroups(List<GroupModel> groups) {
        this.groups = groups;
    }

    public Long getDefaultGroupId() {
        return defaultGroupId;
    }

    public void setDefaultGroupId(Long defaultGroupId) {
        this.defaultGroupId = defaultGroupId;
    }

    public GroupModel getDefaultGroup() {
        return defaultGroup;
    }

    public void setDefaultGroup(GroupModel defaultGroup) {
        this.defaultGroup = defaultGroup;
    }
}