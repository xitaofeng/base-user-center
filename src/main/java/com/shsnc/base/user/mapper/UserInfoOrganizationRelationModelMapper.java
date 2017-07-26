package com.shsnc.base.user.mapper;

import com.shsnc.base.user.model.UserInfoOrganizationRelationModel;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

public interface UserInfoOrganizationRelationModelMapper {

    int deleteByPrimaryKey(Long relationId);

    int insert(UserInfoOrganizationRelationModel record);

    int insertSelective(UserInfoOrganizationRelationModel record);

    UserInfoOrganizationRelationModel selectByPrimaryKey(Long relationId);

    int updateByPrimaryKeySelective(UserInfoOrganizationRelationModel record);

    int updateByPrimaryKey(UserInfoOrganizationRelationModel record);

    int insertRelationList(@Param("userInfoOrganizationRelationModels") List<UserInfoOrganizationRelationModel> userInfoOrganizationRelationModels);

    List<Long> getOrganizationIdsByUserId(Long userId);

    int deleteByUserId(Long userId);

    List<Long> getUserIdsByOrganizationId(Long organizationId);

    int deleteByOrganizationId(Long organizationId);

    int deleteWithChildrenByOrganizationId(Long organizationId);

    List<UserInfoOrganizationRelationModel> getByUserIds(@Param("userIds") Collection<Long> userIds);
}