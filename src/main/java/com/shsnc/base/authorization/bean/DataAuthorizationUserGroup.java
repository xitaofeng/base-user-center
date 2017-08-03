package com.shsnc.base.authorization.bean;

import com.shsnc.api.core.validation.ValidationType;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

/**
 * Created by Elena on 2017/7/14.
 */
public class DataAuthorizationUserGroup {

    @NotEmpty(groups = ValidationType.Add.class)
    private Long groupId;

    @NotEmpty(groups = ValidationType.Add.class)
    private List<Long> propertyIdList;

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public List<Long> getPropertyIdList() {
        return propertyIdList;
    }

    public void setPropertyIdList(List<Long> propertyIdList) {
        this.propertyIdList = propertyIdList;
    }
}
