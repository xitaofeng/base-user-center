package com.shsnc.base.user.model;

import com.shsnc.base.module.bean.ResourceGroupInfo;

import java.util.List;

public class GroupModel {
    
    /**
     * 自增主键
     */
    private Long groupId;

    /**
     * 用户组名
     */
    private String name;

    /**
     * 用户组编码
     */
    private String code;

    private List<UserInfoModel> users;
    private List<ResourceGroupInfo> resourceGroups;


    /**
     * 自增主键
     */
    public Long getGroupId() {
        return groupId;
    }

    /**
     * 自增主键
     */
    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    /**
     * 用户组名
     */
    public String getName() {
        return name;
    }

    /**
     * 用户组名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 用户组编码
     */
    public String getCode() {
        return code;
    }

    /**
     * 用户组编码
     */
    public void setCode(String code) {
        this.code = code;
    }

    public List<UserInfoModel> getUsers() {
        return users;
    }

    public void setUsers(List<UserInfoModel> users) {
        this.users = users;
    }

    public List<ResourceGroupInfo> getResourceGroups() {
        return resourceGroups;
    }

    public void setResourceGroups(List<ResourceGroupInfo> resourceGroups) {
        this.resourceGroups = resourceGroups;
    }
}