package com.shsnc.base.user.bean;

import com.shsnc.api.core.validation.ValidationType;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 */
public class UserInfoParam {

    @NotNull(groups = {ValidationType.Update.class})
    private Long userId;
    @NotNull(groups = {ValidationType.Add.class})
    private String username;
    private String alias;
    private String mobile;
    private Integer status;
    @NotEmpty(groups = {ValidationType.Add.class})
    private List<Long> groupIds;
    private List<ExtendPropertyValue> extendPropertyValues;


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<Long> getGroupIds() {
        return groupIds;
    }

    public void setGroupIds(List<Long> groupIds) {
        this.groupIds = groupIds;
    }

    public List<ExtendPropertyValue> getExtendPropertyValues() {
        return extendPropertyValues;
    }

    public void setExtendPropertyValues(List<ExtendPropertyValue> extendPropertyValues) {
        this.extendPropertyValues = extendPropertyValues;
    }
}
