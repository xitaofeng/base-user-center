package com.shsnc.base.authorization.bean;

import com.shsnc.api.core.validation.ValidationType;

import javax.validation.constraints.NotNull;

/**
 * Created by Elena on 2017/6/28.
 */
public class AuthorizationResourcePropertyParam {

    @NotNull(groups = {ValidationType.Update.class})
    private Long propertyId;

    @NotNull(groups = {ValidationType.Add.class, ValidationType.Update.class})
    private String resourceTypeCode;

    @NotNull(groups = {ValidationType.Add.class, ValidationType.Update.class})
    private String resourcePropertyCode;

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

    public String getResourcePropertyCode() {
        return resourcePropertyCode;
    }

    public void setResourcePropertyCode(String resourcePropertyCode) {
        this.resourcePropertyCode = resourcePropertyCode;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
}
