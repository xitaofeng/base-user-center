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
    public Integer batchAddAuthorizationUserRoleRelationModel(List<AuthorizationUserRoleRelationModel> authorizationUserRoleRelationModels);

    /**
     * 根据用户ID获取角色id
     * @param userId
     * @return
     */
    public List<Long> getRoleIdByUserId(@Param("userId") Long userId);

    /**
     * 根据角色id获取用户ID
     * @param roleId
     * @return
     */
    public List<Long> getUserIdByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据角色id获取用户ID
     * @param roleIds
     * @return
     */
    public List<Long> getUserIdByRoleIds(List<Long> roleIds);


    /**
     * 根据用户id 删除分组和角色关系
     * @param userId
     * @return
     */
    public Long deleteAuthorizationUserRoleRelationModelByUserId(@Param("userId") Long userId);


    /**
     * 根据用户id 删除分组和角色关系
     * @param roleId
     * @return
     */
    public Long deleteAuthorizationUserRoleRelationModelByRoleId(@Param("roleId") Long roleId);

    /**
     *
     * @param userId
     * @param code
     * @return
     */
    public Integer getCountByUserIdAndRoleCode(@Param("userId") Long userId,@Param("roleCode") String code);
}
