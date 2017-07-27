package com.shsnc.base.authorization.model.condition;

/**
 * Created by Elena on 2017/6/20.
 */
public class AuthorizationResourcePropertyCondition {

    private String propertyName;

    private Integer propertyValue;

    private String resourceTypeCode;

    private String resourceTypeName;

    private Long parentId;

    public String getResourceTypeCode() {
        return resourceTypeCode;
    }

    public void setResourceTypeCode(String resourceTypeCode) {
        this.resourceTypeCode = resourceTypeCode;
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
