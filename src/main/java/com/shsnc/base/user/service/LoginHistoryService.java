package com.shsnc.base.user.service;

/**
 * Created by houguangqiang on 2017/7/4.
 */

import com.shsnc.base.user.mapper.LoginHistoryModelMapper;
import com.shsnc.base.user.model.LoginHistoryModel;
import com.shsnc.base.util.BizAssert;
import com.shsnc.base.util.config.BizException;
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

    private void checkInput(LoginHistoryModel loginHistoryModel) throws BizException {
        BizAssert.notNull(loginHistoryModel);
    }
}
