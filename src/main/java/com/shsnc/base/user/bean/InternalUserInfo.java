package com.shsnc.base.user.bean;

import com.shsnc.base.user.config.UserConstant;

import java.util.List;

/**
 * @author houguangqiang
 * @date 2017-07-31
 * @since 1.0
 */
public class InternalUserInfo {

    private Long userId;
    private String username;
    private String alias;
    private String mobile;
    private String email;
    private Integer internal;
    private Integer status;
    private boolean superAdmin;
    private List<Long> groupIds;
    
    private List<Long> resourceGroupIds;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public List<Long> getGroupIds() {
        return groupIds;
    }

    public void setGroupIds(List<Long> groupIds) {
        this.groupIds = groupIds;
    }

    public boolean getSuperAdmin() {
        return this.internal == UserConstant.USER_INTERNAL_TRUE;
}

    public void setSuperAdmin(Boolean superAdmin) {
        this.superAdmin = superAdmin;
    }

    public List<Long> getResourceGroupIds() {
        return resourceGroupIds;
    }

    public void setResourceGroupIds(List<Long> resourceGroupIds) {
        this.resourceGroupIds = resourceGroupIds;
    }
    
    
}
