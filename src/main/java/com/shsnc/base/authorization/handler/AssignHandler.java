package com.shsnc.base.authorization.handler;

import com.shsnc.api.core.RequestHandler;
import com.shsnc.api.core.annotation.RequestMapper;
import com.shsnc.api.core.validation.Validate;
import com.shsnc.base.authorization.model.AuthorizationInfoModel;
import com.shsnc.base.authorization.service.AssignService;
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

    @RequestMapper("/user/to/role")
    @Validate
    public boolean userAssignRole(@NotNull Long userId, @NotEmpty List<Long> roleIdList) throws Exception {
        return assignService.userAssignRole(userId, roleIdList);
    }

    @RequestMapper("/role/to/user")
    @Validate
    public boolean roleAssignUser(@NotNull Long roleId, @NotEmpty List<Long> userIdList) throws Exception {
        return assignService.roleAssignUser(roleId, userIdList);
    }

    @RequestMapper("/group/to/role")
    @Validate
    public boolean groupAssignRole(@NotNull Long groupId, @NotEmpty List<Long> roleIdList) throws Exception {
        return assignService.groupAssignRole(groupId, roleIdList);
    }

    @RequestMapper("/role/to/group")
    @Validate
    public boolean roleAssignGroup(@NotNull Long roleId, @NotEmpty List<Long> groupIdList) throws Exception {
        return assignService.roleAssignGroup(roleId,groupIdList);
    }
}
