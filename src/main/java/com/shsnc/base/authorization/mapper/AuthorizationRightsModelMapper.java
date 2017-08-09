package com.shsnc.base.authorization.mapper;

import com.shsnc.base.authorization.config.DataObject;
import com.shsnc.base.authorization.model.AuthorizationRightsModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AuthorizationRightsModelMapper {
    int deleteByPrimaryKey(Long rightId);

    int insert(AuthorizationRightsModel record);

    int insertSelective(AuthorizationRightsModel record);

    AuthorizationRightsModel selectByPrimaryKey(Long rightId);

    int updateByPrimaryKeySelective(AuthorizationRightsModel record);

    int updateByPrimaryKey(AuthorizationRightsModel record);

    int insertList(@Param("authorizationRightsModels") List<AuthorizationRightsModel> authorizationRightsModels);

    int deleteByObjectId(@Param("objectId") Long objectId, @Param("dataObject") DataObject dataObject);

    int deleteByObjectIds(@Param("objectIds") List<Long> objectIds, @Param("dataObject") DataObject dataObject);

    List<AuthorizationRightsModel> getByGroupIds(@Param("groupIds") List<Long> groupIds, @Param("dataObject") DataObject dataObject);

    List<Long> getObjectIdsByGroupIds(@Param("groupIds") List<Long> groupIds, @Param("dataObject") DataObject dataObject);

    int getPermissionByObjectId(@Param("objectId") Long objectId, @Param("dataObject") DataObject dataObject);

    int clearByGroupId(Long groupId);

    int clearByGroupIds(@Param("groupIds") List<Long> groupIds);

    List<AuthorizationRightsModel> getByObjectId(@Param("objectId") Long objectId, @Param("dataObject") DataObject dataObject);

    List<AuthorizationRightsModel> getByObjectIds(@Param("objectIds") List<Long> objectIds, @Param("dataObject") DataObject dataObject);

    int deleteByGroupId(@Param("groupId") Long groupId, @Param("dataObject") DataObject dataObject);
}