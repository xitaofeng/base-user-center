package com.shsnc.base.user.mapper;

import com.shsnc.base.user.model.GroupModel;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;
import java.util.Set;

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
}