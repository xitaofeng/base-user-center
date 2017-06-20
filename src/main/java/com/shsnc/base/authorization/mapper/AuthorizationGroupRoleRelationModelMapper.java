package com.shsnc.base.authorization.mapper;

import com.shsnc.base.authorization.model.AuthorizationGroupRoleRelationModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户组和角色关系 Mapper
 * Created by Elena on 2017/6/7.
 */
public interface AuthorizationGroupRoleRelationModelMapper {

    /**
     * 批量插入数据返回 插入条数
     * @param authorizationGroupRoleRelationModels
     * @return
     */
    public Integer batchAddAuthorizationGroupRoleRelationModel(List<AuthorizationGroupRoleRelationModel> authorizationGroupRoleRelationModels);

    /**
     * 根据分组ID获取角色id
     * @param groupId
     * @return
     */
    public List<Long> getRoleIdByGroupId(@Param("groupId") Long groupId);

    /**
     * 根据roleId获取groupId
     * @param roleId
     * @return
     */
    public List<Long> getGroupIdByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据分组列表获取角色id
     * @param groupIds
     * @return
     */
    public List<Long> getRoleIdByGroupIds(@Param("groupIds") List<Long> groupIds);

    /**
     * 根据分组id 删除分组和角色关系
     * @param groupId
     * @return
     */
    public Integer deleteAuthorizationGroupRoleRelationModelByGroupId(@Param("groupId") Long groupId);

    /**
     * 根据角色删除 角色和组的关系
     * @param roleId
     * @return
     */
    public Integer deleteAuthorizationGroupRoleRelationModelByRoleId(@Param("roleId") Long roleId);
}
