package com.shsnc.base.authorization.bean;

import com.shsnc.api.core.validation.Validate;
import com.shsnc.api.core.validation.ValidationType;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by Elena on 2017/6/5.
 */
public class AuthorizationInfo implements Serializable {

    @NotNull(groups = {ValidationType.Update.class})
    private Long authorizationId;

    @NotNull(groups = {ValidationType.Add.class})
    @Max(50)
    private String authorizationName;

    @NotNull(groups = {ValidationType.Add.class})
    @Max(100)
    private String authorizationCode;

    private Integer authorizationStatus;

    @Max(200)
    private String description;

    private Long createTime;

    public Long getAuthorizationId() {
        return authorizationId;
    }

    public void setAuthorizationId(Long authorizationId) {
        this.authorizationId = authorizationId;
    }

    public String getAuthorizationName() {
        return authorizationName;
    }

    public void setAuthorizationName(String authorizationName) {
        this.authorizationName = authorizationName;
    }

    public String getAuthorizationCode() {
        return authorizationCode;
    }

    public void setAuthorizationCode(String authorizationCode) {
        this.authorizationCode = authorizationCode;
    }

    public Integer getAuthorizationStatus() {
        return authorizationStatus;
    }

    public void setAuthorizationStatus(Integer authorizationStatus) {
        this.authorizationStatus = authorizationStatus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
}
