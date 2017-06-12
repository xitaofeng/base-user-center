package com.shsnc.base.authorization.handler;

import com.shsnc.api.core.RequestHandler;
import com.shsnc.api.core.annotation.RequestMapper;
import com.shsnc.base.authorization.bean.DataAuthorization;
import com.shsnc.base.authorization.service.DataAuthorizationService;
import com.shsnc.base.util.config.BizException;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by Elena on 2017/6/7.
 * 数据权限处理器
 */
@Component
@RequestMapper("/authorization/data")
public class DataAuthorizationHandler implements RequestHandler {

    @RequestMapper("")
    public boolean auth(@NotEmpty List<DataAuthorization> dataAuthorizations) throws BizException {
        return dataAuthorizationService.auth(dataAuthorizations);
    }

    @Autowired
    private DataAuthorizationService dataAuthorizationService;

    @RequestMapper("/user")
    public boolean userAuth(@NotNull Integer resourceType, @NotEmpty List<Long> userIdList, @NotEmpty List<Long> resourceIdList, @NotEmpty List<Long> propertyIdList) throws Exception {
        return dataAuthorizationService.userAuth(resourceType, userIdList, resourceIdList, propertyIdList);
    }


    @RequestMapper("/role")
    public boolean roleAuth(@NotNull Integer resourceType, @NotEmpty List<Long> roleIdList, @NotEmpty List<Long> resourceIdList, @NotEmpty List<Long> propertyIdList) throws BizException {
        return dataAuthorizationService.roleAuth(resourceType,roleIdList,resourceIdList, propertyIdList);
    }


}
