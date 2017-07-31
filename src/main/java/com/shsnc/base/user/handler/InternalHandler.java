package com.shsnc.base.user.handler;

import com.shsnc.api.core.RequestHandler;
import com.shsnc.api.core.annotation.RequestMapper;
import com.shsnc.api.core.validation.Validate;
import com.shsnc.base.user.bean.InternalUserInfo;
import com.shsnc.base.user.config.ServerConfig;
import com.shsnc.base.user.config.UserConstant;
import com.shsnc.base.user.mapper.UserInfoGroupRelationModelMapper;
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
import java.util.List;

/**
 * Created by houguangqiang on 2017/6/11.
 */
@Component
@RequestMapper("/user/internal")
public class InternalHandler implements RequestHandler {

    private Logger logger = LoggerFactory.getLogger(InternalHandler.class);
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private UserInfoGroupRelationModelMapper userInfoGroupRelationModelMapper;

    @RequestMapper("/authenticate")
    @Validate
    public InternalUserInfo authenticate(@NotNull String token) throws BizException {
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
                        UserInfoModel userInfo = userInfoService.getUserInfo(Long.valueOf(result[0]));
                        List<Long> groupIds = userInfoGroupRelationModelMapper.getGroupIdsByUserId(userInfo.getUserId());
                        InternalUserInfo internalUserInfo = JsonUtil.convert(userInfo, InternalUserInfo.class);
                        internalUserInfo.setGroupIds(groupIds);
                        return internalUserInfo;
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
