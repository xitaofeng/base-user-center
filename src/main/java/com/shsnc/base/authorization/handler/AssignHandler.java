package com.shsnc.base.authorization.handler;

import com.shsnc.api.core.RequestHandler;
import com.shsnc.api.core.annotation.Authentication;
import com.shsnc.api.core.annotation.RequestMapper;
import com.shsnc.api.core.util.LogRecord;
import com.shsnc.api.core.util.LogWriter;
import com.shsnc.api.core.validation.Validate;
import com.shsnc.base.authorization.model.AuthorizationRoleModel;
import com.shsnc.base.authorization.service.AssignService;
import com.shsnc.base.authorization.service.AuthorizationRoleService;
import com.shsnc.base.constants.LogConstant;
import com.shsnc.base.user.model.GroupModel;
import com.shsnc.base.user.service.GroupService;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by Elena on 2017/6/8.
 */
@Component
@RequestMapper("/authorization/assign")
public class AssignHandler implements RequestHandler {

    @Autowired
    private AssignService assignService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private AuthorizationRoleService authorizationRoleService;

    @RequestMapper("/user/to/role")
    @Validate
    @Authentication("BASE_USER_CENTER_AUTHORIZATION_ASSIGN_USER_TO_ROLE")
    public boolean userAssignRole(@NotNull Long userId, List<Long> roleIdList) throws Exception {
        return assignService.userAssignRole(userId, roleIdList);
    }

    @RequestMapper("/role/to/user")
    @Validate
    @Authentication("BASE_USER_CENTER_AUTHORIZATION_ASSIGN_ROLE_TO_USER")
    public boolean roleAssignUser(@NotNull Long roleId,  List<Long> userIdList) throws Exception {
        return assignService.roleAssignUser(roleId, userIdList);
    }

    @RequestMapper("/group/to/role")
    @Validate
    @Authentication("BASE_USER_CENTER_AUTHORIZATION_ASSIGN_GROUP_TO_ROLE")
    public boolean groupAssignRole(@NotNull Long groupId, @NotEmpty List<Long> roleIdList) throws Exception {
        GroupModel group = groupService.getGroup(groupId);
        if (group != null) {
            LogRecord logRecord = new LogRecord(LogConstant.Module.USER, LogConstant.Action.AUTHORIZE);
            logRecord.setDescription(String.format("给用户组【%s】分配角色", group.getName()));
            LogWriter.writeLog(logRecord);
        }
        return assignService.groupAssignRole(groupId, roleIdList);
    }

    @RequestMapper("/role/to/group")
    @Validate
    @Authentication("BASE_USER_CENTER_AUTHORIZATION_ASSIGN_ROLE_TO_GROUP")
    public boolean roleAssignGroup(@NotNull Long roleId, @NotEmpty List<Long> groupIdList) throws Exception {
        AuthorizationRoleModel authorizationRoleModel = authorizationRoleService.getAuthorizationRoleByRoleId(roleId);
        if (authorizationRoleModel != null) {
            LogRecord logRecord = new LogRecord(LogConstant.Module.USER, LogConstant.Action.AUTHORIZE);
            logRecord.setDescription(String.format("给角色【%s】分配用户组", authorizationRoleModel.getRoleName()));
            LogWriter.writeLog(logRecord);
        }
        return assignService.roleAssignGroup(roleId,groupIdList);
    }
}
