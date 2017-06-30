package com.shsnc.base.authorization.bean;

import com.shsnc.api.core.validation.ValidationType;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by Elena on 2017/6/7.
 */
public class AuthorizationRole implements Serializable {

    @NotNull(groups = {ValidationType.Update.class})
    private Long roleId;

    @NotNull(groups = {ValidationType.Add.class})
    @Max(50)
    private String roleName;

    @NotNull(groups = {ValidationType.Add.class})
    @Max(50)
    private String roleCode;

    private Integer isBuilt;

    @Max(200)
    private String description;

    private Long createTime;

    private Integer orders;

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Integer getIsBuilt() {
        return isBuilt;
    }

    public void setIsBuilt(Integer isBuilt) {
        this.isBuilt = isBuilt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Integer getOrders() {
        return orders;
    }

    public void setOrders(Integer orders) {
        this.orders = orders;
    }
}
