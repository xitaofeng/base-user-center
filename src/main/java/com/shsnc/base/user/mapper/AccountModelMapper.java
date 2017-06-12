package com.shsnc.base.user.mapper;

import com.shsnc.base.user.model.AccountModel;

import java.util.List;

public interface AccountModelMapper {
    int deleteByPrimaryKey(Long accountId);

    int insert(AccountModel record);

    int insertSelective(AccountModel record);

    AccountModel selectByPrimaryKey(Long accountId);

    int updateByPrimaryKeySelective(AccountModel record);

    int updateByPrimaryKey(AccountModel record);

    List<AccountModel> getAccountByUserId(Long userId);

    Long getUserIdByAccountName(String accountName);

    int deleteByUserId(Long userId);

    int deleteByUserIds(List<Long> userIds);
}