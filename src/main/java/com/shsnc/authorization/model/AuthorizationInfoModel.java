package com.shsnc.authorization.model;

/**
 * Created by LuoJun on 2017/6/5.
 */
public class AuthorizationInfoModel {

    private Integer authorizationId;

    private String authorizationName;

    private String authorizationCode;

    private Integer authorizationStatus;

    private String description;

    private Long createTime;

    /**
     * 状态
     */
    public enum AuthorizationStatus {
        //启用
        START(1),
        //停用
        STOP(2);

        private Integer authorizationStatus;

        public Integer getAuthorizationStatus() {
            return authorizationStatus;
        }

        AuthorizationStatus(Integer authorizationStatus) {
            this.authorizationStatus = authorizationStatus;
        }
    }

    public Integer getAuthorizationId() {
        return authorizationId;
    }

    public void setAuthorizationId(Integer authorizationId) {
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
