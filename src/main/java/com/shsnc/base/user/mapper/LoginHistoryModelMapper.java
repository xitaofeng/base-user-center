package com.shsnc.base.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.shsnc.base.user.model.LoginHistoryModel;
import com.shsnc.base.util.sql.Pagination;

public interface LoginHistoryModelMapper {
    int deleteByPrimaryKey(Long historyId);

    int insert(LoginHistoryModel record);

    int insertSelective(LoginHistoryModel record);

    LoginHistoryModel selectByPrimaryKey(Long historyId);

    int updateByPrimaryKeySelective(LoginHistoryModel record);

    int updateByPrimaryKey(LoginHistoryModel record);
    
    int getTotalCountByCondition(@Param("condition")LoginHistoryModel condition);
    
    List<LoginHistoryModel> getPageByCondition(@Param("condition")LoginHistoryModel condition, @Param("pagination")Pagination pagination);
}