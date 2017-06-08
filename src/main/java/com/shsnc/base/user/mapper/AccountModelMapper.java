package com.shsnc.base.user.mapper;

import com.shsnc.base.user.model.AccountModel;

public interface AccountModelMapper {
    int deleteByPrimaryKey(Long accountId);

    int insert(AccountModel record);

    int insertSelective(AccountModel record);

    AccountModel selectByPrimaryKey(Long accountId);

    int updateByPrimaryKeySelective(AccountModel record);

    int updateByPrimaryKey(AccountModel record);
}