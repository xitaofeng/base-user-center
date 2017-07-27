package com.shsnc.base.authorization.model.condition;

import java.util.List;

/**
 * 权限角色查询对象
 * Created by Elena on 2017/6/20.
 */
public class AuthorizationRoleCondition {

    private String roleName;

    private Integer isBuilt;

    private String description;

    private String roleCode;

    private Long userId;

    private List<Long> roleIds;

    public List<Long> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Long> roleIds) {
        this.roleIds = roleIds;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
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
}
