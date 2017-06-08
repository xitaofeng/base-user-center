package com.shsnc.base.authorization.model;

import java.io.Serializable;

/**
 * Created by LuoJun on 2017/6/5.
 */
public class AuthorizationInfoModel implements Serializable{

    private Long authorizationId;

    private String authorizationName;

    private String authorizationCode;

    private Integer authorizationStatus;

    private String description;

    private Long createTime;

    /**
     * 状态
     */
    public enum EnumAuthorizationStatus {
        //启用
        ENABLED(1),
        //停用
        DISABLED(2);

        private Integer authorizationStatus;

        public Integer getAuthorizationStatus() {
            return authorizationStatus;
        }

        EnumAuthorizationStatus(Integer authorizationStatus) {
            this.authorizationStatus = authorizationStatus;
        }
    }

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
