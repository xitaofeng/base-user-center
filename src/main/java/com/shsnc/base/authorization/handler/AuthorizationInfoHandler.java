package com.shsnc.base.authorization.handler;

import com.shsnc.api.core.RequestHandler;
import com.shsnc.api.core.annotation.RequestMapper;
import com.shsnc.api.core.validation.Validate;
import com.shsnc.base.authorization.bean.AuthorizationInfo;
import com.shsnc.base.authorization.model.AuthorizationInfoModel;
import com.shsnc.base.authorization.service.AuthorizationInfoService;
import com.shsnc.base.user.model.UserInfoModel;
import com.shsnc.base.util.JsonUtil;
import com.shsnc.base.util.api.ApiResult;
import com.shsnc.base.util.config.BizException;
import com.shsnc.base.util.config.MessageCode;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elena on 2017/6/5.
 */
@Component
@RequestMapper("/authorization/info")
public class AuthorizationInfoHandler implements RequestHandler {

    @Autowired
    private AuthorizationInfoService authorizationInfoService;

    @RequestMapper("/add")
    @ResponseBody
    @Validate
    public Integer addAuthorizationInfo(@NotNull String authorizationName, @NotNull String authorizationCode,
                                        Integer authorizationStatus, String description) throws Exception {
        AuthorizationInfoModel authorizationInfoModel = new AuthorizationInfoModel();
        authorizationInfoModel.setAuthorizationName(authorizationName);
        authorizationInfoModel.setAuthorizationCode(authorizationCode);
        authorizationInfoModel.setAuthorizationStatus(authorizationStatus);
        authorizationInfoModel.setDescription(description);
        ApiResult<AuthorizationInfo> apiResult = new ApiResult<>();
        apiResult.setMessageCode(MessageCode.RESP_OK.getMsgCode());
        apiResult.setMessage(MessageCode.RESP_OK.getMessage());
        return authorizationInfoService.addAuthorizationInfo(authorizationInfoModel);
    }

    @RequestMapper("/edit")
    @ResponseBody
    @Validate
    public boolean editAuthorizationInfo(@NotNull Integer authorizationId, @NotNull String authorizationName, @NotNull String authorizationCode,
                                         Integer authorizationStatus, String description) throws Exception {
        AuthorizationInfoModel authorizationInfoModel = new AuthorizationInfoModel();
        authorizationInfoModel.setAuthorizationId(authorizationId);
        authorizationInfoModel.setAuthorizationName(authorizationName);
        authorizationInfoModel.setAuthorizationCode(authorizationCode);
        authorizationInfoModel.setAuthorizationStatus(authorizationStatus);
        authorizationInfoModel.setDescription(description);
        ApiResult<AuthorizationInfo> apiResult = new ApiResult<>();
        boolean result = authorizationInfoService.editAuthorizationInfo(authorizationInfoModel);
        return result;
    }

    @RequestMapper("/enabled")
    @ResponseBody
    @Validate
    public boolean enabledAuthorizationInfo(@NotNull Integer authorizationId) throws Exception {
        boolean result = authorizationInfoService.changeAuthorizationInfoModelStatus(authorizationId, AuthorizationInfoModel.EnumAuthorizationStatus.ENABLED);
        return result;
    }

    @RequestMapper("/disabled")
    @ResponseBody
    @Validate
    public boolean disabledAuthorizationInfo(@NotNull Integer authorizationId) throws Exception {
        boolean result = authorizationInfoService.changeAuthorizationInfoModelStatus(authorizationId, AuthorizationInfoModel.EnumAuthorizationStatus.DISABLED);
        return result;
    }

    @RequestMapper("/delete")
    @ResponseBody
    @Validate
    public boolean deleteAuthorizationInfo(@NotNull Integer authorizationId) throws Exception {
        List<Integer> authorizationIdList = new ArrayList<>();
        authorizationIdList.add(authorizationId);
        boolean result = authorizationInfoService.batchDeleteAuthorizationInfo(authorizationIdList);
        return result;
    }

    @RequestMapper("/batch/delete")
    @ResponseBody
    @Validate
    public boolean batchDeleteAuthorizationInfo(@NotEmpty List<Integer> authorizationIdList) throws Exception {
        boolean result = authorizationInfoService.batchDeleteAuthorizationInfo(authorizationIdList);
        return result;
    }

    @RequestMapper("/list")
    @ResponseBody
    @Validate
    public List<AuthorizationInfoModel> getAuthorizationList(AuthorizationInfo authorizationInfo) throws Exception {
        AuthorizationInfoModel authorizationInfoModel = new AuthorizationInfoModel();
        BeanUtils.copyProperties(authorizationInfo, authorizationInfoModel);
        return authorizationInfoService.getAuthorizationList(authorizationInfoModel);
    }

    @RequestMapper("/object")
    @ResponseBody
    @Validate
    public AuthorizationInfoModel getAuthorizationList(@NotNull Integer authorizationId) throws Exception {
        return authorizationInfoService.getAuthorizationByAuthorizationId(authorizationId);
    }
}
