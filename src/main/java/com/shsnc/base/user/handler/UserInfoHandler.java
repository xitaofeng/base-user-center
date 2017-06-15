package com.shsnc.base.user.handler;

import com.shsnc.api.core.RequestHandler;
import com.shsnc.api.core.annotation.LoginRequired;
import com.shsnc.api.core.annotation.RequestMapper;
import com.shsnc.api.core.validation.Validate;
import com.shsnc.api.core.validation.ValidationType;
import com.shsnc.base.user.bean.ExtendPropertyValue;
import com.shsnc.base.user.bean.UserInfo;
import com.shsnc.base.user.bean.UserInfoParam;
import com.shsnc.base.user.model.ExtendPropertyValueModel;
import com.shsnc.base.user.model.UserInfoCondition;
import com.shsnc.base.user.model.UserInfoModel;
import com.shsnc.base.user.service.ExtendPropertyValueService;
import com.shsnc.base.user.service.UserInfoService;
import com.shsnc.base.util.JsonUtil;
import com.shsnc.base.util.config.BizException;
import com.shsnc.base.util.crypto.SHAMaker;
import com.shsnc.base.util.sql.Pagination;
import com.shsnc.base.util.sql.QueryData;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 */
@Component
@RequestMapper("/user/info")
@LoginRequired
public class UserInfoHandler implements RequestHandler {

    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private ExtendPropertyValueService extendPropertyValueService;

    private String[][] filedMapping=  {{"username","username"},{"alias","alias"},{"mobile","mobile"},{"email","email"},{"status","status"}};

    @RequestMapper("/getPage")
    public QueryData getPage(UserInfoCondition condition, Pagination pagination){
        pagination.buildSort(filedMapping);
        QueryData queryData = userInfoService.getUserInfoPage(condition, pagination);
        return queryData.convert(UserInfo.class);
    }

    @RequestMapper("/getObject")
    @Validate
    public UserInfo getObject(@NotNull Long userId) throws BizException {
        UserInfoModel userInfoModel = userInfoService.getUserInfo(userId);
        return JsonUtil.convert(userInfoModel,UserInfo.class);
    }

    @RequestMapper("/getList")
    public List<UserInfo> getList(UserInfoParam userInfo){
        UserInfoModel userInfoModel = JsonUtil.convert(userInfo, UserInfoModel.class);
        List<UserInfoModel> userInfoList = userInfoService.getUserInfoList(userInfoModel);
        return JsonUtil.convert(userInfoList, List.class, UserInfo.class);
    }

    @RequestMapper("/add")
    @Validate(groups = ValidationType.Add.class)
    public Long add(UserInfoParam userInfo) throws BizException {
        userInfo.setPassword(SHAMaker.sha256String(userInfo.getPassword()));
        UserInfoModel userInfoModel = JsonUtil.convert(userInfo, UserInfoModel.class);
        List<ExtendPropertyValue> extendPropertyValues = userInfo.getExtendPropertyValues();
        List<ExtendPropertyValueModel> extendPropertyValueModels = null;
        if(extendPropertyValues != null){
            extendPropertyValueModels = JsonUtil.convert(extendPropertyValues, List.class, ExtendPropertyValueModel.class);
        }
        return userInfoService.addUserInfo(userInfoModel, userInfo.getGroupIds(), extendPropertyValueModels);
    }

    @RequestMapper("/update")
    @Validate(groups = ValidationType.Update.class)
    public boolean update(UserInfoParam userInfo) throws BizException {
        UserInfoModel userInfoModel = JsonUtil.convert(userInfo, UserInfoModel.class);
        List<ExtendPropertyValue> extendPropertyValues = userInfo.getExtendPropertyValues();
        List<ExtendPropertyValueModel> extendPropertyValueModels = null;
        if(extendPropertyValues != null){
            extendPropertyValueModels = JsonUtil.convert(extendPropertyValues, List.class, ExtendPropertyValueModel.class);
        }
        return userInfoService.updateUserInfo(userInfoModel,userInfo.getGroupIds(),extendPropertyValueModels);
    }

    @RequestMapper("/delete")
    @Validate
    public boolean delete(@NotNull Long userId) throws BizException {
        return userInfoService.deleteUserInfo(userId);
    }

    @RequestMapper("/batchDelete")
    @Validate
    public boolean batchDelete(@NotEmpty List<Long> userIds) throws BizException {
        return userInfoService.batchDeleteUserInfo(userIds);
    }

    @RequestMapper("/getExtendPropertyValues")
    @Validate
    public List<ExtendPropertyValue> getExtendPropertyValues(@NotNull Long userId){
        List<ExtendPropertyValueModel> extendPropertyValueModelList = extendPropertyValueService.getExtendPropertyValueByUserId(userId);
        return JsonUtil.convert(extendPropertyValueModelList, List.class, ExtendPropertyValue.class);
    }
}
