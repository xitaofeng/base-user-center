package com.shsnc.base.authorization.handler;

import com.shsnc.api.core.RequestHandler;
import com.shsnc.api.core.annotation.Authentication;
import com.shsnc.api.core.annotation.RequestMapper;
import com.shsnc.api.core.util.LogRecord;
import com.shsnc.api.core.util.LogWriter;
import com.shsnc.api.core.validation.Validate;
import com.shsnc.api.core.validation.ValidationType;
import com.shsnc.base.authorization.bean.AuthorizationRole;
import com.shsnc.base.authorization.model.AuthorizationRoleModel;
import com.shsnc.base.authorization.model.condition.AuthorizationRoleCondition;
import com.shsnc.base.authorization.service.AuthorizationRoleService;
import com.shsnc.base.constants.LogConstant;
import com.shsnc.base.util.sql.Pagination;
import com.shsnc.base.util.sql.QueryData;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elena on 2017/6/7.
 */
@Component
@RequestMapper("/authorization/role")
public class AuthorizationRoleHandler implements RequestHandler {

    private String[][] filedMapping = {{"roleName", "role_name"}, {"isBuilt", "is_built"}, {"orders", "orders"}, {"description", "description"}, {"createTime", "create_time"}};

    @Autowired
    private AuthorizationRoleService authorizationRoleService;

    @RequestMapper("/add")
    @Validate(groups = ValidationType.Add.class)
    @Authentication("BASE_USER_CENTER_AUTHORIZATION_ROLE_ADD")
    public Long addAuthorizationRoleModel(AuthorizationRole authorizationRole) throws Exception {
        AuthorizationRoleModel authorizationRoleModel = new AuthorizationRoleModel();
        BeanUtils.copyProperties(authorizationRole, authorizationRoleModel);

        LogRecord logRecord = new LogRecord(LogConstant.Module.USER, LogConstant.Action.ADD);
        logRecord.setDescription(String.format("新增角色【%s】", authorizationRole.getRoleName()));
        LogWriter.writeLog(logRecord);

        return authorizationRoleService.addAuthorizationRoleModel(authorizationRoleModel);
    }

    @RequestMapper("/edit")
    @Validate(groups = ValidationType.Update.class)
    @Authentication("BASE_USER_CENTER_AUTHORIZATION_ROLE_UPDATE")
    public boolean editAuthorizationInfo(AuthorizationRole authorizationRole) throws Exception {
        AuthorizationRoleModel authorizationRoleModel = new AuthorizationRoleModel();
        BeanUtils.copyProperties(authorizationRole, authorizationRoleModel);

        LogRecord logRecord = new LogRecord(LogConstant.Module.USER, LogConstant.Action.UPDATE);
        logRecord.setDescription(String.format("更新角色【%s】", authorizationRole.getRoleName()));
        LogWriter.writeLog(logRecord);

        return authorizationRoleService.editAuthorizationRoleModel(authorizationRoleModel);
    }

    @RequestMapper("/delete")
    @Validate
    @Authentication("BASE_USER_CENTER_AUTHORIZATION_ROLE_DELETE")
    public boolean deleteAuthorizationRole(@NotNull Long roleId) throws Exception {
        List<Long> roleIdList = new ArrayList<>();
        roleIdList.add(roleId);

        AuthorizationRoleModel authorizationRoleModel = authorizationRoleService.getAuthorizationRoleByRoleId(roleId);
        if (authorizationRoleModel != null) {
            LogRecord logRecord = new LogRecord(LogConstant.Module.USER, LogConstant.Action.DELETE);
            logRecord.setDescription(String.format("删除角色【%s】", authorizationRoleModel.getRoleName()));
            LogWriter.writeLog(logRecord);
        }

        return authorizationRoleService.batchDeleteAuthorizationRole(roleIdList);
    }

    @RequestMapper("/batch/delete")
    @Validate
    @Authentication("BASE_USER_CENTER_AUTHORIZATION_ROLE_DELETE_BATCH")
    public boolean batchDeleteAuthorizationRole(@NotEmpty List<Long> roleIdList) throws Exception {

        List<AuthorizationRoleModel> authorizationRoleModels = authorizationRoleService.getByRoleIds(roleIdList);
        if (!authorizationRoleModels.isEmpty()) {
            LogRecord logRecord = new LogRecord(LogConstant.Module.USER, LogConstant.Action.DELETE);
            logRecord.setDescription(String.format("批量删除角色【%s】", authorizationRoleModels.stream().map(AuthorizationRoleModel::getRoleName).reduce("】【", String::concat)));
            LogWriter.writeLog(logRecord);
        }
        return authorizationRoleService.batchDeleteAuthorizationRole(roleIdList);
    }

    @RequestMapper("/list")
    @Authentication("BASE_USER_CENTER_AUTHORIZATION_ROLE_GET_LIST")
    public List<AuthorizationRoleModel> getAuthorizationRoleModelList(AuthorizationRoleCondition condition) throws Exception {
        return authorizationRoleService.getAuthorizationRoleModelList(condition);
    }

    @RequestMapper("/page/list")
    @Authentication("BASE_USER_CENTER_AUTHORIZATION_ROLE_GET_PAGE_LIST")
    public QueryData getPageList(AuthorizationRoleCondition condition, Pagination pagination) {
        pagination.buildSort(filedMapping);
        QueryData queryData = authorizationRoleService.getPageList(condition, pagination);
        return queryData.convert(AuthorizationRole.class);
    }

    @RequestMapper("/one")
    @Validate
    @Authentication("BASE_USER_CENTER_AUTHORIZATION_ROLE_GET_ONE")
    public AuthorizationRoleModel getAuthorizationRoleByRoleId(@NotNull Long roleId) throws Exception {
        return authorizationRoleService.getAuthorizationRoleByRoleId(roleId);
    }

    @RequestMapper("/is/admin")
    @Validate
    @Authentication("BASE_USER_CENTER_AUTHORIZATION_ROLE_IS_ADMIN")
    public boolean isAdmin(@NotNull Long userId) throws Exception {
        return authorizationRoleService.isAdmin(userId);
    }

    /**
     * 用户已拥有的角色
     *
     * @param userId
     * @return
     */
    @RequestMapper("/user/have/list")
    @Authentication("BASE_USER_CENTER_AUTHORIZATION_ROLE_USER_HAVE_LIST")
    public List<Long> userHaveRoleList(@NotNull Long userId) {
        return authorizationRoleService.getRoleIdsByUserId(userId);
    }

}
