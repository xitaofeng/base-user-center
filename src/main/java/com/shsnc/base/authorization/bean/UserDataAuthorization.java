package com.shsnc.base.authorization.bean;

import java.io.Serializable;

/**
 * 用户数据权限
 * Created by Elena on 2017/6/14.
 */
public class UserDataAuthorization implements Serializable {

    private String resourceTypeCode;

    private Long resourceId;

    private String propertyValues;

    public String getResourceTypeCode() {
        return resourceTypeCode;
    }

    public void setResourceTypeCode(String resourceTypeCode) {
        this.resourceTypeCode = resourceTypeCode;
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
