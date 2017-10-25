package com.shsnc.base.user.handler;

import com.shsnc.api.core.RequestHandler;
import com.shsnc.api.core.annotation.LoginRequired;
import com.shsnc.api.core.annotation.RequestMapper;
import com.shsnc.api.core.validation.Validate;
import com.shsnc.base.user.bean.UserInfo;
import com.shsnc.base.user.model.UserInfoModel;
import com.shsnc.base.user.model.condition.UserInfoCondition;
import com.shsnc.base.user.service.UserInfoService;
import com.shsnc.base.util.JsonUtil;
import com.shsnc.base.util.config.BaseException;
import com.shsnc.base.util.config.BizException;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;


@Component
@RequestMapper("/user/internal/info")
public class UserInfoInternalHandler implements RequestHandler{

    @Autowired
    private UserInfoService userInfoService;

    @RequestMapper("/getObject")
    @Validate
    @LoginRequired
    public UserInfo getObject(@NotNull Long userId) throws BaseException {
        UserInfoModel userInfoModel = userInfoService.getUserInfo(userId);
        if (userInfoModel != null) {
            List<UserInfoModel> userInfoModels = Collections.singletonList(userInfoModel);
            userInfoService.selectOrganization(userInfoModels);
            userInfoService.selectGroups(userInfoModels);
            userInfoService.selectRoles(userInfoModels);
            return JsonUtil.convert(userInfoModel, UserInfo.class);
        }
        return null;
    }


    @RequestMapper("/getUserInfo")
    @Validate
    public UserInfo getUserInfo(@NotNull Long userId) throws BizException {
        UserInfoModel userInfoModel = userInfoService.getUserInfoByCache(userId);
        return JsonUtil.convert(userInfoModel, UserInfo.class);
    }


    @RequestMapper("/getList")
    @Validate
    public List<UserInfo> getList(@NotEmpty List<Long> userIds) throws BaseException {
        List<UserInfoModel> userInfoModels = userInfoService.getUserInfoListByUserIds(userIds);

        return JsonUtil.convert(userInfoModels,List.class, UserInfo.class);
    }

    @RequestMapper("/getUserIdsByCondition")
    public List<Long> getUserIdsByCondition(UserInfoCondition condition) throws BaseException {
        return userInfoService.getUserIdsByCondition(condition);
    }

    @RequestMapper("/getCurrentUserIds")
    @Validate
    @LoginRequired
    public List<Long> getCurrentUserIds(Long userId){
        return userInfoService.getCurrentUserIds(userId);
    }
}
