package com.shsnc.base.user.handler;

import com.shsnc.api.core.RequestHandler;
import com.shsnc.api.core.annotation.RequestMapper;
import com.shsnc.api.core.validation.Validate;
import com.shsnc.api.core.validation.ValidationType;
import com.shsnc.base.user.bean.ExtendProperty;
import com.shsnc.base.user.bean.ExtendPropertyValue;
import com.shsnc.base.user.bean.UserInfo;
import com.shsnc.base.user.bean.UserInfoParam;
import com.shsnc.base.user.model.ExtendPropertyValueModel;
import com.shsnc.base.user.model.UserInfoModel;
import com.shsnc.base.user.service.UserInfoService;
import com.shsnc.base.util.JsonUtil;
import com.shsnc.base.util.config.BizException;
import com.shsnc.base.util.crypto.SHAMaker;
import com.sun.tools.corba.se.idl.constExpr.BooleanAnd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 */
@Component
@RequestMapper("/user/info")
public class UserInfoHandler implements RequestHandler {

    @Autowired
    private UserInfoService userInfoService;

    @RequestMapper("/getPage")
    public UserInfo getPage(UserInfoParam userInfo){
        return null;
    }

    @RequestMapper("/getObject")
    public UserInfo getObject(@NotNull Long userId){
        return null;
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
    public boolean delete(@NotNull Long userId) throws BizException {
        return userInfoService.deleteUserInfo(userId);
    }

}
