package com.shsnc.base.user.handler;

import com.shsnc.api.core.RequestHandler;
import com.shsnc.api.core.annotation.LoginRequired;
import com.shsnc.api.core.annotation.RequestMapper;
import com.shsnc.base.user.bean.UserInfo;
import com.shsnc.base.user.model.LoginHistoryModel;
import com.shsnc.base.user.service.LoginHistoryService;
import com.shsnc.base.util.sql.Pagination;
import com.shsnc.base.util.sql.QueryData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by houguangqiang on 2017/6/11.
 */
@Component
@RequestMapper("/user/loginHistory")
@LoginRequired
public class LoginHistoryHandler implements RequestHandler {
    
    @Autowired
    private LoginHistoryService loginHistoryService;
    
    @RequestMapper("/getPage")
    public QueryData getPage(LoginHistoryModel condition, Pagination pagination){
       
        QueryData queryData = loginHistoryService.getLoginHistoryPage(condition, pagination);
        
        return queryData;
    }

}
