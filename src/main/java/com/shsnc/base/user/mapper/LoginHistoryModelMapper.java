package com.shsnc.base.user.mapper;

import com.shsnc.base.user.model.LoginHistoryModel;

public interface LoginHistoryModelMapper {
    int deleteByPrimaryKey(Long historyId);

    int insert(LoginHistoryModel record);

    int insertSelective(LoginHistoryModel record);

    LoginHistoryModel selectByPrimaryKey(Long historyId);

    int updateByPrimaryKeySelective(LoginHistoryModel record);

    int updateByPrimaryKey(LoginHistoryModel record);
}