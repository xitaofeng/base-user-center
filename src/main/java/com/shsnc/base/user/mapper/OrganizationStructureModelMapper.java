package com.shsnc.base.user.mapper;

import com.shsnc.base.user.model.OrganizationStructureModel;
import org.apache.ibatis.annotations.Param;

public interface OrganizationStructureModelMapper {
    int deleteByPrimaryKey(Long structureId);

    int insert(OrganizationStructureModel record);

    int insertSelective(OrganizationStructureModel record);

    OrganizationStructureModel selectByPrimaryKey(Long structureId);

    int updateByPrimaryKeySelective(OrganizationStructureModel record);

    int updateByPrimaryKey(OrganizationStructureModel record);

    int insertOrganizationStructure(@Param("organizationId") Long organizationId, @Param("parentId") Long parentId);

    Long getParentIdByOrganizationId(Long organizationId);

    int deleteOldRelation(Long organizationId);

    int insertNewRelation(@Param("organizationId") Long organizationId, @Param("parentId") Long parentId);

    int updateChildrenLevel(Long organizationId);

    int deleteOrganizationStructure(Long organizationId);

    int deleteOrganizationAndChildrenRelation(Long organizationId);
}