package com.shsnc.base.user.handler;

import com.shsnc.api.core.RequestHandler;
import com.shsnc.api.core.annotation.RequestMapper;
import com.shsnc.base.user.bean.param.UserInfoParam;
import com.shsnc.base.user.bean.result.UserInfo;
import com.shsnc.base.user.mapper.UserInfoModelMapper;
import com.shsnc.base.user.model.UserInfoModel;
import com.shsnc.base.user.service.UserInfoService;
import com.shsnc.base.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 */
@Component
@RequestMapper("/user")
public class UserInfoHandler implements RequestHandler {

    @Autowired
    private UserInfoService userInfoService;

    @RequestMapper("/getList")
    public List<UserInfo> getList(UserInfoParam userInfo){
        List<UserInfoModel> list = userInfoService.getList(JsonUtil.convert(userInfo, UserInfoModel.class));
        return JsonUtil.convert(list, List.class, UserInfo.class);
    }
}
