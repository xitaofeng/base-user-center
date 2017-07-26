package com.shsnc.base.user.mapper;

import com.shsnc.base.user.bean.GroupCondition;
import com.shsnc.base.user.model.GroupModel;
import com.shsnc.base.util.sql.Pagination;
import org.apache.ibatis.annotations.Param;

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
}