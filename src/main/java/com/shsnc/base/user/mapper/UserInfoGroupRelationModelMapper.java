package com.shsnc.base.user.mapper;

import com.shsnc.base.user.model.UserInfoGroupRelationModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserInfoGroupRelationModelMapper {
    int deleteByPrimaryKey(Long relationId);

    int insert(UserInfoGroupRelationModel record);

    int insertSelective(UserInfoGroupRelationModel record);

    UserInfoGroupRelationModel selectByPrimaryKey(Long relationId);

    int updateByPrimaryKeySelective(UserInfoGroupRelationModel record);

    int updateByPrimaryKey(UserInfoGroupRelationModel record);

    int insertRelationList(@Param("userInfoGroupRelationModels") List<UserInfoGroupRelationModel> userInfoGroupRelationModels);

    int deleteByUserId(Long userId);

    List<Long> getGroupIdsByUserId(Long userId);

    List<UserInfoGroupRelationModel> getByUserIds(@Param("userIds") List<Long> userIds);
}