package com.shsnc.base.authorization.model;

import java.io.Serializable;

/**
 * 资源数据授权
 * Created by Elena on 2017/6/9.
 */
public class AuthorizationResourceAuthModel implements Serializable {

    /**
     * 创建用户授对象
     *
     * @param resourceTypeCode
     * @param resourceId
     * @param propertyId
     * @param enumAuthType
     * @param authValue
     * @return
     */
    public static AuthorizationResourceAuthModel createAuthorizationResourceAuthModel(String resourceTypeCode, Long resourceId, Long propertyId, EnumAuthType enumAuthType, Long authValue) {
        AuthorizationResourceAuthModel authorizationResourceAuthModel = new AuthorizationResourceAuthModel();
        authorizationResourceAuthModel.setResourceTypeCode(resourceTypeCode);
        authorizationResourceAuthModel.setResourceId(resourceId);
        authorizationResourceAuthModel.setPropertyId(propertyId);
        authorizationResourceAuthModel.setAuthType(enumAuthType.getValue());
        authorizationResourceAuthModel.setAuthValue(authValue);
        return authorizationResourceAuthModel;
    }

    /**
     * 授权类型
     */
    public enum EnumAuthType {
        //用户
        USER(1),
        //角色
        ROLE(2),
        //组
        GROUP(3);

        private Integer value;

        public Integer getValue() {
            return value;
        }

        EnumAuthType(Integer value) {
            this.value = value;
        }
    }

    private Long id;

    private String resourceTypeCode;

    private Long resourceId;

    private Long propertyId;

    private Integer authType;

    private Long authValue;

    public String getResourceTypeCode() {
        return resourceTypeCode;
    }

    public void setResourceTypeCode(String resourceTypeCode) {
        this.resourceTypeCode = resourceTypeCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    public Long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Long propertyId) {
        this.propertyId = propertyId;
    }

    public Integer getAuthType() {
        return authType;
    }

    public void setAuthType(Integer authType) {
        this.authType = authType;
    }

    public Long getAuthValue() {
        return authValue;
    }

    public void setAuthValue(Long authValue) {
        this.authValue = authValue;
    }
}
