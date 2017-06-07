package com.shsnc.base.user.mapper;

import com.shsnc.base.user.model.UserInfoGroupRelationModel;

import java.util.List;

public interface UserInfoGroupRelationModelMapper {
    int deleteByPrimaryKey(Long relationId);

    int insert(UserInfoGroupRelationModel record);

    int insertSelective(UserInfoGroupRelationModel record);

    UserInfoGroupRelationModel selectByPrimaryKey(Long relationId);

    int updateByPrimaryKeySelective(UserInfoGroupRelationModel record);

    int updateByPrimaryKey(UserInfoGroupRelationModel record);

    int insertRelationList(List<UserInfoGroupRelationModel> userInfoGroupRelationModels);
}