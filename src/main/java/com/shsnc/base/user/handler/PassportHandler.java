package com.shsnc.base.user.handler;

import com.shsnc.api.core.RequestHandler;
import com.shsnc.api.core.ThreadContext;
import com.shsnc.api.core.annotation.LoginRequired;
import com.shsnc.api.core.annotation.RequestMapper;
import com.shsnc.api.core.validation.Validate;
import com.shsnc.base.authorization.service.AuthorizationRoleService;
import com.shsnc.base.bean.Condition;
import com.shsnc.base.user.bean.Certification;
import com.shsnc.base.user.bean.LoginResult;
import com.shsnc.base.user.bean.UserInfo;
import com.shsnc.base.user.config.ServerConfig;
import com.shsnc.base.user.config.UserConstant;
import com.shsnc.base.user.model.GroupModel;
import com.shsnc.base.user.model.LoginHistoryModel;
import com.shsnc.base.user.model.OrganizationModel;
import com.shsnc.base.user.model.UserInfoModel;
import com.shsnc.base.user.service.AccountService;
import com.shsnc.base.user.service.GroupService;
import com.shsnc.base.user.service.LoginHistoryService;
import com.shsnc.base.user.service.OrganizationService;
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
import java.util.List;

/**
 * Created by houguangqiang on 2017/6/9.
 */
@Component
@RequestMapper("/user/passport")
public class PassportHandler implements RequestHandler{

    private Logger logger = LoggerFactory.getLogger(PassportHandler.class);

    @Autowired
    private AccountService accountService;
    @Autowired
    private LoginHistoryService loginHistoryService;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private AuthorizationRoleService authorizationRoleService;

    @RequestMapper("/login")
    @Validate
    public LoginResult login(@NotNull String account, @NotNull String password) throws BizException {
        LoginResult loginResult = new LoginResult();
        // 登录start
        String errorMsg = null;
        String serverMsg = null;
        UserInfoModel userInfoModel = null;
        LoginHistoryModel loginHistory = new LoginHistoryModel();
        loginHistory.setAccountName(account);
        try {
            userInfoModel = accountService.getUserInfoByAccountName(account);
        } catch (BizException e) {
            errorMsg = e.getErrorMessage();
            serverMsg = errorMsg;
        }
        if (userInfoModel != null) {
            loginHistory.setUserId(userInfoModel.getUserId());
            String newPassword = SHAMaker.sha256String(password);
            if(userInfoModel.getPassword().equals(newPassword)){
                if(userInfoModel.getStatus().equals(UserConstant.USER_STATUS_DISABLED)){
                    errorMsg = "该账户已被禁用，具体请联系管理员！";
                    serverMsg = errorMsg;
                } else if(userInfoModel.getStatus().equals(UserConstant.USER_STATUS_LOCKED)){
                    errorMsg = "该账户已被锁住，如需解锁请联系管理员！";
                    serverMsg = errorMsg;
                } else {
                    String userId = userInfoModel.getUserId().toString();
                    String uuid = ServerConfig.isOnlyCheck() || ServerConfig.isDevModel() ? TokenHelper.encodeUID(userId) : TokenHelper.generateUUID();
                    String token = null;
                    try {
                        token = SimpleTokenProvider.generateToken(userId, uuid, ServerConfig.isDevModel());
                    } catch (Exception e) {
                        errorMsg = "服务器异常！生成token失败";
                        serverMsg = "用户登录生成token异常";
                        logger.error(serverMsg, e);
                    }
                    if (token != null) {
                        OrganizationModel organizationModel = organizationService.getOrganizationByUserId(userInfoModel.getUserId());
                        userInfoModel.setOrganization(organizationModel);
                        List<GroupModel> groupModels = groupService.getGroupByUserId(userInfoModel.getUserId(), new Condition());
                        userInfoModel.setGroups(groupModels);

                        UserInfo userInfo = JsonUtil.convert(userInfoModel, UserInfo.class);

                        boolean superAdmin = authorizationRoleService.isSuperAdmin(userInfo.getUserId());
                        userInfo.setSuperAdmin(superAdmin);

                        loginResult.setUserInfo(userInfo);
                        Certification certification = new Certification(token);
                        loginResult.setCertification(certification);
                        try {
                            // 会话有效期缓存
                            RedisUtil.saveString(RedisUtil.buildRedisKey(UserConstant.REDIS_LOGIN_KEY,uuid), token , ServerConfig.getSessionTime());
                            // 用户id到token的缓存
                            RedisUtil.setFieldValue(RedisUtil.buildRedisKey(UserConstant.REDIS_LOGIN_USER,userId), uuid, token, ServerConfig.getSessionTime());
                        } catch (Exception e) {
                            errorMsg = "服务器异常！";
                            serverMsg = "操作REDIS失败";
                            logger.error(serverMsg, e);
                        }
                    }
                }
            } else {
                errorMsg = "登录密码错误！";
                serverMsg = errorMsg;
            }
        }
        // 记录登录历史记录
        String ip = ThreadContext.getClientInfo().getClientIp();
        loginHistory.setLoginIp(ip);
        loginHistory.setLoginTime(System.currentTimeMillis());
        loginHistory.setLoginType(UserConstant.LOGIN);
        loginHistory.setSuccess(serverMsg == null ? UserConstant.LOGIN_SUCCESS : UserConstant.LOGIN_ERROR);
        loginHistory.setErrorMsg(serverMsg);
        loginHistoryService.addLoginHistory(loginHistory);
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
