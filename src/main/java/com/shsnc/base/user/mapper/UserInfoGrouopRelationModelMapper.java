package com.shsnc.base.user.mapper;

import com.shsnc.base.user.model.UserInfoGrouopRelationModel;

public interface UserInfoGrouopRelationModelMapper {
    int deleteByPrimaryKey(Long relationId);

    int insert(UserInfoGrouopRelationModel record);

    int insertSelective(UserInfoGrouopRelationModel record);

    UserInfoGrouopRelationModel selectByPrimaryKey(Long relationId);

    int updateByPrimaryKeySelective(UserInfoGrouopRelationModel record);

    int updateByPrimaryKey(UserInfoGrouopRelationModel record);
}