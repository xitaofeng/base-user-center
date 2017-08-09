package com.shsnc.base.user.bean;

import com.shsnc.base.module.bean.ResourceGroupInfo;

import java.util.List;

public class Group {

    private Long groupId;
    private String name;
    private String code;
    private List<UserInfo> users;
    private List<ResourceGroupInfo> resourceGroups;

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<UserInfo> getUsers() {
        return users;
    }

    public void setUsers(List<UserInfo> users) {
        this.users = users;
    }

    public List<ResourceGroupInfo> getResourceGroups() {
        return resourceGroups;
    }

    public void setResourceGroups(List<ResourceGroupInfo> resourceGroups) {
        this.resourceGroups = resourceGroups;
    }
}
