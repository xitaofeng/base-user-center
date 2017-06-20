package com.shsnc.base.authorization.handler;

import com.shsnc.api.core.RequestHandler;
import com.shsnc.api.core.annotation.RequestMapper;
import com.shsnc.api.core.validation.Validate;
import com.shsnc.base.authorization.bean.AuthorizationRole;
import com.shsnc.base.authorization.model.AuthorizationRoleModel;
import com.shsnc.base.authorization.service.AuthorizationRoleService;
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

    @Autowired
    private AuthorizationRoleService authorizationRoleService;

    @RequestMapper("/add")
    @Validate
    public Long addAuthorizationRoleModel(@NotNull String roleName, String description, Integer orders) throws Exception {
        AuthorizationRoleModel authorizationRoleModel = new AuthorizationRoleModel();
        authorizationRoleModel.setRoleName(roleName);
        authorizationRoleModel.setDescription(description);
        authorizationRoleModel.setOrders(orders);
        return authorizationRoleService.addAuthorizationRoleModel(authorizationRoleModel);
    }

    @RequestMapper("/edit")
    @Validate
    public boolean editAuthorizationInfo(@NotNull Long roleId, @NotNull String roleName, String description) throws Exception {
        AuthorizationRoleModel authorizationRoleModel = new AuthorizationRoleModel();
        authorizationRoleModel.setRoleId(roleId);
        authorizationRoleModel.setRoleName(roleName);
        authorizationRoleModel.setDescription(description);
        return authorizationRoleService.editAuthorizationRoleModel(authorizationRoleModel);
    }

    @RequestMapper("/delete")
    @Validate
    public boolean deleteAuthorizationRole(@NotNull Long roleId) throws Exception {
        List<Long> roleIdList = new ArrayList<>();
        roleIdList.add(roleId);
        return authorizationRoleService.batchDeleteAuthorizationRole(roleIdList);
    }

    @RequestMapper("/batch/delete")
    @Validate
    public boolean batchDeleteAuthorizationRole(@NotEmpty List<Long> roleIdList) throws Exception {
        return authorizationRoleService.batchDeleteAuthorizationRole(roleIdList);
    }

    @RequestMapper("/list")
    @Validate
    public List<AuthorizationRoleModel> getAuthorizationRoleModelList(String roleName, String description) throws Exception {
        AuthorizationRole authorizationRole = new AuthorizationRole();
        authorizationRole.setRoleName(roleName);
        authorizationRole.setDescription(description);
        AuthorizationRoleModel authorizationRoleModel = new AuthorizationRoleModel();
        BeanUtils.copyProperties(authorizationRole, authorizationRoleModel);
        return authorizationRoleService.getAuthorizationRoleModelList(authorizationRoleModel);
    }

    @RequestMapper("/one")
    @Validate
    public AuthorizationRoleModel getAuthorizationRoleByRoleId(@NotNull Long roleId) throws Exception {
        return authorizationRoleService.getAuthorizationRoleByRoleId(roleId);
    }

    /**
     * 用户分配角色
     * @param userId
     * @param roleIdList
     * @return
     * @throws Exception
     */
    @RequestMapper("/user/assign")
    @Validate
    public boolean userAssignRole(@NotNull Long userId,@NotEmpty List<Long> roleIdList) throws Exception {
        return authorizationRoleService.batchDeleteAuthorizationRole(roleIdList);
    }

}
