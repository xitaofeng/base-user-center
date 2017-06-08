package com.shsnc.base.user.bean;

import com.shsnc.api.core.validation.ValidationType;

import javax.validation.constraints.NotNull;

public class ExtendPropertyValueParam {

    @NotNull(groups = ValidationType.Update.class)
    private Long propertyValueId;

    @NotNull(groups = ValidationType.Add.class)
    private Long userId;

    @NotNull(groups = ValidationType.Add.class)
    private Long propertyId;

    @NotNull(groups = ValidationType.Add.class)
    private String propertyValue;

    public Long getPropertyValueId() {
        return propertyValueId;
    }

    public void setPropertyValueId(Long propertyValueId) {
        this.propertyValueId = propertyValueId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Long propertyId) {
        this.propertyId = propertyId;
    }

    public String getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(String propertyValue) {
        this.propertyValue = propertyValue == null ? null : propertyValue.trim();
    }
}