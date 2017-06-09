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
import org.springframework.util.CollectionUtils;
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
    @Validate
    public Long addAuthorizationInfo(@NotNull String authorizationName, @NotNull String authorizationCode,
                                     Integer authorizationStatus, String description) throws Exception {
        AuthorizationInfoModel authorizationInfoModel = new AuthorizationInfoModel();
        authorizationInfoModel.setAuthorizationName(authorizationName);
        authorizationInfoModel.setAuthorizationCode(authorizationCode);
        authorizationInfoModel.setAuthorizationStatus(authorizationStatus);
        authorizationInfoModel.setDescription(description);
        return authorizationInfoService.addAuthorizationInfo(authorizationInfoModel);
    }

    @RequestMapper("/edit")
    @Validate
    public boolean editAuthorizationInfo(@NotNull Long authorizationId, @NotNull String authorizationName, @NotNull String authorizationCode,
                                         Integer authorizationStatus, String description) throws Exception {
        AuthorizationInfoModel authorizationInfoModel = new AuthorizationInfoModel();
        authorizationInfoModel.setAuthorizationId(authorizationId);
        authorizationInfoModel.setAuthorizationName(authorizationName);
        authorizationInfoModel.setAuthorizationCode(authorizationCode);
        authorizationInfoModel.setAuthorizationStatus(authorizationStatus);
        authorizationInfoModel.setDescription(description);
        return authorizationInfoService.editAuthorizationInfo(authorizationInfoModel);
    }

    @RequestMapper("/enabled")
    @Validate
    public boolean enabledAuthorizationInfo(@NotNull Long authorizationId) throws Exception {
        return authorizationInfoService.editAuthorizationInfoModelStatus(authorizationId, AuthorizationInfoModel.EnumAuthorizationStatus.ENABLED);
    }

    @RequestMapper("/disabled")
    @Validate
    public boolean disabledAuthorizationInfo(@NotNull Long authorizationId) throws BizException {
        return authorizationInfoService.editAuthorizationInfoModelStatus(authorizationId, AuthorizationInfoModel.EnumAuthorizationStatus.DISABLED);
    }

    @RequestMapper("/delete")
    @Validate
    public boolean deleteAuthorizationInfo(@NotNull Long authorizationId) throws Exception {
        List<Long> authorizationIdList = new ArrayList<>();
        authorizationIdList.add(authorizationId);
        return authorizationInfoService.batchDeleteAuthorizationInfo(authorizationIdList);
    }

    @RequestMapper("/batch/delete")
    @Validate
    public boolean batchDeleteAuthorizationInfo(@NotEmpty List<Long> authorizationIdList) throws Exception {
        return authorizationInfoService.batchDeleteAuthorizationInfo(authorizationIdList);
    }

    @RequestMapper("/list")
    @Validate
    public List<AuthorizationInfo> getAuthorizationList(String authorizationName, String authorizationCode, Integer authorizationStatus, String description) throws Exception {
        AuthorizationInfo authorizationInfo = new AuthorizationInfo();
        authorizationInfo.setAuthorizationName(authorizationName);
        authorizationInfo.setAuthorizationCode(authorizationCode);
        authorizationInfo.setAuthorizationStatus(authorizationStatus);
        authorizationInfo.setDescription(description);

        AuthorizationInfoModel authorizationInfoModel = new AuthorizationInfoModel();
        BeanUtils.copyProperties(authorizationInfo, authorizationInfoModel);

        List<AuthorizationInfoModel> list = authorizationInfoService.getAuthorizationList(authorizationInfoModel);
        List<AuthorizationInfo> result = new ArrayList<>();
        if (!CollectionUtils.isEmpty(list)) {
            list.forEach(item -> {
                AuthorizationInfo tempAuthorizationInfo = new AuthorizationInfo();
                BeanUtils.copyProperties(item, tempAuthorizationInfo);
                result.add(tempAuthorizationInfo);
            });
        }
        return result;
    }

    @RequestMapper("/one")
    @Validate
    public AuthorizationInfo getAuthorizationByAuthorizationId(@NotNull Long authorizationId) throws Exception {
        AuthorizationInfo authorizationInfo = new AuthorizationInfo();
        AuthorizationInfoModel authorizationInfoModel = authorizationInfoService.getAuthorizationByAuthorizationId(authorizationId);
        if (authorizationInfoModel != null) {
            BeanUtils.copyProperties(authorizationInfoModel, authorizationInfo);
        }
        return authorizationInfo;
    }
}
