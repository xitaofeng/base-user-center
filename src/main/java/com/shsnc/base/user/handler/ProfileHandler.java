package com.shsnc.base.user.handler;

import com.shsnc.api.core.RequestHandler;
import com.shsnc.api.core.ThreadContext;
import com.shsnc.api.core.annotation.LoginRequired;
import com.shsnc.api.core.annotation.RequestMapper;
import com.shsnc.base.user.bean.ExtendPropertyValue;
import com.shsnc.base.user.bean.UserInfo;
import com.shsnc.base.user.bean.UserInfoParam;
import com.shsnc.base.user.config.UserConstant;
import com.shsnc.base.user.model.ExtendPropertyValueModel;
import com.shsnc.base.user.model.UserInfoModel;
import com.shsnc.base.user.service.UserInfoService;
import com.shsnc.base.util.JsonUtil;
import com.shsnc.base.util.RedisUtil;
import com.shsnc.base.util.config.BaseException;
import com.shsnc.base.util.config.BizException;
import com.shsnc.base.util.crypto.SHAMaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by houguangqiang on 2017/6/8.
 */
@Component
@RequestMapper("/user/profile")
@LoginRequired
public class ProfileHandler implements RequestHandler {

    @Autowired
    private UserInfoService userInfoService;

    @RequestMapper("/getInfo")
    public UserInfo getInfo() throws BizException {
        Long userId = ThreadContext.getUserInfo().getUserId();
        UserInfoModel userInfoModel = userInfoService.getUserInfoByCache(userId);
        List<UserInfoModel> userInfoModels = Collections.singletonList(userInfoModel);
        userInfoService.selectGroups(userInfoModels);
        userInfoService.selectOrganization(userInfoModels);
        return JsonUtil.convert(userInfoModel,UserInfo.class);
    }

    @RequestMapper("/modifyInfo")
    public boolean modifyInfo(UserInfoParam userInfo) throws BaseException {
        Long userId = ThreadContext.getUserInfo().getUserId();
        UserInfoModel userInfoModel = JsonUtil.convert(userInfo, UserInfoModel.class);
        userInfoModel.setUserId(userId);
        userInfoModel.setUsername(null);
        userInfoModel.setPassword(null);
        userInfoModel.setStatus(null);
        List<ExtendPropertyValue> extendPropertyValues = userInfo.getExtendPropertyValues();
        List<ExtendPropertyValueModel> extendPropertyValueModels = null;
        if(extendPropertyValues != null){
            extendPropertyValueModels = JsonUtil.convert(extendPropertyValues, List.class, ExtendPropertyValueModel.class);
        }
        return userInfoService.updateUserInfo(userInfoModel, extendPropertyValueModels, null);
    }

    @RequestMapper("/modifyPassword")
    public void modifyPassword(@NotNull String oldPassword, @NotNull String newPassword) throws BaseException {
        Long userId = ThreadContext.getUserInfo().getUserId();
        String dbPassword = userInfoService.getPasswordByUserId(userId);
        oldPassword = SHAMaker.sha256String(oldPassword);
        if(dbPassword.equals(oldPassword)){
            userInfoService.updatePassword(userId, newPassword);
            Map<String, String> tokenMap = RedisUtil.getMap(RedisUtil.buildRedisKey(UserConstant.REDIS_LOGIN_USER, userId.toString()));
            for (Map.Entry<String, String> entry : tokenMap.entrySet()) {
                RedisUtil.remove(RedisUtil.buildRedisKey(UserConstant.REDIS_LOGIN_KEY,entry.getValue()));
            }
            RedisUtil.remove(RedisUtil.buildRedisKey(UserConstant.REDIS_LOGIN_USER, userId.toString()));
        } else {
            throw new BizException("原登录密码错误！");
        }
    }

}
