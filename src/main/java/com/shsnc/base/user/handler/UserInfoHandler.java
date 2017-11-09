package com.shsnc.base.user.handler;

import com.shsnc.api.core.RequestHandler;
import com.shsnc.api.core.annotation.Authentication;
import com.shsnc.api.core.annotation.LoginRequired;
import com.shsnc.api.core.annotation.RequestMapper;
import com.shsnc.api.core.util.LogRecord;
import com.shsnc.api.core.util.LogWriter;
import com.shsnc.api.core.validation.Validate;
import com.shsnc.api.core.validation.ValidationType;
import com.shsnc.base.constants.LogConstant;
import com.shsnc.base.user.bean.ExtendPropertyValue;
import com.shsnc.base.user.bean.UserInfo;
import com.shsnc.base.user.bean.UserInfoParam;
import com.shsnc.base.user.model.ExtendPropertyValueModel;
import com.shsnc.base.user.model.UserInfoModel;
import com.shsnc.base.user.model.condition.UserInfoCondition;
import com.shsnc.base.user.service.ExtendPropertyValueService;
import com.shsnc.base.user.service.UserInfoService;
import com.shsnc.base.util.JsonUtil;
import com.shsnc.base.util.config.BaseException;
import com.shsnc.base.util.config.BizException;
import com.shsnc.base.util.crypto.RSAUtil;
import com.shsnc.base.util.sql.Pagination;
import com.shsnc.base.util.sql.QueryData;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 */
@Component
@RequestMapper("/user/info")
@LoginRequired
public class UserInfoHandler implements RequestHandler {

    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private ExtendPropertyValueService extendPropertyValueService;

    private String[][] filedMapping=  {{"userId","user_id"},{"username","username"},{"alias","alias"},{"mobile","mobile"},{"email","email"},{"status","status"}};

    @RequestMapper("/getPage")
    @Authentication("BASE_USER_INFO_GET_PAGE")
    public QueryData getPage(UserInfoCondition condition, Pagination pagination){
        pagination.buildSort(filedMapping);
        QueryData queryData = userInfoService.getUserInfoPage(condition, pagination);
        return queryData.convert(UserInfo.class);
    }

    @RequestMapper("/findUsers")
    @Authentication("BASE_USER_INFO_FIND_USERS")
    public List<UserInfo> findUsers(UserInfoCondition condition){
        List<UserInfoModel> users = userInfoService.findUsers(condition, true);
        return JsonUtil.convert(users, List.class, UserInfo.class);
    }

    @RequestMapper("/getObject")
    @Validate
    @Authentication("BASE_USER_INFO_GET_OBJECT")
    public UserInfo getObject(@NotNull Long userId) throws BaseException {
        UserInfoModel userInfoModel = userInfoService.getUserInfo(userId);
        if (userInfoModel != null) {
            List<UserInfoModel> userInfoModels = Collections.singletonList(userInfoModel);
            userInfoService.selectOrganization(userInfoModels);
            userInfoService.selectGroups(userInfoModels);
            userInfoService.selectRoles(userInfoModels);
            return JsonUtil.convert(userInfoModel, UserInfo.class);
        }
        return null;
    }

    @RequestMapper("/getList")
    @Validate
    @Authentication("BASE_USER_INFO_GET_LIST")
    public List<UserInfo> getList(@NotEmpty List<Long> userIds) throws BaseException {
        List<UserInfoModel> userInfoModels = userInfoService.getUserInfoListByUserIds(userIds);
        return JsonUtil.convert(userInfoModels,List.class, UserInfo.class);
    }

    @RequestMapper("/add")
    @Authentication("BASE_USER_INFO_ADD")
    @Validate(groups = ValidationType.Add.class)
    public Long add(UserInfoParam userInfo) throws BaseException {
        LogRecord logRecord = new LogRecord(LogConstant.Module.USER, LogConstant.Action.ADD);
        logRecord.setDescription(String.format("新增用户【%s】",userInfo.getAlias()));
        LogWriter.writeLog(logRecord);

        UserInfoModel userInfoModel = JsonUtil.convert(userInfo, UserInfoModel.class);
        List<ExtendPropertyValue> extendPropertyValues = userInfo.getExtendPropertyValues();
        List<ExtendPropertyValueModel> extendPropertyValueModels = null;
        if(extendPropertyValues != null){
            extendPropertyValueModels = JsonUtil.convert(extendPropertyValues, List.class, ExtendPropertyValueModel.class);
        }
        try {
            String password = RSAUtil.decrypt(userInfoModel.getPassword());
            userInfoModel.setPassword(password);
        } catch (BadPaddingException e) {
            throw new BizException("密码格式错误！");
        } catch (IllegalBlockSizeException e) {
            throw new BizException("密码长度过长！");
        }
        return userInfoService.addUserInfo(userInfoModel, extendPropertyValueModels, userInfo.getRoleIds());
    }

    @RequestMapper("/update")
    @Validate(groups = ValidationType.Update.class)
    @Authentication("BASE_USER_INFO_UPDATE")
    public boolean update(UserInfoParam userInfo) throws BaseException {
        LogRecord logRecord = new LogRecord(LogConstant.Module.USER, LogConstant.Action.UPDATE);
        logRecord.setDescription(String.format("更新用户【%s】",userInfo.getAlias()));
        LogWriter.writeLog(logRecord);

        UserInfoModel userInfoModel = JsonUtil.convert(userInfo, UserInfoModel.class);
        List<ExtendPropertyValue> extendPropertyValues = userInfo.getExtendPropertyValues();
        List<ExtendPropertyValueModel> extendPropertyValueModels = null;
        if(extendPropertyValues != null){
            extendPropertyValueModels = JsonUtil.convert(extendPropertyValues, List.class, ExtendPropertyValueModel.class);
        }

        return userInfoService.updateUserInfo(userInfoModel, extendPropertyValueModels, userInfo.getRoleIds());
    }

    @RequestMapper("/delete")
    @Validate
    @Authentication("BASE_USER_INFO_DELETE")
    public boolean delete(@NotNull Long userId) throws BaseException {
        UserInfoModel userInfo = userInfoService.getUserInfo(userId);
        if (userInfo != null) {
            LogRecord logRecord = new LogRecord(LogConstant.Module.USER, LogConstant.Action.DELETE);
            logRecord.setDescription(String.format("删除用户【%s】",userInfo.getAlias()));
            LogWriter.writeLog(logRecord);
        }
        return userInfoService.deleteUserInfo(userId);
    }

    @RequestMapper("/batchDelete")
    @Validate
    @Authentication("BASE_USER_INFO_BATCH_DELETE")
    public boolean batchDelete(@NotEmpty List<Long> userIds) throws BaseException {
        List<UserInfoModel> userInfoModels = userInfoService.getUserInfoListByUserIds(userIds);
        if (!userInfoModels.isEmpty()) {
            LogRecord logRecord = new LogRecord(LogConstant.Module.USER, LogConstant.Action.DELETE);
            logRecord.setDescription(String.format("删除用户【%s】", userInfoModels.stream().map(UserInfoModel::getAlias).collect(Collectors.joining("】【"))));
            LogWriter.writeLog(logRecord);
        }
        return userInfoService.batchDeleteUserInfo(userIds);
    }

    @RequestMapper("/getExtendPropertyValues")
    @Validate
    @Authentication("BASE_USER_INFO_GET_EXTEND_PROPERTY_VALUES")
    public List<ExtendPropertyValue> getExtendPropertyValues(@NotNull Long userId){
        List<ExtendPropertyValueModel> extendPropertyValueModelList = extendPropertyValueService.getExtendPropertyValueByUserId(userId);
        return JsonUtil.convert(extendPropertyValueModelList, List.class, ExtendPropertyValue.class);
    }

    @RequestMapper("/resetPassword")
    @Validate
    @Authentication("BASE_USER_INFO_RESET_PASSWORD")
    public boolean resetPassword(@NotNull Long userId, @NotNull String newPassword) throws BaseException {
        UserInfoModel userInfo = userInfoService.getUserInfo(userId);
        if (userInfo != null) {
            LogRecord logRecord = new LogRecord(LogConstant.Module.USER, LogConstant.Action.UPDATE);
            logRecord.setDescription(String.format("更新用户【%s】密码",userInfo.getAlias()));
            LogWriter.writeLog(logRecord);
        }
        try {
            String password = RSAUtil.decrypt(newPassword);
            return userInfoService.updatePassword(userId, password);
        } catch (BadPaddingException e) {
            throw new BizException("密码格式错误！");
        } catch (IllegalBlockSizeException e) {
            throw new BizException("密码长度过长！");
        }
    }

    @RequestMapper("/moveToOrganization")
    @Authentication("BASE_USER_ORGANIZATION_MOVE_TO_ORGANIZATION")
    @Validate
    public boolean moveToOrganization(@NotNull Long userId, @NotNull Long organizationId) throws BaseException {
        UserInfoModel userInfo = userInfoService.getUserInfo(userId);
        if (userInfo != null) {
            LogRecord logRecord = new LogRecord(LogConstant.Module.USER, LogConstant.Action.UPDATE);
            logRecord.setDescription(String.format("更新用户【%s】跟组织结构关系",userInfo.getAlias()));
            LogWriter.writeLog(logRecord);
        }
        return userInfoService.moveToOrganization(Collections.singletonList(userId), organizationId);
    }
}
