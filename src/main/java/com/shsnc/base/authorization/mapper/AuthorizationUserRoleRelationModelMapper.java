package com.shsnc.base.authorization.mapper;

import com.shsnc.base.authorization.model.AuthorizationGroupRoleRelationModel;
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
    public List<Integer> getRoleIdByUserId(@Param("userId") Integer userId);

    /**
     * 根据用户id 删除分组和角色关系
     * @param userId
     * @return
     */
    public Integer deleteAuthorizationUserRoleRelationModelByUserId(@Param("userId") Integer userId);
}
