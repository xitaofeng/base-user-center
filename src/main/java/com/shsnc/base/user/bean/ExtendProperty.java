package com.shsnc.base.user.bean;

import com.shsnc.api.core.validation.Validate;
import com.shsnc.api.core.validation.ValidationType;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

public class ExtendProperty {

    @NotNull(groups = {ValidationType.Update.class, ValidationType.Delete.class})
    private Long propertyId;

    @NotNull(groups = ValidationType.Add.class)
    private String propertyName;

    private String description;

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
        this.propertyName = propertyName == null ? null : propertyName.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}