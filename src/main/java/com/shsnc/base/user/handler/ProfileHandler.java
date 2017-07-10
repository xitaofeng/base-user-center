package com.shsnc.base.user.handler;

import com.shsnc.api.core.RequestHandler;
import com.shsnc.api.core.ThreadContext;
import com.shsnc.api.core.annotation.LoginRequired;
import com.shsnc.api.core.annotation.RequestMapper;
import com.shsnc.base.user.bean.ExtendPropertyValue;
import com.shsnc.base.user.bean.Group;
import com.shsnc.base.user.bean.UserInfo;
import com.shsnc.base.user.bean.UserInfoParam;
import com.shsnc.base.user.model.ExtendPropertyValueModel;
import com.shsnc.base.user.model.GroupModel;
import com.shsnc.base.user.model.UserInfoModel;
import com.shsnc.base.user.service.GroupService;
import com.shsnc.base.user.service.UserInfoService;
import com.shsnc.base.util.JsonUtil;
import com.shsnc.base.util.config.BizException;
import com.shsnc.base.util.crypto.SHAMaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by houguangqiang on 2017/6/8.
 */
@Component
@RequestMapper("/user/profile")
@LoginRequired
public class ProfileHandler implements RequestHandler {

    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private GroupService groupService;

    @RequestMapper("/getInfo")
    public UserInfo getInfo() throws BizException {
        Long userId = ThreadContext.getUserInfo().getUserId();
        UserInfoModel userInfoModel = userInfoService.getUserInfo(userId);
        return JsonUtil.convert(userInfoModel,UserInfo.class);
    }

    @RequestMapper("/getGroups")
    public List<Group> getGroups() throws BizException {
        Long userId = ThreadContext.getUserInfo().getUserId();
        List<GroupModel> groupModels = groupService.getGroupsByUserId(userId);
        return JsonUtil.convert(groupModels,List.class, Group.class);
    }

    @RequestMapper("/modifyInfo")
    public boolean modifyInfo(UserInfoParam userInfo) throws BizException {
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
        return userInfoService.updateUserInfo(userInfoModel, null, extendPropertyValueModels, null);
    }

    @RequestMapper("/modifyPassword")
    public void modifyPassword(@NotNull String oldPassword, @NotNull String newPassword) throws BizException {
        Long userId = ThreadContext.getUserInfo().getUserId();
        UserInfoModel userInfoModel = userInfoService.getUserInfo(userId);
        oldPassword = SHAMaker.sha256String(oldPassword);
        if(userInfoModel.getPassword().equals(oldPassword)){
            UserInfoModel passwordModel = new UserInfoModel();
            passwordModel.setUserId(userId);
            passwordModel.setPassword(SHAMaker.sha256String(newPassword));
            userInfoService.updateUserInfo(passwordModel, null, null, null);

        } else {
            throw new BizException("原登陆密码错误！");
        }
    }

}
