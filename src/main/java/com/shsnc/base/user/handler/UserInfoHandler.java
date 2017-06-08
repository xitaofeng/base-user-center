package com.shsnc.base.user.handler;

import com.shsnc.api.core.RequestHandler;
import com.shsnc.api.core.annotation.RequestMapper;
import com.shsnc.base.user.bean.UserInfoParam;
import com.shsnc.base.user.bean.UserInfo;
import com.shsnc.base.user.service.UserInfoService;
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
        return null;
    }
}
