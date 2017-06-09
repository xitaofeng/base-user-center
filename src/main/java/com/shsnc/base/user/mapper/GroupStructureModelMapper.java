package com.shsnc.base.user.mapper;

import com.shsnc.base.user.model.GroupStructureModel;
import org.apache.ibatis.annotations.Param;

public interface GroupStructureModelMapper {
    int deleteByPrimaryKey(Long structureId);

    int insert(GroupStructureModel record);

    int insertSelective(GroupStructureModel record);

    GroupStructureModel selectByPrimaryKey(Long structureId);

    int updateByPrimaryKeySelective(GroupStructureModel record);

    int updateByPrimaryKey(GroupStructureModel record);

    int insertGroupStructure(@Param("groupId") Long groupId, @Param("parentId") Long parentId);

    Long getParentIdByGroupId(Long groupId);

    int deleteOldRelation(Long groupId);

    int insertNewRelation(@Param("groupId") Long groupId, @Param("parentId") Long parentId);

    int updateChildrenLevel(Long groupId);

    int deleteGroupStructure(Long groupId);

    int deleteGroupAndChildrenRelation(Long groupId);
}