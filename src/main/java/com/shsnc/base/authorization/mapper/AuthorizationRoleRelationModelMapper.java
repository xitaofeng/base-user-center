package com.shsnc.base.authorization.mapper;

import com.shsnc.base.authorization.model.AuthorizationRoleRelationModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * Created by Elena on 2017/6/7.
 */
public interface AuthorizationRoleRelationModelMapper {

    /**
     * 插入单条数据 返回主键ID
     * @param authorizationRoleRelationModel
     * @return
     */
    public int addAuthorizationRoleRelationModel(AuthorizationRoleRelationModel authorizationRoleRelationModel);

    /**
     * 批量插入数据返回 插入条数
     * @param authorizationRoleRelationModels
     * @return
     */
    public Integer batchAddAuthorizationRoleRelationModel(List<AuthorizationRoleRelationModel> authorizationRoleRelationModels);

    /**
     * 根据角色ID 获取 角色所有权限
     * @param roleId
     * @return
     */
    public List<Integer> getAuthorizationIdByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据权限ID 获取所有角色
     * @param authorizationId
     * @return
     */
    public List<Integer> getRoleIdByAuthorizationId(@Param("authorizationId") Long authorizationId);

    /**
     * 根据角色ID 获取 角色所有权限
     * @param roleIds
     * @return
     */
    public List<Long> getAuthorizationIdByRoleIds(List<Long> roleIds);

    /**
     * 根据角色ID 获取 角色所有权限
     * @param roleIds
     * @return
     */
    public List<String> getAuthorizationCodeByRoleIds(List<Long> roleIds);

    /**
     * 根据角色ID 删除角色和权限关系
     * @param roleId
     * @return
     */
    public Integer deleteAuthorizationRoleRelationModelByRoleId(@Param("roleId") Long roleId);
}
