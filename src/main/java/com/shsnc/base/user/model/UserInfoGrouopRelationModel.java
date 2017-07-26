package com.shsnc.base.user.model;

public class UserInfoGrouopRelationModel {
    
    /**
     * 自增主键
     */
    private Long relationId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户组id
     */
    private Long groupId;

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
     * 用户id
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * 用户id
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * 用户组id
     */
    public Long getGroupId() {
        return groupId;
    }

    /**
     * 用户组id
     */
    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }
}