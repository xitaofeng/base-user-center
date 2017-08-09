package com.shsnc.base.authorization.mapper;

import com.shsnc.base.authorization.config.DataAuthorization;
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

    int deleteByObjectId(@Param("objectId") Long objectId, @Param("dataAuthorization") DataAuthorization dataAuthorization);

    int deleteByObjectIds(@Param("objectIds") List<Long> objectIds, @Param("dataAuthorization") DataAuthorization dataAuthorization);

    List<AuthorizationRightsModel> getByGroupIds(@Param("groupIds") List<Long> groupIds, @Param("dataAuthorization") DataAuthorization dataAuthorization);

    List<Long> getObjectIdsByGroupIds(@Param("groupIds") List<Long> groupIds, @Param("dataAuthorization") DataAuthorization dataAuthorization);

    int getPermissionByObjectId(@Param("objectId") Long objectId, @Param("dataAuthorization") DataAuthorization dataAuthorization);

    int clearByGroupId(Long groupId);

    int clearByGroupIds(@Param("groupIds") List<Long> groupIds);

    List<AuthorizationRightsModel> getByObjectId(@Param("objectId") Long objectId, @Param("dataAuthorization") DataAuthorization dataAuthorization);

    List<AuthorizationRightsModel> getByObjectIds(@Param("objectIds") List<Long> objectIds, @Param("dataAuthorization") DataAuthorization dataAuthorization);

    int deleteByGroupId(@Param("groupId") Long groupId, @Param("dataAuthorization") DataAuthorization dataAuthorization);
}