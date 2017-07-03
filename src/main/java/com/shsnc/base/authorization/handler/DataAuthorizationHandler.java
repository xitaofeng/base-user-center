package com.shsnc.base.authorization.handler;

import com.shsnc.api.core.RequestHandler;
import com.shsnc.api.core.annotation.Authentication;
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
 */
@Component
@RequestMapper("/authorization/data")
public class DataAuthorizationHandler implements RequestHandler {

    @Autowired
    private DataAuthorizationService dataAuthorizationService;

    @RequestMapper("")
    @Validate(groups = ValidationType.Add.class)
    @Authentication("BASE_USER_CENTER_AUTHORIZATION_DATA")
    public boolean auth(DataAuthorization dataAuthorization) throws BizException {
        return dataAuthorizationService.auth(dataAuthorization);
    }

    @RequestMapper("/user")
    @Validate
    @Authentication("BASE_USER_CENTER_AUTHORIZATION_DATA_USER")
    public boolean userAuth(@NotNull Integer resourceType, @NotEmpty List<Long> userIdList, @NotEmpty List<Long> resourceIdList, @NotEmpty List<Long> propertyIdList) throws Exception {
        return dataAuthorizationService.userAuth(resourceType, userIdList, resourceIdList, propertyIdList);
    }

    @RequestMapper("/role")
    @Validate
    @Authentication("BASE_USER_CENTER_AUTHORIZATION_DATA_ROLE")
    public boolean roleAuth(@NotNull Integer resourceType, @NotEmpty List<Long> roleIdList, @NotEmpty List<Long> resourceIdList, @NotEmpty List<Long> propertyIdList) throws BizException {
        return dataAuthorizationService.roleAuth(resourceType, roleIdList, resourceIdList, propertyIdList);
    }

    @RequestMapper("/auth/value")
    @Validate
    @Authentication("BASE_USER_CENTER_AUTHORIZATION_DATA_AUTH_VALUE")
    public String[] authValue(@NotNull Long userId, @NotNull Integer resourceType, @NotNull Long resourceId) throws BizException {
        return dataAuthorizationService.getAuthValue(userId, resourceType, resourceId);
    }


    /**
     *
     * @param userId  用户id
     * @param resourceType 资源类型
     * @param authorizationPropertyValue 权限属性值
     * @return
     * @throws BizException
     */
    @RequestMapper("/authorization/value")
    @Validate
    @Authentication("BASE_USER_CENTER_AUTHORIZATION_DATA_AUTHORIZATION_VALUE")
    public Map<Long, String> getUserResourceTypeAutValuehList(@NotNull Long userId, @NotNull Integer resourceType,String authorizationPropertyValue) throws BizException {
        return dataAuthorizationService.getUserResourceTypeAutValuehList(userId, resourceType,authorizationPropertyValue);
    }



}
