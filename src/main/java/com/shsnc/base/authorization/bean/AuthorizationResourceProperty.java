package com.shsnc.base.authorization.bean;

import com.shsnc.api.core.validation.ValidationType;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by Elena on 2017/6/12.
 */
public class AuthorizationResourceProperty implements Serializable {

    @NotNull(groups = {ValidationType.Update.class})
    private Long propertyId;

    @NotNull(groups = {ValidationType.Add.class})
    private String propertyName;

    @NotNull(groups = {ValidationType.Add.class})
    private String propertyValue;

    @NotNull(groups = {ValidationType.Add.class})
    private String resourceTypeCode;

    @NotNull(groups = {ValidationType.Add.class})
    private String resourceTypeName;

    private Long parentId;

    public String getResourceTypeCode() {
        return resourceTypeCode;
    }

    public void setResourceTypeCode(String resourceTypeCode) {
        this.resourceTypeCode = resourceTypeCode;
    }

    public Long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Long propertyId) {
        this.propertyId = propertyId;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(String propertyValue) {
        this.propertyValue = propertyValue;
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
