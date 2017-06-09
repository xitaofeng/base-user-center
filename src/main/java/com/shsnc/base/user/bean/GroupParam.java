package com.shsnc.base.user.bean;

import com.shsnc.api.core.validation.ValidationType;

import javax.validation.constraints.NotNull;

public class GroupParam {

    @NotNull(groups = ValidationType.Update.class)
    private Long groupId;

    @NotNull(groups = ValidationType.Add.class)
    private String name;

    @NotNull(groups = ValidationType.Add.class)
    private String code;

    private Long parentId;

    private String description;

    private Integer status;

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
        this.name = name == null ? null : name.trim();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
}