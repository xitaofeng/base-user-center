package com.shsnc.base.authorization.handler;

import com.shsnc.api.core.RequestHandler;
import com.shsnc.api.core.annotation.Authentication;
import com.shsnc.api.core.annotation.LoginRequired;
import com.shsnc.api.core.annotation.RequestMapper;
import com.shsnc.api.core.validation.Validate;
import com.shsnc.api.core.validation.ValidationType;
import com.shsnc.base.authorization.bean.DataAuthorization;
import com.shsnc.base.authorization.service.DataAuthorizationService;
import com.shsnc.base.util.config.BizException;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * Created by Elena on 2017/6/7.
 * 数据权限处理器
 * @deprecated 请用 {@link AuthorizationRightInternalHandler} 取代
 */
@Component
@RequestMapper("/authorization/data")
public class DataAuthorizationHandler implements RequestHandler {

    @Autowired
    private DataAuthorizationService dataAuthorizationService;

    @RequestMapper("")
    @Validate(groups = ValidationType.Add.class)
    @Authentication("BASE_USER_CENTER_AUTHORIZATION_DATA")
    public boolean auth(@NotNull String resourceTypeCode,@NotEmpty List<DataAuthorization> dataAuthorizationList) throws BizException {
        return dataAuthorizationService.auth(resourceTypeCode, dataAuthorizationList);
    }

    @RequestMapper("/user")
    @Validate
    @Authentication("BASE_USER_CENTER_AUTHORIZATION_DATA_USER")
    public boolean userAuth(@NotNull String resourceTypeCode, @NotEmpty List<Long> userIdList, @NotEmpty List<Long> resourceIdList, @NotEmpty List<Long> propertyIdList) throws Exception {
        return false;//dataAuthorizationService.userAuth(resourceTypeCode, userIdList, resourceIdList, propertyIdList);
    }

    @RequestMapper("/role")
    @Validate
    @Authentication("BASE_USER_CENTER_AUTHORIZATION_DATA_ROLE")
    public boolean roleAuth(@NotNull String resourceTypeCode, @NotEmpty List<Long> roleIdList, @NotEmpty List<Long> resourceIdList, @NotEmpty List<Long> propertyIdList) throws BizException {
        return false;// dataAuthorizationService.roleAuth(resourceTypeCode, roleIdList, resourceIdList, propertyIdList);
    }

    @RequestMapper("/auth/value")
    @Validate
    @Authentication("BASE_USER_CENTER_AUTHORIZATION_DATA_AUTH_VALUE")
    public String[] authValue(@NotNull Long userId, @NotNull String resourceTypeCode, @NotNull Long resourceId) throws BizException {
        return dataAuthorizationService.getAuthValue(userId, resourceTypeCode, resourceId);
    }


    /**
     * 获取用户指定资源类型下指定权限属性
     *
     * @param userId                     用户id
     * @param resourceTypeCode           资源类型编码
     * @param authorizationPropertyValue 权限属性值
     * @return
     * @throws BizException
     */
    @RequestMapper("/resource/type/code/property/value")
    @Validate
    //@Authentication("BASE_USER_CENTER_AUTHORIZATION_DATA_RESOURCE_TYPE_CODE_PROPERTY_VALUE")
    @LoginRequired
    public Map<Long, String> getUserAutValueListByResourceTypeAndPropertyValue(@NotNull Long userId, @NotNull String resourceTypeCode, String authorizationPropertyValue) throws BizException {
        return dataAuthorizationService.getUserAutValueListByResourceTypeAndPropertyValue(userId, resourceTypeCode, authorizationPropertyValue);
    }

    /**
     * 资源类型权限值列表
     *
     * @param userId
     * @param resourceTypeCode
     * @return
     * @throws BizException
     */
    @RequestMapper("/resource/type/code/value")
    @Validate
    //@Authentication("BASE_USER_CENTER_AUTHORIZATION_DATA_RESOURCE_TYPE_VALUE")
    @LoginRequired
    public Map<Long, String> getUserAutValuehListByResourceTypeCode(@NotNull Long userId, @NotNull String resourceTypeCode) throws BizException {
        return dataAuthorizationService.getUserAutValuehListByResourceType(userId, resourceTypeCode);
    }

}
