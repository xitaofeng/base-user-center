package com.shsnc.base.authorization.mapper;

import com.shsnc.base.authorization.model.AuthorizationUserRoleRelationModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户角色关系Mapper
 * Created by Elena on 2017/6/7.
 */
public interface AuthorizationUserRoleRelationModelMapper {

    /**
     * 批量插入数据返回 插入条数
     * @param authorizationUserRoleRelationModels
     * @return
     */
    Integer batchAddAuthorizationUserRoleRelationModel(List<AuthorizationUserRoleRelationModel> authorizationUserRoleRelationModels);

    /**
     * 根据用户ID获取角色id
     * @param userId
     * @return
     */
    List<Long> getRoleIdByUserId(@Param("userId") Long userId);

    /**
     * 根据角色id获取用户ID
     * @param roleId
     * @return
     */
    List<Long> getUserIdByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据角色id获取用户ID
     * @param roleIds
     * @return
     */
    List<Long> getUserIdByRoleIds(List<Long> roleIds);


    /**
     * 根据用户id 删除分组和角色关系
     * @param userId
     * @return
     */
    Long deleteAuthorizationUserRoleRelationModelByUserId(@Param("userId") Long userId);


    /**
     * 根据用户id 删除分组和角色关系
     * @param roleId
     * @return
     */
    Long deleteAuthorizationUserRoleRelationModelByRoleId(@Param("roleId") Long roleId);

    /**
     *
     * @param userId
     * @param code
     * @return
     */
    Integer getCountByUserIdAndRoleCode(@Param("userId") Long userId,@Param("roleCode") String code);

    List<AuthorizationUserRoleRelationModel> getByRoleIds(@Param("roleIds") List<Long> roleIds);

    List<AuthorizationUserRoleRelationModel> getByUserIds(@Param("userIds") List<Long> userIds);

    List<Long> getUserIdsByRoleCodeAndUserIds(@Param("roleCode") String roleCode, @Param("userIds") List<Long> userIds);
}
