package com.shsnc.base.authorization.mapper;

import com.shsnc.base.authorization.model.AuthorizationRoleModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Elena on 2017/6/7.
 */
public interface AuthorizationRoleModelMapper {

    /**
     * 添加权限角色信息 返回数据ID
     *
     * @param authorizationRoleModel
     * @return
     */
    public Integer addAuthorizationRoleModel(AuthorizationRoleModel authorizationRoleModel);

    /**
     * 编辑权限角色信息 返回数据编辑成功数据条数
     *
     * @param authorizationRoleModel
     * @return
     */
    public Integer editAuthorizationRoleModel(AuthorizationRoleModel authorizationRoleModel);

    /**
     * 批量删除
     *
     * @param roleIdList
     * @return
     */
    public Integer batchDeleteAuthorizationRole(List<Integer> roleIdList);

    /**
     * 获取单一对象数据
     *
     * @param roleId
     * @return
     */
    public AuthorizationRoleModel getAuthorizationRoleModelByRoleId(Integer roleId);

    /**
     * 获取列表数据
     *
     * @param authorizationRoleModel
     * @return
     */
    public List<AuthorizationRoleModel> getAuthorizationRoleModelList(AuthorizationRoleModel authorizationRoleModel);

    /**
     * 根据角色名称获取列表
     *
     * @param roleName
     * @return
     */
    public List<AuthorizationRoleModel> getListByRoleName(String roleName);

}
