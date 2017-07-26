package com.shsnc.base.user.model;

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

    private List<OrganizationModel> organizations;

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

    public List<OrganizationModel> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(List<OrganizationModel> organizations) {
        this.organizations = organizations;
    }
}