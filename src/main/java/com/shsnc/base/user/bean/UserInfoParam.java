package com.shsnc.base.user.bean;

import com.shsnc.api.core.validation.ValidationType;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 */
public class UserInfoParam {

    @NotNull(groups = {ValidationType.Update.class})
    private Long userId;
    @NotNull(groups = {ValidationType.Add.class})
    private String username;
    @NotNull(groups = {ValidationType.Add.class})
    private String password;
    private String alias;
    private String email;
    private String mobile;
    private Integer status;
    @NotNull(groups = {ValidationType.Add.class})
    private Long organizationId;
    @NotNull(groups = {ValidationType.Add.class})
    private Long defaultGroupId;
    @NotEmpty(groups = {ValidationType.Add.class})
    private List<Long> groupIds;
    private List<ExtendPropertyValue> extendPropertyValues;
    private List<Long> roleIds;


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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public List<Long> getGroupIds() {
        return groupIds;
    }

    public void setGroupIds(List<Long> groupIds) {
        this.groupIds = groupIds;
    }

    public List<ExtendPropertyValue> getExtendPropertyValues() {
        return extendPropertyValues;
    }

    public void setExtendPropertyValues(List<ExtendPropertyValue> extendPropertyValues) {
        this.extendPropertyValues = extendPropertyValues;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public Long getDefaultGroupId() {
        return defaultGroupId;
    }

    public void setDefaultGroupId(Long defaultGroupId) {
        this.defaultGroupId = defaultGroupId;
    }
}
