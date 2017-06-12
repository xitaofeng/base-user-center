package com.shsnc.base.user.handler;

import com.shsnc.api.core.RequestHandler;
import com.shsnc.api.core.ThreadContext;
import com.shsnc.api.core.annotation.LoginRequired;
import com.shsnc.api.core.annotation.RequestMapper;
import com.shsnc.api.core.validation.Validate;
import com.shsnc.base.user.bean.Certification;
import com.shsnc.base.user.bean.LoginResult;
import com.shsnc.base.user.bean.UserInfo;
import com.shsnc.base.user.config.ServerConfig;
import com.shsnc.base.user.config.UserConstant;
import com.shsnc.base.user.model.UserInfoModel;
import com.shsnc.base.user.service.AccountService;
import com.shsnc.base.user.support.token.SimpleTokenProvider;
import com.shsnc.base.user.support.token.TokenHelper;
import com.shsnc.base.util.JsonUtil;
import com.shsnc.base.util.RedisUtil;
import com.shsnc.base.util.config.BizException;
import com.shsnc.base.util.crypto.SHAMaker;
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
    private AccountService accountService;

    @RequestMapper("/login")
    @Validate
    public LoginResult login(@NotNull String account, @NotNull String password) throws BizException {

        LoginResult loginResult = new LoginResult();
        // 登陆start
        String errorMsg = null;
        UserInfoModel userInfoModel = null;
        try {
            userInfoModel = accountService.getUserInfoByAccountName(account);
            password = SHAMaker.sha256String(password);
            if(userInfoModel.getPassword().equals(password)){
                if(userInfoModel.getStatus().equals(UserConstant.USER_STATUS_DISABLED)){
                    errorMsg = "该账户已被禁用，具体请联系管理员！";
                } else if(userInfoModel.getStatus().equals(UserConstant.USER_STATUS_LOCKED)){
                    errorMsg = "该账户已被锁住，如需解锁请联系管理员！";
                } else {
                    try {
                        String userId = userInfoModel.getUserId().toString();
                        String uuid = ServerConfig.getIsSingleLogin() ? TokenHelper.encodeUID(userId) : TokenHelper.generateUUID();
                        String token = SimpleTokenProvider.generateToken(userId, uuid);
                        UserInfo userInfo = JsonUtil.convert(userInfoModel, UserInfo.class);
                        loginResult.setUserInfo(userInfo);
                        Certification certification = new Certification(token);
                        loginResult.setCertification(certification);
                        try {
                            RedisUtil.setFieldValue(UserConstant.REDIS_LOGIN_KEY, uuid, token, ServerConfig.getSessionTime());
                        } catch (Exception e) {
                            errorMsg = "服务器异常！";
                            logger.error("操作REDIS失败", e);
                        }
                    } catch (Exception e) {
                        errorMsg = "服务器异常！";
                        logger.error("用户登陆生成token异常", e);
                    }
                }
            } else {
                errorMsg = "登陆密码错误！";
            }
        } catch (BizException e) {
            errorMsg = e.getErrorMessage();
        }

        // 记录登陆历史记录

        // 提示信息
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
        String[] result = null;
        try {
            result = SimpleTokenProvider.resolveToken(token);
        } catch (Exception e) {
            errorMsg = "无效token！";
        }
        try {
            RedisUtil.delMapField(UserConstant.REDIS_LOGIN_KEY, result[1]);
        } catch (Exception e) {
            errorMsg = "服务器异常！";
            logger.error("操作REDIS失败", e);
        }

        // 记录登出历史记录

        // 提示信息
        if(errorMsg != null){
            throw new BizException(errorMsg);
        }
    }



}
