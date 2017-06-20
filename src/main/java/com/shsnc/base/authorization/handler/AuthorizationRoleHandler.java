package com.shsnc.base.authorization.handler;

import com.shsnc.api.core.RequestHandler;
import com.shsnc.api.core.annotation.RequestMapper;
import com.shsnc.api.core.validation.Validate;
import com.shsnc.api.core.validation.ValidationType;
import com.shsnc.base.authorization.bean.AuthorizationRole;
import com.shsnc.base.authorization.model.AuthorizationRoleModel;
import com.shsnc.base.authorization.model.condition.AuthorizationRoleCondition;
import com.shsnc.base.authorization.service.AuthorizationRoleService;
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
    public Long addAuthorizationRoleModel(AuthorizationRole authorizationRole) throws Exception {
        AuthorizationRoleModel authorizationRoleModel = new AuthorizationRoleModel();
        BeanUtils.copyProperties(authorizationRole, authorizationRoleModel);
        return authorizationRoleService.addAuthorizationRoleModel(authorizationRoleModel);
    }

    @RequestMapper("/edit")
    @Validate(groups = ValidationType.Update.class)
    public boolean editAuthorizationInfo(AuthorizationRole authorizationRole) throws Exception {
        AuthorizationRoleModel authorizationRoleModel = new AuthorizationRoleModel();
        BeanUtils.copyProperties(authorizationRole, authorizationRoleModel);
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
    public List<AuthorizationRoleModel> getAuthorizationRoleModelList(AuthorizationRoleCondition condition) throws Exception {
        AuthorizationRoleModel authorizationRoleModel = new AuthorizationRoleModel();
        BeanUtils.copyProperties(condition, authorizationRoleModel);
        return authorizationRoleService.getAuthorizationRoleModelList(authorizationRoleModel);
    }

    @RequestMapper("/page/list")
    public QueryData getPageList(AuthorizationRoleCondition condition, Pagination pagination) {
        pagination.buildSort(filedMapping);
        QueryData queryData = authorizationRoleService.getPageList(condition, pagination);
        return queryData.convert(AuthorizationRole.class);
    }

    @RequestMapper("/one")
    @Validate
    public AuthorizationRoleModel getAuthorizationRoleByRoleId(@NotNull Long roleId) throws Exception {
        return authorizationRoleService.getAuthorizationRoleByRoleId(roleId);
    }

    /**
     * 用户分配角色
     *
     * @param userId
     * @param roleIdList
     * @return
     * @throws Exception
     */
    @RequestMapper("/user/assign")
    @Validate
    public boolean userAssignRole(@NotNull Long userId, @NotEmpty List<Long> roleIdList) throws Exception {
        return authorizationRoleService.batchDeleteAuthorizationRole(roleIdList);
    }

}
