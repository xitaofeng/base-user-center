package com.shsnc.base.authorization.bean;

import java.io.Serializable;

/**
 * 用户数据权限
 * Created by Elena on 2017/6/14.
 */
public class UserDataAuthorization implements Serializable {

    private Integer resourceType;

    private Long resourceId;

    private Integer propertyValue;

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

    public Integer getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(Integer propertyValue) {
        this.propertyValue = propertyValue;
    }
}
