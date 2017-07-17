package com.shsnc.base.authorization.bean;

import com.shsnc.api.core.validation.ValidationType;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

/**
 * Created by Elena on 2017/7/14.
 */
public class DataAuthorizationRole {

    @NotEmpty(groups = ValidationType.Add.class)
    private Long roleId;

    @NotEmpty(groups = ValidationType.Add.class)
    private List<Long> propertyIdList;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public List<Long> getPropertyIdList() {
        return propertyIdList;
    }

    public void setPropertyIdList(List<Long> propertyIdList) {
        this.propertyIdList = propertyIdList;
    }
}
