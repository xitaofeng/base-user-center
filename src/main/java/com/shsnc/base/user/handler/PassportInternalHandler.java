package com.shsnc.base.user.handler;

import com.shsnc.api.core.RequestHandler;
import com.shsnc.api.core.ThreadContext;
import com.shsnc.api.core.annotation.RequestMapper;
import com.shsnc.api.core.util.LogRecord;
import com.shsnc.api.core.util.LogWriter;
import com.shsnc.api.core.validation.Validate;
import com.shsnc.base.constants.LogConstant;
import com.shsnc.base.user.bean.LoginResult;
import com.shsnc.base.user.bean.UserInfo;
import com.shsnc.base.user.config.UserConstant;
import com.shsnc.base.user.model.LoginHistoryModel;
import com.shsnc.base.user.service.LoginHistoryService;
import com.shsnc.base.user.service.PassportService;
import com.shsnc.base.util.JsonUtil;
import com.shsnc.base.util.config.BizException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

/**
 * Created by houguangqiang on 2017/6/9.
 */
@Component
@RequestMapper("/user/internal/passport")
public class PassportInternalHandler implements RequestHandler{

    private Logger logger = LoggerFactory.getLogger(PassportInternalHandler.class);

    @Autowired
    private  PassportService passportService;
    @Autowired
    private LoginHistoryService loginHistoryService;

    @Validate
    @RequestMapper("/login")
    public LoginResult login(@NotNull String username) throws BizException {
        LoginResult loginResult = null;
        String errorMsg = null;
        try {
            loginResult = passportService.login(username);
        } catch (BizException e) {
            errorMsg = e.getErrorMessage();
        }
        LoginHistoryModel loginHistory = new LoginHistoryModel();

        // 记录登录历史记录
        String ip = ThreadContext.getClientInfo().getClientIp();
        if (loginResult != null) {
            UserInfo userInfo = loginResult.getUserInfo();
            if (userInfo != null) {
                loginHistory.setUserId(userInfo.getUserId());
                loginHistory.setAccountName(userInfo.getUsername());
                ThreadContext.setUserInfo(JsonUtil.convert(userInfo, com.shsnc.api.core.UserInfo.class));
                LogRecord logRecord = new LogRecord(LogConstant.Module.USER, LogConstant.Action.LOGIN);
                logRecord.setDescription(String.format("用户【%s】登入",userInfo.getAlias()));
                LogWriter.writeLog(logRecord);
            }
        }
        loginHistory.setLoginIp(ip);
        loginHistory.setLoginTime(System.currentTimeMillis());
        loginHistory.setLoginType(UserConstant.LOGIN);
        loginHistory.setSuccess(errorMsg == null ? UserConstant.LOGIN_SUCCESS : UserConstant.LOGIN_ERROR);
        loginHistory.setErrorMsg(errorMsg);
        loginHistoryService.addLoginHistory(loginHistory);

        if (errorMsg != null) {
            throw new BizException(errorMsg);
        }

        return loginResult;
    }

}
