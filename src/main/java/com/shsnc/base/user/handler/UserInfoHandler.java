package com.shsnc.base.user.handler;

import com.shsnc.api.core.RequestHandler;
import com.shsnc.api.core.annotation.Authentication;
import com.shsnc.api.core.annotation.LoginRequired;
import com.shsnc.api.core.annotation.RequestMapper;
import com.shsnc.api.core.validation.Validate;
import com.shsnc.api.core.validation.ValidationType;
import com.shsnc.base.authorization.service.AuthorizationRoleService;
import com.shsnc.base.user.bean.ExtendPropertyValue;
import com.shsnc.base.user.bean.UserInfo;
import com.shsnc.base.user.bean.UserInfoParam;
import com.shsnc.base.user.model.ExtendPropertyValueModel;
import com.shsnc.base.user.model.UserInfoCondition;
import com.shsnc.base.user.model.UserInfoModel;
import com.shsnc.base.user.service.ExtendPropertyValueService;
import com.shsnc.base.user.service.UserInfoService;
import com.shsnc.base.util.JsonUtil;
import com.shsnc.base.util.config.BizException;
import com.shsnc.base.util.sql.Pagination;
import com.shsnc.base.util.sql.QueryData;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;

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
    @Autowired
    private AuthorizationRoleService authorizationRoleService;

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
        List<UserInfoModel> users = userInfoService.findUsers(condition);
        return JsonUtil.convert(users, List.class, UserInfo.class);
    }

    @RequestMapper("/getObject")
    @Validate
    @Authentication("BASE_USER_INFO_GET_OBJECT")
    public UserInfo getObject(@NotNull Long userId) throws BizException {
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
    public List<UserInfo> getList(@NotEmpty List<Long> userIds) throws BizException {
        List<UserInfoModel> userInfoModels = userInfoService.getUserInfoListByUserIds(userIds);
        return JsonUtil.convert(userInfoModels,List.class, UserInfo.class);
    }

    @RequestMapper("/add")
    @Authentication("BASE_USER_INFO_ADD")
    @Validate(groups = ValidationType.Add.class)
    public Long add(UserInfoParam userInfo) throws BizException {
        UserInfoModel userInfoModel = JsonUtil.convert(userInfo, UserInfoModel.class);
        List<ExtendPropertyValue> extendPropertyValues = userInfo.getExtendPropertyValues();
        List<ExtendPropertyValueModel> extendPropertyValueModels = null;
        if(extendPropertyValues != null){
            extendPropertyValueModels = JsonUtil.convert(extendPropertyValues, List.class, ExtendPropertyValueModel.class);
        }
        return userInfoService.addUserInfo(userInfoModel, userInfo.getGroupIds(), extendPropertyValueModels, userInfo.getRoleIds());
    }

    @RequestMapper("/update")
    @Validate(groups = ValidationType.Update.class)
    @Authentication("BASE_USER_INFO_UPDATE")
    public boolean update(UserInfoParam userInfo) throws BizException {
        UserInfoModel userInfoModel = JsonUtil.convert(userInfo, UserInfoModel.class);
        List<ExtendPropertyValue> extendPropertyValues = userInfo.getExtendPropertyValues();
        List<ExtendPropertyValueModel> extendPropertyValueModels = null;
        if(extendPropertyValues != null){
            extendPropertyValueModels = JsonUtil.convert(extendPropertyValues, List.class, ExtendPropertyValueModel.class);
        }
        return userInfoService.updateUserInfo(userInfoModel,userInfo.getGroupIds(),extendPropertyValueModels,userInfo.getRoleIds());
    }

    @RequestMapper("/delete")
    @Validate
    @Authentication("BASE_USER_INFO_DELETE")
    public boolean delete(@NotNull Long userId) throws BizException {
        return userInfoService.deleteUserInfo(userId);
    }

    @RequestMapper("/batchDelete")
    @Validate
    @Authentication("BASE_USER_INFO_BATCH_DELETE")
    public boolean batchDelete(@NotEmpty List<Long> userIds) throws BizException {
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
    public boolean resetPassword(@NotNull Long userId, @NotNull String newPassword) throws BizException {
        return userInfoService.updatePassword(userId,newPassword);
    }

    @RequestMapper("/moveToOrganization")
    @Authentication("BASE_USER_ORGANIZATION_MOVE_TO_ORGANIZATION")
    @Validate
    public boolean moveToOrganization(@NotNull Long userId, @NotNull Long organizationId) throws BizException {
        return userInfoService.moveToOrganization(Collections.singletonList(userId), organizationId);
    }
}
