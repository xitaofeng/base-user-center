package com.shsnc.base.user.mapper;

import com.shsnc.base.user.model.LoginHistoryModel;
import com.shsnc.base.user.model.condition.LoginHistoryCondition;
import com.shsnc.base.util.sql.Pagination;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LoginHistoryModelMapper {
    int deleteByPrimaryKey(Long historyId);

    int insert(LoginHistoryModel record);

    int insertSelective(LoginHistoryModel record);

    LoginHistoryModel selectByPrimaryKey(Long historyId);

    int updateByPrimaryKeySelective(LoginHistoryModel record);

    int updateByPrimaryKey(LoginHistoryModel record);
    
    int getTotalCountByCondition(@Param("condition")LoginHistoryCondition condition);
    
    List<LoginHistoryModel> getPageByCondition(@Param("condition")LoginHistoryCondition condition, @Param("pagination")Pagination pagination);
}