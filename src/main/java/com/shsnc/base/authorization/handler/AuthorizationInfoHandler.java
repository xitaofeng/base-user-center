package com.shsnc.base.authorization.handler;

import com.shsnc.api.core.RequestHandler;
import com.shsnc.api.core.annotation.RequestMapper;
import com.shsnc.api.core.validation.Validate;
import com.shsnc.api.core.validation.ValidationType;
import com.shsnc.base.authorization.bean.AuthorizationInfo;
import com.shsnc.base.authorization.model.AuthorizationInfoModel;
import com.shsnc.base.authorization.model.condition.AuthorizationInfoCondition;
import com.shsnc.base.authorization.service.AuthorizationInfoService;
import com.shsnc.base.user.bean.UserInfo;
import com.shsnc.base.user.model.UserInfoCondition;
import com.shsnc.base.util.sql.Pagination;
import com.shsnc.base.util.sql.QueryData;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elena on 2017/6/5.
 */
@Component
@RequestMapper("/authorization/info")
public class AuthorizationInfoHandler implements RequestHandler {


    private String[][] filedMapping = {{"authorizationName", "authorization_name"}, {"authorizationCode", "authorization_code"}, {"authorizationStatus", "authorization_status"}, {"description", "description"}, {"createTime", "create_time"}};


    @Autowired
    private AuthorizationInfoService authorizationInfoService;

    @RequestMapper("/add")
    @Validate(groups = ValidationType.Add.class)
    public Long addAuthorizationInfo(AuthorizationInfo authorizationInfo) throws Exception {
        AuthorizationInfoModel authorizationInfoModel = new AuthorizationInfoModel();
        BeanUtils.copyProperties(authorizationInfo, authorizationInfoModel);
        return authorizationInfoService.addAuthorizationInfo(authorizationInfoModel);
    }

    @RequestMapper("/edit")
    @Validate(groups = ValidationType.Update.class)
    public boolean editAuthorizationInfo(AuthorizationInfo authorizationInfo) throws Exception {
        AuthorizationInfoModel authorizationInfoModel = new AuthorizationInfoModel();
        BeanUtils.copyProperties(authorizationInfo, authorizationInfoModel);
        return authorizationInfoService.editAuthorizationInfo(authorizationInfoModel);
    }

    @RequestMapper("/enabled")
    @Validate
    public boolean enabledAuthorizationInfo(@NotNull Long authorizationId) throws Exception {
        return authorizationInfoService.editAuthorizationInfoModelStatus(authorizationId, AuthorizationInfoModel.EnumAuthorizationStatus.ENABLED);
    }

    @RequestMapper("/disabled")
    @Validate
    public boolean disabledAuthorizationInfo(@NotNull Long authorizationId) throws Exception {
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
    public List<AuthorizationInfo> getAuthorizationList(AuthorizationInfoCondition condition) throws Exception {

        List<AuthorizationInfoModel> list = authorizationInfoService.getAuthorizationList(condition);
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

    @RequestMapper("/page/list")
    public QueryData getPageList(AuthorizationInfoCondition condition, Pagination pagination) {
        pagination.buildSort(filedMapping);
        QueryData queryData = authorizationInfoService.getAuthorizationPageList(condition, pagination);
        return queryData.convert(AuthorizationInfo.class);
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
