package com.shsnc.base.user.handler;

import com.shsnc.api.core.RequestHandler;
import com.shsnc.api.core.annotation.RequestMapper;
import com.shsnc.api.core.validation.Validate;
import com.shsnc.base.user.bean.Certification;
import com.shsnc.base.user.bean.LoginResult;
import com.shsnc.base.user.bean.UserInfo;
import com.shsnc.base.user.config.ServerConfig;
import com.shsnc.base.user.config.UserConstant;
import com.shsnc.base.user.model.UserInfoModel;
import com.shsnc.base.user.service.AccountService;
import com.shsnc.base.util.JsonUtil;
import com.shsnc.base.util.RedisUtil;
import com.shsnc.base.util.config.BaseException;
import com.shsnc.base.util.config.BizException;
import com.shsnc.base.util.config.MessageCode;
import com.shsnc.base.util.crypto.AESUtil;
import com.shsnc.base.util.crypto.MD5Util;
import com.shsnc.base.util.crypto.SHAMaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Created by houguangqiang on 2017/6/9.
 */
@Component
@RequestMapper("/user/passport")
public class LoginHandler implements RequestHandler{

    private Logger logger = LoggerFactory.getLogger(LoginHandler.class);

    private static final String TOKEN_KEY = "29cZ7jxSd5+JbSac39pg5A==";
    private static final String TOKEN_SEPARATOR = ":";
    private static final int default_session = 1800;

    private boolean isSingleLogin = ServerConfig.getIsSingleLogin();
    private int session = ServerConfig.getSession();

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
                        String uid = userInfoModel.getUserId().toString();
                        String uuid = generateUUID(isSingleLogin, uid);
                        String token = generateToken(uid, uuid);
                        try {
                            RedisUtil.setFieldValue(UserConstant.REDIS_LOGIN_KEY, uuid, token, session);
                        } catch (Exception e) {
                            errorMsg = "服务器异常！";
                            logger.error("操作REDIS失败", e);
                        }
                        UserInfo userInfo = JsonUtil.convert(userInfoModel, UserInfo.class);
                        loginResult.setUserInfo(userInfo);
                        Certification certification = new Certification(token);
                        loginResult.setCertification(certification);
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
    @Validate
    public void logout(@NotNull String token) throws BizException {
        String errorMsg = null;
        String[] result = null;
        try {
            result = resolveToken(token);
        } catch (Exception e) {
            errorMsg = "非法token！";
        }
        if(result.length != 3){
            errorMsg = "非法token！";
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

    @RequestMapper("/authenticate")
    @Validate
    public boolean authenticate(@NotNull String token) throws BizException {
        String errorMsg = null;
        String[] result = null;
        boolean success = false;
        try {
            result = resolveToken(token);
            if(result.length != 3){
                errorMsg = "非法token！";
            } else{
                String serverToken = RedisUtil.getFieldValue(UserConstant.REDIS_LOGIN_KEY,result[1]);
                success =  serverToken != null && serverToken.equals(token);
                if(success){
                    RedisUtil.setFieldValue(UserConstant.REDIS_LOGIN_KEY, result[1], token,session);
                }
            }
        } catch (Exception e) {
            errorMsg = "非法token！";
        }

        // 提示信息
        if(errorMsg != null){
            throw new BizException(errorMsg);
        }
        return success;
    }


    private String generateUUID(boolean isSingleLogin, String uid) {
        if(isSingleLogin){
            return MD5Util.encodeWithoutSalt(uid);
        } else {
            return UUID.randomUUID().toString().replace("-","").toUpperCase();
        }
    }

    private String generateUUID(){
        return generateUUID(false, null);
    }

    private String generateToken(String userId, String uuid) throws Exception {
        return AESUtil.encrypt(userId+TOKEN_SEPARATOR+uuid+TOKEN_SEPARATOR+generateUUID(), TOKEN_KEY);
    }

    private String[] resolveToken(String token) throws Exception {
        String result = AESUtil.decrypt(token,TOKEN_KEY);
        return result.split(TOKEN_SEPARATOR);
    }
}
