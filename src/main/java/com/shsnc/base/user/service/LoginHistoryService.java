package com.shsnc.base.user.service;

/**
 * Created by houguangqiang on 2017/7/4.
 */

import java.util.List;

import com.shsnc.api.core.ThreadContext;
import com.shsnc.base.user.mapper.LoginHistoryModelMapper;
import com.shsnc.base.user.model.LoginHistoryModel;
import com.shsnc.base.user.model.UserInfoModel;
import com.shsnc.base.user.model.condition.UserInfoCondition;
import com.shsnc.base.util.BizAssert;
import com.shsnc.base.util.config.BizException;
import com.shsnc.base.util.sql.Pagination;
import com.shsnc.base.util.sql.QueryData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginHistoryService {

    @Autowired
    private LoginHistoryModelMapper loginHistoryModelMapper;

    public void addLoginHistory(LoginHistoryModel loginHistoryModel) throws BizException {
        checkInput(loginHistoryModel);
        loginHistoryModelMapper.insert(loginHistoryModel);
    }
    
    public QueryData getLoginHistoryPage(LoginHistoryModel condition, Pagination pagination) {
       
        QueryData queryData = new QueryData(pagination);
        int totalCount =loginHistoryModelMapper.getTotalCountByCondition(condition);
        queryData.setRowCount(totalCount);
        List<LoginHistoryModel> list =loginHistoryModelMapper.getPageByCondition(condition, pagination);
      
        queryData.setRecords(list);
        
        return queryData;
    }

    private void checkInput(LoginHistoryModel loginHistoryModel) throws BizException {
        BizAssert.notNull(loginHistoryModel);
    }
    
    
}
