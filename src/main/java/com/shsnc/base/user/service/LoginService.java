package com.shsnc.base.user.service;

import com.shsnc.base.user.bean.UserInfo;
import com.shsnc.base.user.config.UserConstant;
import com.shsnc.base.user.mapper.AccountModelMapper;
import com.shsnc.base.user.mapper.UserInfoModelMapper;
import com.shsnc.base.user.model.UserInfoModel;
import com.shsnc.base.user.support.Assert;
import com.shsnc.base.util.RedisUtil;
import com.shsnc.base.util.config.BizException;
import com.shsnc.base.util.crypto.AESUtil;
import com.shsnc.base.util.crypto.DESUtils;
import com.shsnc.base.util.crypto.MD5Util;
import com.shsnc.base.util.crypto.SHAMaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.applet.Main;

import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * Created by houguangqiang on 2017/6/9.
 */
@Service
public class LoginService {

    private Logger logger = LoggerFactory.getLogger(LoginService.class);

    private static final String UUID_KEY = "TcrhvIAjeNOivyM57C0kCg==";
    private static final String TOKEN_KEY = "29cZ7jxSd5+JbSac39pg5A==";
    private static final String TOKEN_SEPARATOR = ":";
    private static final int default_session = 1800;

    @Autowired
    private UserInfoModelMapper userInfoModelMapper;
    @Autowired
    private AccountModelMapper accountModelMapper;

    public void login(String account, String password, String ip, boolean sso, int session) throws BizException {
        Assert.notNull(account, "账户名不能为空！");
        Assert.notNull(password, "密码不能为空！");
        Assert.notNull(ip, "登陆ip不能为空！");

        // 登陆start
        String errorMsg = null;
        Long userId = accountModelMapper.getUserIdByAccountName(account);
        if(userId == null){
            errorMsg = "账户名不存在！";
        } else {
            UserInfoModel userInfoModel = userInfoModelMapper.selectByPrimaryKey(userId);
            if(userInfoModel == null || userInfoModel.getStatus().equals(UserConstant.USER_DELETEED_FALSE)){
                errorMsg = "用户不存在！";
            } else {
                password = SHAMaker.sha256String(password);
                if(userInfoModel.getPassword().equals(password)){
                    if(userInfoModel.getStatus().equals(UserConstant.USER_STATUS_DISABLED)){
                        errorMsg = "该账户已被禁用，具体请联系管理员！";
                    } else if(userInfoModel.getStatus().equals(UserConstant.USER_STATUS_LOCKED)){
                        errorMsg = "该账户已被锁住，如需解锁请联系管理员！";
                    } else {
                        try {
                            String uid = userInfoModel.getUserId().toString();
                            String uuid = generateUUID(uid, !sso);
                            String token = generateToken(uid, uuid);
                            RedisUtil.setFieldValue(UserConstant.REDIS_LOGIN_KEY, uuid, token, session);
                        } catch (Exception e) {
                            errorMsg = "服务器异常！";
                            logger.error("用户登陆生成token异常", e);
                        }
                    }
                } else {
                    errorMsg = "登陆密码错误！";
                }
            }
        }
        // 记录登陆历史记录


    }

    private String generateUUID(String uid, boolean random) {
        if(random){
            return UUID.randomUUID().toString().replace("-","").toUpperCase();
        } else {
            return MD5Util.encodeWithoutSalt(uid);
        }
    }

    private String generateToken(String userId, String uuid) throws Exception {
        return AESUtil.encrypt(userId+TOKEN_SEPARATOR+uuid+TOKEN_SEPARATOR+generateUUID(null, true), TOKEN_KEY);
    }

    private String[] resolveToken(String token) throws Exception {
        String result = AESUtil.decrypt(token,TOKEN_KEY);
        return result.split(TOKEN_SEPARATOR);
    }

    public static void main(String[] args) throws Exception {
        LoginService loginService = new LoginService();
        String userId = "123";
        String uuid = loginService.generateUUID(userId, false);
        System.out.println(uuid);

        String token = loginService.generateToken(userId, uuid);
        System.out.println(token);

        String[] result = loginService.resolveToken(token);
        System.out.println(result[0]);
        System.out.println(result[1]);
    }
}
