package com.shsnc.base.user.mapper;

import com.shsnc.base.user.model.UserGroupResourceGroupRelationModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserGroupResourceGroupRelationModelMapper {
    int deleteByPrimaryKey(Long relationId);

    int insert(UserGroupResourceGroupRelationModel record);

    int insertSelective(UserGroupResourceGroupRelationModel record);

    UserGroupResourceGroupRelationModel selectByPrimaryKey(Long relationId);

    int updateByPrimaryKeySelective(UserGroupResourceGroupRelationModel record);

    int updateByPrimaryKey(UserGroupResourceGroupRelationModel record);

    int insertRelationList(@Param("userGroupResourceGroupRelationModels") List<UserGroupResourceGroupRelationModel> userGroupResourceGroupRelationModels);

    int deleteByUserGroupId(Long userGroupId);

    List<Long> getResourceGroupIdsByUserGroupId(Long userGroupId);

    List<UserGroupResourceGroupRelationModel> getByUserGroupids(@Param("userGroupIds") List<Long> userGroupIds);
}