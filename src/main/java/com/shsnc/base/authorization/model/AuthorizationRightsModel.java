package com.shsnc.base.authorization.model;

public class AuthorizationRightsModel {
    
    /**
     * 自增主键
     */
    private Long rightId;

    /**
     * 目标对象类型，1=资源组，2=脚本，3=编排
     */
    private Integer objectType;

    /**
     * 目标对象类型编排，resource_group=资源组，2=script，3=orchestration
     */
    private String objectTypeCode;

    /**
     * 目标对象id，例如资源组对象id，脚本id，编排id
     */
    private Long objectId;

    /**
     * 用户组id
     */
    private Long groupId;

    /**
     * 权限值，用于数据权限控制（查看编辑删除执行等）
     */
    private Integer permission;

    /**
     * 自增主键
     */
    public Long getRightId() {
        return rightId;
    }

    /**
     * 自增主键
     */
    public void setRightId(Long rightId) {
        this.rightId = rightId;
    }

    /**
     * 目标对象类型，1=资源组，2=脚本，3=编排
     */
    public Integer getObjectType() {
        return objectType;
    }

    /**
     * 目标对象类型，1=资源组，2=脚本，3=编排
     */
    public void setObjectType(Integer objectType) {
        this.objectType = objectType;
    }

    /**
     * 目标对象类型编排，resource_group=资源组，2=script，3=orchestration
     */
    public String getObjectTypeCode() {
        return objectTypeCode;
    }

    /**
     * 目标对象类型编排，resource_group=资源组，2=script，3=orchestration
     */
    public void setObjectTypeCode(String objectTypeCode) {
        this.objectTypeCode = objectTypeCode;
    }

    /**
     * 目标对象id，例如资源组对象id，脚本id，编排id
     */
    public Long getObjectId() {
        return objectId;
    }

    /**
     * 目标对象id，例如资源组对象id，脚本id，编排id
     */
    public void setObjectId(Long objectId) {
        this.objectId = objectId;
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

    /**
     * 权限值，用于数据权限控制（查看编辑删除执行等）
     */
    public Integer getPermission() {
        return permission;
    }

    /**
     * 权限值，用于数据权限控制（查看编辑删除执行等）
     */
    public void setPermission(Integer permission) {
        this.permission = permission;
    }
}