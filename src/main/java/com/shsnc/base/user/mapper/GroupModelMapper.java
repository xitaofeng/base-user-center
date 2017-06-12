package com.shsnc.base.user.mapper;

import com.shsnc.base.user.model.ExtendPropertyModel;
import com.shsnc.base.user.model.GroupCondition;
import com.shsnc.base.user.model.GroupModel;
import com.shsnc.base.util.sql.Pagination;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

public interface GroupModelMapper {

    int deleteByPrimaryKey(Long groupId);

    int insert(GroupModel record);

    int insertSelective(GroupModel record);

    GroupModel selectByPrimaryKey(Long groupId);

    int updateByPrimaryKeySelective(GroupModel record);

    int updateByPrimaryKey(GroupModel record);

    GroupModel selectOneGroup(GroupModel groupModel);

    List<Long> getGroupIdsByGroupIds(@Param("groupIds") Collection<Long> groupIds);

    int deleteGroupAndChildren(Long groupId);

    List<GroupModel> getGroupsByUserId(Long userId);

    int getTotalCountByCondition(@Param("condition") GroupCondition condition);

    List<ExtendPropertyModel> getPageByCondition(@Param("condition") GroupCondition condition, @Param("pagination") Pagination pagination);

    List<GroupModel> getNodeList(Long parentId);
}