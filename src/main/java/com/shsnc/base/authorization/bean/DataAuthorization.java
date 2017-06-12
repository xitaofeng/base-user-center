package com.shsnc.base.authorization.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 数据授权
 * Created by Elena on 2017/6/12.
 */
public class DataAuthorization implements Serializable {

    private Integer resourceType;

    private Integer authType;

    private List<Long> authValueList;

    private List<Long> resourceIdList;

    private List<Long> propertyIdList;

    public Integer getResourceType() {
        return resourceType;
    }

    public void setResourceType(Integer resourceType) {
        this.resourceType = resourceType;
    }

    public Integer getAuthType() {
        return authType;
    }

    public void setAuthType(Integer authType) {
        this.authType = authType;
    }

    public List<Long> getAuthValueList() {
        return authValueList;
    }

    public void setAuthValueList(List<Long> authValueList) {
        this.authValueList = authValueList;
    }

    public List<Long> getResourceIdList() {
        return resourceIdList;
    }

    public void setResourceIdList(List<Long> resourceIdList) {
        this.resourceIdList = resourceIdList;
    }

    public List<Long> getPropertyIdList() {
        return propertyIdList;
    }

    public void setPropertyIdList(List<Long> propertyIdList) {
        this.propertyIdList = propertyIdList;
    }
}
