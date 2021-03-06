package com.shsnc.base.user.mapper;

import com.shsnc.base.user.model.condition.GroupCondition;
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

    List<GroupModel> selectAll();

    int getTotalCountByCondition(@Param("condition") GroupCondition condition);

    List<GroupModel> getPageByCondition(@Param("condition") GroupCondition condition, @Param("pagination") Pagination pagination);

    boolean deleteByGroupIds(@Param("groupIds") List<Long> groupIds);

    GroupModel selectOne(GroupModel group);

    List<Long> getGroupIdsByGroupIds(@Param("groupIds") List<Long> groupIds);

    List<GroupModel> getByGroupIds(@Param("groupIds") Collection<Long> groupIds);

    List<GroupModel> getListByCondition(@Param("condition") GroupCondition condition);
}