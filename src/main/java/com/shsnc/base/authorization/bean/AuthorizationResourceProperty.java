package com.shsnc.base.authorization.bean;

import com.shsnc.api.core.validation.Validate;
import com.shsnc.api.core.validation.ValidationType;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by Elena on 2017/6/12.
 */
public class AuthorizationResourceProperty implements Serializable {

    @NotNull(groups = {ValidationType.Update.class})
    private Long id;

    @NotNull(groups = {ValidationType.Add.class})
    private String propertyName;

    @NotNull(groups = {ValidationType.Add.class})
    private Integer propertyValue;

    @NotNull(groups = {ValidationType.Add.class})
    private Integer resourceType;

    @NotNull(groups = {ValidationType.Add.class})
    private String resourceTypeName;

    private Long parentId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public Integer getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(Integer propertyValue) {
        this.propertyValue = propertyValue;
    }

    public Integer getResourceType() {
        return resourceType;
    }

    public void setResourceType(Integer resourceType) {
        this.resourceType = resourceType;
    }

    public String getResourceTypeName() {
        return resourceTypeName;
    }

    public void setResourceTypeName(String resourceTypeName) {
        this.resourceTypeName = resourceTypeName;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
}
