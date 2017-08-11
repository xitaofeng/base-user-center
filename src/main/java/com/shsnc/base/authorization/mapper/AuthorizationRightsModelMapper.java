package com.shsnc.base.authorization.mapper;

import com.shsnc.base.authorization.config.DataObject;
import com.shsnc.base.authorization.model.AuthorizationRightsModel;
import com.shsnc.base.bean.Condition;
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

    List<AuthorizationRightsModel> getRights(@Param("dataObject") DataObject dataObject, @Param("condition") Condition condition);

    List<Integer> getPermissionByObjectId(@Param("dataObject") DataObject dataObject, @Param("objectId") Long objectId, @Param("condition") Condition condition);

    List<AuthorizationRightsModel> getByObjectId(@Param("dataObject") DataObject dataObject, @Param("objectId") Long objectId, @Param("condition") Condition condition);

    List<AuthorizationRightsModel> getByObjectIds(@Param("dataObject") DataObject dataObject, @Param("objectIds") List<Long> objectIds, @Param("condition") Condition condition);

    List<AuthorizationRightsModel> getByGroupIds(@Param("dataObject") DataObject dataObject, @Param("groupIds") List<Long> groupIds);

    int deleteByGroupId(Long groupId);

    int deleteByGroupIds(@Param("groupIds") List<Long> groupIds);

    int deleteByRightIds(@Param("rightIds") List<Long> rightIds);

    int deleteByObjectIds(@Param("dataObject") DataObject dataObject, @Param("objectIds") List<Long> objectIds);

    int deleteByObjectId(@Param("dataObject") DataObject dataObject, @Param("objectId") Long objectId);

    List<Long> getNotDeletableObjectIds(@Param("dataObject") DataObject dataObject, @Param("objectIds") List<Long> objectIds);

    List<Long> getDeletableObjectIds(@Param("dataObject") DataObject dataObject, @Param("objectIds") List<Long> objectIds);

    List<AuthorizationRightsModel> getByGroupId(@Param("dataObject") DataObject dataObject, @Param("groupId") Long groupId, @Param("condition") Condition condition);

    List<Long> getNotDeletableGroupIds(List<Long> groupIds);
}