package com.shsnc.base.authorization.handler;

import com.shsnc.api.core.RequestHandler;
import com.shsnc.api.core.annotation.Authentication;
import com.shsnc.api.core.annotation.LoginRequired;
import com.shsnc.api.core.annotation.RequestMapper;
import com.shsnc.api.core.validation.Validate;
import com.shsnc.base.authorization.config.DataObject;
import com.shsnc.base.authorization.config.DataOperation;
import com.shsnc.base.authorization.service.AuthorizationRightsService;
import com.shsnc.base.util.config.BizException;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author houguangqiang
 * @date 2017-08-09
 * @since 1.0
 */
@Component
@LoginRequired
@RequestMapper("/authorization/orchestration")
public class AuthorizationOrchestrationHandler implements RequestHandler {

    @Autowired
    private AuthorizationRightsService authorizationRightsService;

    /**
     * 授权编排
     */
    @RequestMapper("/authorize")
    @Validate
    @Authentication("BASE_USER_CENTER_AUTHORIZATION_ORCHESTRATION_AUTHORIZE")
    public boolean authorize(@NotNull Long orchestrationId, @NotNull List<Long> groupIds) throws BizException {
        authorizationRightsService.authorize(DataObject.ORCHESTRATION,orchestrationId, groupIds, DataOperation.ALL);
        return true;
    }

    @RequestMapper("/checkOne")
    @Validate
    @Authentication("BASE_USER_CENTER_AUTHORIZATION_ORCHESTRATION_CHECK_ONE")
    public boolean checkOne(@NotNull Long orchestrationId){
        return authorizationRightsService.checkPermisson(DataObject.ORCHESTRATION, orchestrationId, DataOperation.ALL);
    }

    @RequestMapper("/checkMany")
    @Validate
    @Authentication("BASE_USER_CENTER_AUTHORIZATION_ORCHESTRATION_CHECK_Many")
    public boolean checkMany(@NotEmpty List<Long> orchestrationIds){
        return authorizationRightsService.checkPermisson(DataObject.ORCHESTRATION, orchestrationIds, DataOperation.ALL);
    }

    @RequestMapper("/getAllIds")
    @Validate
    @Authentication("BASE_USER_CENTER_AUTHORIZATION_ORCHESTRATION_GET_ALL_IDS")
    public List<Long> getAllIds() throws BizException {
        return authorizationRightsService.getObjectIds(DataObject.ORCHESTRATION, DataOperation.ALL);
    }

}
