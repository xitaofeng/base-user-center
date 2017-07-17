package com.shsnc.base.authorization.bean;

import com.shsnc.api.core.validation.ValidationType;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

/**
 * Created by Elena on 2017/7/14.
 */
public class DataAuthorizationUser {

    @NotEmpty(groups = ValidationType.Add.class)
    private Long userId;

    @NotEmpty(groups = ValidationType.Add.class)
    private List<Long> propertyIdList;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<Long> getPropertyIdList() {
        return propertyIdList;
    }

    public void setPropertyIdList(List<Long> propertyIdList) {
        this.propertyIdList = propertyIdList;
    }
}
