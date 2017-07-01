package com.shsnc.base.authorization.bean;

import com.shsnc.api.core.validation.ValidationType;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 数据授权对象
 * Created by Elena on 2017/6/12.
 */
public class DataAuthorization implements Serializable {

    @NotNull(groups = ValidationType.Add.class)
    private Integer resourceType;

    private List<Long> authRoleList;

    private List<Long> authUserList;

    @NotEmpty(groups = ValidationType.Add.class)
    private List<Long> resourceIdList;

    @NotEmpty(groups = ValidationType.Add.class)
    private List<Long> propertyIdList;

    public Integer getResourceType() {
        return resourceType;
    }

    public void setResourceType(Integer resourceType) {
        this.resourceType = resourceType;
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

    public List<Long> getAuthRoleList() {
        return authRoleList;
    }

    public void setAuthRoleList(List<Long> authRoleList) {
        this.authRoleList = authRoleList;
    }

    public List<Long> getAuthUserList() {
        return authUserList;
    }

    public void setAuthUserList(List<Long> authUserList) {
        this.authUserList = authUserList;
    }
}
