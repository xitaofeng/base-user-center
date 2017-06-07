package com.shsnc.base.user.mapper;

import com.shsnc.base.user.model.GroupModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GroupModelMapper {

    int deleteByPrimaryKey(Long groupId);

    int insert(GroupModel record);

    int insertSelective(GroupModel record);

    GroupModel selectByPrimaryKey(Long groupId);

    int updateByPrimaryKeySelective(GroupModel record);

    int updateByPrimaryKey(GroupModel record);

    GroupModel existGroup(GroupModel groupModel);

    List<Long> existByGroupIds(@Param("groupIds") List<Long> groupIds);
}