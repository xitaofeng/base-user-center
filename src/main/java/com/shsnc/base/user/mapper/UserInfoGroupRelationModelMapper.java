package com.shsnc.base.user.mapper;

import com.shsnc.base.user.model.UserInfoGroupRelationModel;
import com.shsnc.base.bean.Condition;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

public interface UserInfoGroupRelationModelMapper {
    int deleteByPrimaryKey(Long relationId);

    int insert(UserInfoGroupRelationModel record);

    int insertSelective(UserInfoGroupRelationModel record);

    UserInfoGroupRelationModel selectByPrimaryKey(Long relationId);

    int updateByPrimaryKeySelective(UserInfoGroupRelationModel record);

    int updateByPrimaryKey(UserInfoGroupRelationModel record);

    int insertRelationList(@Param("userInfoGroupRelationModels") List<UserInfoGroupRelationModel> userInfoGroupRelationModels);

    List<Long> getGroupIdsByUserId(@Param("userId") Long userId, @Param("condition") Condition condition);

    List<UserInfoGroupRelationModel> getByUserId(@Param("userId") Long userId, @Param("condition") Condition condition);

    List<UserInfoGroupRelationModel> getByUserIds(@Param("userIds") List<Long> userIds, @Param("condition") Condition condition);

    int deleteByGroupId(Long groupId);

    int deleteByRelationIds(@Param("relationIds") Collection<Long> relationIds);

    int deleteByGroupIds(@Param("groupIds") List<Long> groupIds);

    List<Long> getUserIdsByGroupId(Long groupId);

    List<UserInfoGroupRelationModel> getByGroupids(@Param("groupIds") List<Long> groupIds);

    List<Long> getUserIdsByGroupIds(@Param("groupIds") List<Long> groupIds);

    List<Long> getNotDeletableUserIds(@Param("userIds") Collection<Long> userIds);

    List<Long> getDeletableUserIds(@Param("userIds") Collection<Long> userIds);

    List<Long> getUserIdsByUserIds(@Param("userIds") Collection<Long> userIds, @Param("condition") Condition condition);

    Long getUserIdByUserId(@Param("userId") Long userId, @Param("condition") Condition condition);

    List<UserInfoGroupRelationModel> getByGroupId(Long groupId);

    List<Long> getCurrentUserIds(Long userId);

    List<Long> getAllGroupIdsByUserId(@Param("userId") Long userId);

    List<UserInfoGroupRelationModel> getByUserIdsNoPermission(@Param("userIds") List<Long> userIds);
}