package com.shsnc.base.authorization.model.condition;

/**
 * 权限角色查询对象
 * Created by Elena on 2017/6/20.
 */
public class AuthorizationRoleCondition {

    private String roleName;

    private Integer isBuilt;

    private String description;

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
