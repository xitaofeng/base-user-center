package com.shsnc.base.authorization.bean;

import java.io.Serializable;

/**
 * 用户数据权限
 * Created by Elena on 2017/6/14.
 */
public class UserDataAuthorization implements Serializable {

    private Integer resourceType;

    private Long resourceId;

    private String propertyValues;

    public Integer getResourceType() {
        return resourceType;
    }

    public void setResourceType(Integer resourceType) {
        this.resourceType = resourceType;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    public String getPropertyValues() {
        return propertyValues;
    }

    public void setPropertyValues(String propertyValues) {
        this.propertyValues = propertyValues;
    }
}
