package com.shsnc.base.authorization.mapper;

import com.shsnc.base.authorization.model.AuthorizationRoleRelationModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Elena on 2017/6/7.
 */
public interface AuthorizationRoleRelationModelMapper {

    /**
     * 插入单条数据 返回主键ID
     * @param authorizationRoleRelationModel
     * @return
     */
    int addAuthorizationRoleRelationModel(AuthorizationRoleRelationModel authorizationRoleRelationModel);

    /**
     * 批量插入数据返回 插入条数
     * @param authorizationRoleRelationModels
     * @return
     */
    Integer batchAddAuthorizationRoleRelationModel(List<AuthorizationRoleRelationModel> authorizationRoleRelationModels);

    /**
     * 根据角色ID 获取 角色所有权限
     * @param roleId
     * @return
     */
    List<String> getAuthorizationIdByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据权限ID 获取所有角色
     * @param authorizationId
     * @return
     */
    List<Integer> getRoleIdByAuthorizationId(@Param("authorizationId") Long authorizationId);

    /**
     * 根据角色ID 获取 角色所有权限
     * @param roleIds
     * @return
     */
    List<Long> getAuthorizationIdByRoleIds(List<Long> roleIds);

    /**
     * 根据角色ID 获取 角色所有权限
     * @param roleIds
     * @return
     */
    List<String> getAuthorizationCodeByRoleIds(List<Long> roleIds);

    /**
     * 根据角色ID 删除角色和权限关系
     * @param roleId
     * @return
     */
    Integer deleteAuthorizationRoleRelationModelByRoleId(@Param("roleId") Long roleId);

    List<Long> getUserIdsByAuthorizationCodeAndUserIds(@Param("authorizationCode") String authorizationCode, @Param("userIds") List<Long> userIds);
}
