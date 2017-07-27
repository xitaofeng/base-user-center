package com.shsnc.base.user.mapper;

import com.shsnc.base.user.model.ExtendPropertyModel;
import com.shsnc.base.user.model.OrganizationCondition;
import com.shsnc.base.user.model.OrganizationModel;
import com.shsnc.base.util.sql.Pagination;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

public interface OrganizationModelMapper {

    int deleteByPrimaryKey(Long organizationId);

    int insert(OrganizationModel record);

    int insertSelective(OrganizationModel record);

    OrganizationModel selectByPrimaryKey(Long organizationId);

    int updateByPrimaryKeySelective(OrganizationModel record);

    int updateByPrimaryKey(OrganizationModel record);

    OrganizationModel selectOne(OrganizationModel organizationModel);

    List<Long> getOrganizationIdsByOrganizationIds(@Param("organizationIds") Collection<Long> organizationIds);

    int deleteOrganizationAndChildren(Long organizationId);

    List<OrganizationModel> getOrganizationsByUserId(Long userId);

    int getTotalCountByCondition(@Param("condition") OrganizationCondition condition);

    List<ExtendPropertyModel> getPageByCondition(@Param("condition") OrganizationCondition condition, @Param("pagination") Pagination pagination);

    List<OrganizationModel> getOrganizationNodeList(@Param("parentId") Long parentId);

    List<Long> getAllOrganizationIdsByUserId(Long userId);

    List<OrganizationModel> getOrganizationsByUserIds(@Param("userIds") Collection<Long> userIds);

    List<OrganizationModel> getByOrganizationIdIds(@Param("organizationIds") Collection<Long> organizationIds);
}