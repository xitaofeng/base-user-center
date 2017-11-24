package com.shsnc.base.user.handler;

import com.shsnc.api.core.RequestHandler;
import com.shsnc.api.core.ThreadContext;
import com.shsnc.api.core.annotation.LoginRequired;
import com.shsnc.api.core.annotation.RequestMapper;
import com.shsnc.api.core.util.LogRecord;
import com.shsnc.api.core.util.LogWriter;
import com.shsnc.api.core.validation.Validate;
import com.shsnc.base.constants.LogConstant;
import com.shsnc.base.user.bean.LoginResult;
import com.shsnc.base.user.bean.UserInfo;
import com.shsnc.base.user.config.UserConstant;
import com.shsnc.base.user.model.LoginHistoryModel;
import com.shsnc.base.user.model.UserInfoModel;
import com.shsnc.base.user.service.LoginHistoryService;
import com.shsnc.base.user.service.PassportService;
import com.shsnc.base.user.service.UserInfoService;
import com.shsnc.base.user.support.token.SimpleTokenProvider;
import com.shsnc.base.util.JsonUtil;
import com.shsnc.base.util.RedisUtil;
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
@RequestMapper("/user/passport")
public class PassportHandler implements RequestHandler{

    private Logger logger = LoggerFactory.getLogger(PassportHandler.class);

    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private LoginHistoryService loginHistoryService;
    @Autowired
    private PassportService passportService;

    @RequestMapper("/login")
    @Validate
    public LoginResult login(@NotNull String account, @NotNull String password) throws BizException {

        LoginResult loginResult = null;
        String errorMsg = null;
        try {
            loginResult = passportService.login(account, password);
        } catch (BizException e) {
            errorMsg = e.getErrorMessage();
        }
        LoginHistoryModel loginHistory = new LoginHistoryModel();
        loginHistory.setAccountName(account);

        // 记录登录历史记录
        String ip = ThreadContext.getClientInfo().getClientIp();
        if (loginResult != null) {
            UserInfo userInfo = loginResult.getUserInfo();
            if (userInfo != null) {
                loginHistory.setUserId(userInfo.getUserId());
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

    @RequestMapper("/logout")
    @LoginRequired
    public void logout() throws BizException {
        String token = ThreadContext.getClientInfo().getToken();
        String errorMsg = null;
        String serverMsg = null;
        String[] result = null;
        LoginHistoryModel loginHistory = new LoginHistoryModel();
        try {
            result = SimpleTokenProvider.resolveToken(token);
            loginHistory.setUserId(Long.valueOf(result[0]));
        } catch (IllegalArgumentException e) {
            errorMsg = "无效token！";
            serverMsg = errorMsg;
        }
        if (result != null) {
            UserInfoModel userInfo = userInfoService.getUserInfo(loginHistory.getUserId());
            if (userInfo != null) {
                LogRecord logRecord = new LogRecord(LogConstant.Module.USER, LogConstant.Action.LOGOUT);
                logRecord.setDescription(String.format("用户【%s】登出",userInfo.getAlias()));
                LogWriter.writeLog(logRecord);
            }
            try {
                // 移除会话有效期
                RedisUtil.remove(RedisUtil.buildRedisKey(UserConstant.REDIS_LOGIN_KEY,result[1]));
                // 移除用户id到token的缓存
                RedisUtil.delMapField(RedisUtil.buildRedisKey(UserConstant.REDIS_LOGIN_USER,result[0]),result[1]);
            } catch (Exception e) {
                errorMsg = "服务器异常！";
                serverMsg = "操作REDIS失败";
                logger.error(serverMsg, e);
            }
        }

        // 记录登出历史记录
        String ip = ThreadContext.getClientInfo().getClientIp();
        loginHistory.setAccountName(ThreadContext.getUserInfo().getUsername());
        loginHistory.setLoginIp(ip);
        loginHistory.setLoginTime(System.currentTimeMillis());
        loginHistory.setLoginType(UserConstant.LOGOUT);
        loginHistory.setSuccess(serverMsg == null ? UserConstant.LOGIN_SUCCESS : UserConstant.LOGIN_ERROR);
        loginHistory.setErrorMsg(serverMsg);
        loginHistoryService.addLoginHistory(loginHistory);

        // 提示信息
        if(errorMsg != null){
            throw new BizException(errorMsg);
        }
    }



}
