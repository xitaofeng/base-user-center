package com.shsnc.base.user.bean;

import java.util.ArrayList;
import java.util.List;

public class OrganizationNode {

    private Long organizationId;

    private String name;

    private String code;

    private String description;

    private Long parentId;

    private List<OrganizationNode> children = new ArrayList<>();

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
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

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public List<OrganizationNode> getChildren() {
        return children;
    }

    public void addChild(OrganizationNode child) {
        this.children.add(child);
    }
}