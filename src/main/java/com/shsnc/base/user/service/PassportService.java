package com.shsnc.base.user.service;

import com.shsnc.base.authorization.service.AuthorizationRoleService;
import com.shsnc.base.bean.Condition;
import com.shsnc.base.user.bean.Certification;
import com.shsnc.base.user.bean.LoginResult;
import com.shsnc.base.user.bean.UserInfo;
import com.shsnc.base.user.config.ServerConfig;
import com.shsnc.base.user.config.UserConstant;
import com.shsnc.base.user.model.GroupModel;
import com.shsnc.base.user.model.OrganizationModel;
import com.shsnc.base.user.model.UserInfoModel;
import com.shsnc.base.user.support.token.SimpleTokenProvider;
import com.shsnc.base.user.support.token.TokenHelper;
import com.shsnc.base.util.JsonUtil;
import com.shsnc.base.util.RedisUtil;
import com.shsnc.base.util.config.BizException;
import com.shsnc.base.util.crypto.RSAUtil;
import com.shsnc.base.util.crypto.SHAMaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.util.List;

/**
 * @author houguangqiang
 * @date 2017-11-24
 * @since 1.0
 */
@Service
public class PassportService {

    private static final Logger logger = LoggerFactory.getLogger(PassportService.class);

    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private AuthorizationRoleService authorizationRoleService;

    public LoginResult login(String username) throws BizException {
        UserInfoModel userInfo = accountService.getUserInfoByAccountName(username);
        return getLoginResult(userInfo);
    }

    public LoginResult login(String account, String password) throws BizException {
        try {
            password = RSAUtil.decrypt(password);
        } catch (BadPaddingException e) {
            throw new BizException("密码格式错误！");
        } catch (IllegalBlockSizeException e) {
            throw new BizException("密码长度过长！");
        }
        LoginResult loginResult = null;
        // 登录start
        String errorMsg = null;
        UserInfoModel userInfoModel = null;
        try {
            userInfoModel = accountService.getUserInfoByAccountName(account);
        } catch (BizException e) {
            errorMsg = e.getErrorMessage();
        }
        if (userInfoModel != null) {
            String newPassword = SHAMaker.sha256String(password);
            if(userInfoModel.getPassword().equals(newPassword)){
                if(userInfoModel.getStatus().equals(UserConstant.USER_STATUS_DISABLED)){
                    errorMsg = "该账户已被禁用，具体请联系管理员！";
                } else if(userInfoModel.getStatus().equals(UserConstant.USER_STATUS_LOCKED)){
                    errorMsg = "该账户已被锁住，如需解锁请联系管理员！";
                } else {
                    loginResult = getLoginResult(userInfoModel);
                }
            } else {
                errorMsg = "登录密码错误！";
            }
        }

        // 提示信息
        if (errorMsg != null) {
            throw new BizException(errorMsg);
        }
        return loginResult;
    }

    private LoginResult getLoginResult(UserInfoModel userInfoModel) throws BizException {
        LoginResult loginResult = new LoginResult();
        String errorMsg = null;
        String userId = userInfoModel.getUserId().toString();
        String uuid = ServerConfig.isOnlyCheck() || ServerConfig.isDevModel() ? TokenHelper.encodeUID(userId) : TokenHelper.generateUUID();
        String token = null;
        try {
            token = SimpleTokenProvider.generateToken(userId, uuid, ServerConfig.isDevModel());
        } catch (Exception e) {
            errorMsg = "服务器异常！生成token失败";
            logger.error(errorMsg, e);
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
            loginResult.setCertification(new Certification(token));
            try {
                // 会话有效期缓存
                RedisUtil.saveString(RedisUtil.buildRedisKey(UserConstant.REDIS_LOGIN_KEY,uuid), token , ServerConfig.getSessionTime());
                // 用户id到token的缓存
                RedisUtil.setFieldValue(RedisUtil.buildRedisKey(UserConstant.REDIS_LOGIN_USER,userId), uuid, token, ServerConfig.getSessionTime());
            } catch (Exception e) {
                errorMsg = "服务器异常！";
                logger.error(errorMsg, e);
            }
        }
        if (errorMsg != null) {
            throw new BizException(errorMsg);
        }
        return loginResult;
    }

}
