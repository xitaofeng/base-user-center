package com.shsnc.base.authorization.handler;

import com.shsnc.api.core.RequestHandler;
import com.shsnc.api.core.annotation.RequestMapper;
import com.shsnc.base.authorization.bean.AuthorizationInfo;
import com.shsnc.base.authorization.model.AuthorizationInfoModel;
import com.shsnc.base.authorization.service.AuthorizationInfoService;
import com.shsnc.base.user.model.UserInfoModel;
import com.shsnc.base.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by Elena on 2017/6/5.
 */
@Component
@RequestMapper("/authorization/info")
public class AuthorizationInfoHandler implements RequestHandler {

    @Autowired
    private AuthorizationInfoService authorizationInfoService;

    @RequestMapping("/add")
    public AuthorizationInfo addAuthorizationInfo(String authorizationName, String authorizationCode,
                                                  Integer authorizationStatus, String description) throws Exception {
        AuthorizationInfoModel authorizationInfoModel = new AuthorizationInfoModel();
        authorizationInfoModel.setAuthorizationName(authorizationName);
        authorizationInfoModel.setAuthorizationCode(authorizationCode);
        authorizationInfoModel.setAuthorizationStatus(authorizationStatus);
        authorizationInfoModel.setDescription(description);
        AuthorizationInfo authorizationInfo = JsonUtil.convert(authorizationInfoService.addAuthorizationInfo(authorizationInfoModel), AuthorizationInfo.class);
        return authorizationInfo;
    }
}
