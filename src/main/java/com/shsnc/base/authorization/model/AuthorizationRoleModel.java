package com.shsnc.base.authorization.model;

import java.io.Serializable;

/**
 * Created by LuoJun on 2017/6/6.
 */
public class AuthorizationRoleModel implements Serializable {

    private Long roleId;

    private String roleName;

    private Integer isBuilt;

    private String description;

    private Long createTime;

    private Integer orders;

    /**
     * 是否内置
     */
    public enum EnumIsBuilt {
        //启用
        TRUE(1),
        //停用
        FALSE(0);

        private Integer value;

        public Integer getValue() {
            return value;
        }

        EnumIsBuilt(Integer value) {
            this.value = value;
        }
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
