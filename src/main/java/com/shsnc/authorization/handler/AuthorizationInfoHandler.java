package com.shsnc.authorization.handler;

import com.shsnc.api.core.RequestHandler;
import com.shsnc.authorization.model.AuthorizationInfoModel;
import com.shsnc.authorization.service.AuthorizationInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by Elena on 2017/6/5.
 */
@Controller
@RequestMapping("/authorization/info")
public class AuthorizationInfoHandler implements RequestHandler {

    @Autowired
    private AuthorizationInfoService authorizationInfoService;

    @RequestMapping("/add")
    public AuthorizationInfoModel addAuthorizationInfo(
            @RequestParam(name = "authorizationName", required = true) String authorizationName,
            @RequestParam(name = "authorizationCode", required = true) String authorizationCode,
            Integer authorizationStatus, String description) throws Exception {
        AuthorizationInfoModel authorizationInfoModel = new AuthorizationInfoModel();
        authorizationInfoModel.setAuthorizationName(authorizationName);
        authorizationInfoModel.setAuthorizationCode(authorizationCode);
        authorizationInfoModel.setAuthorizationStatus(authorizationStatus);
        authorizationInfoModel.setDescription(description);
        authorizationInfoModel = authorizationInfoService.addAuthorizationInfo(authorizationInfoModel);
        return authorizationInfoModel;
    }
}
