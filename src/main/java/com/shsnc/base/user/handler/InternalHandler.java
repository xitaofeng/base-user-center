package com.shsnc.base.user.handler;

import com.shsnc.api.core.RequestHandler;
import com.shsnc.api.core.annotation.RequestMapper;
import com.shsnc.api.core.validation.Validate;
import com.shsnc.base.user.bean.UserInfo;
import com.shsnc.base.user.config.ServerConfig;
import com.shsnc.base.user.config.UserConstant;
import com.shsnc.base.user.model.UserInfoModel;
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
 * Created by houguangqiang on 2017/6/11.
 */
@Component
@RequestMapper("/user/internal")
public class InternalHandler implements RequestHandler {

    private Logger logger = LoggerFactory.getLogger(InternalHandler.class);
    @Autowired
    private UserInfoService userInfoService;

    @RequestMapper("/authenticate")
    @Validate
    public UserInfo authenticate(@NotNull String token) throws BizException {
        String errorMsg = null;
        String[] result = null;
        try {
            result = SimpleTokenProvider.resolveToken(token);
            if(result.length != 3){
                errorMsg = "无效token！";
            } else{
                try {
                    String serverToken = RedisUtil.getFieldValue(UserConstant.REDIS_LOGIN_KEY,result[1]);
                    boolean success =  serverToken != null && serverToken.equals(token);
                    if(success){
                        RedisUtil.setFieldValue(UserConstant.REDIS_LOGIN_KEY, result[1], token, ServerConfig.getSessionTime());
                        String userInfoJson = RedisUtil.getFieldValue(UserConstant.REDIS_USER_INFO_KEY, result[0]);
                        if(userInfoJson != null){
                            return JsonUtil.jsonToObject(userInfoJson, UserInfo.class);
                        }
                        UserInfoModel userInfo = userInfoService.getUserInfo(Long.valueOf(result[0]));
                        return JsonUtil.convert(userInfo, UserInfo.class);
                    }
                } catch (Exception e) {
                    errorMsg = "服务器异常！";
                    logger.error("连接Redis异常！",e);
                }
            }
        } catch (Exception e) {
            errorMsg = "无效token！";
        }

        // 提示信息
        if(errorMsg != null){
            throw new BizException(errorMsg);
        }
        return null;
    }
}
