package com.shsnc.base.user.model;

/**
 * Created by houguangqiang on 2017/6/7.
 */
public class ExtendPropertyCondition {
    private String propertyName;

    private String description;

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
