package com.shsnc.base.user.model;

public class UserGroupResourceGroupRelationModel {
    
    /**
     * 自增主键
     */
    private Long relationId;

    /**
     * 用户组id
     */
    private Long userGroupId;

    /**
     * 资源组id
     */
    private Long resourceGroupId;

    /**
     * 自增主键
     */
    public Long getRelationId() {
        return relationId;
    }

    /**
     * 自增主键
     */
    public void setRelationId(Long relationId) {
        this.relationId = relationId;
    }

    /**
     * 用户组id
     */
    public Long getUserGroupId() {
        return userGroupId;
    }

    /**
     * 用户组id
     */
    public void setUserGroupId(Long userGroupId) {
        this.userGroupId = userGroupId;
    }

    /**
     * 资源组id
     */
    public Long getResourceGroupId() {
        return resourceGroupId;
    }

    /**
     * 资源组id
     */
    public void setResourceGroupId(Long resourceGroupId) {
        this.resourceGroupId = resourceGroupId;
    }
}