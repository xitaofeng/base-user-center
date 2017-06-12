package com.shsnc.base.user.service;

import com.shsnc.base.user.config.UserConstant;
import com.shsnc.base.user.mapper.AccountModelMapper;
import com.shsnc.base.user.mapper.UserInfoModelMapper;
import com.shsnc.base.user.model.AccountModel;
import com.shsnc.base.user.model.UserInfoModel;
import com.shsnc.base.user.support.Assert;
import com.shsnc.base.util.config.BizException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by houguangqiang on 2017/6/8.
 */
@Service
public class AccountService {

    @Autowired
    private AccountModelMapper accountModelMapper;
    @Autowired
    private UserInfoModelMapper userInfoModelMapper;

    public boolean addAccount(UserInfoModel userInfoModel) throws BizException {
        Assert.notNull(userInfoModel);
        Assert.notNull(userInfoModel.getUserId(),"用户id不能为空！");
        // 新增账户记录
        AccountModel accountModel = new AccountModel();
        accountModel.setUserId(userInfoModel.getUserId());
        String username = userInfoModel.getUsername();
        if(username != null){
            accountModel.setAccountName(username);
            accountModel.setAccountType(UserConstant.ACCOUNT_TYPE_USERNAME);
            accountModelMapper.insert(accountModel);
        }
        String mobile = userInfoModel.getMobile();
        if(mobile != null){
            accountModel.setAccountName(mobile);
            accountModel.setAccountType(UserConstant.ACCOUNT_TYPE_MOBILE);
            accountModelMapper.insert(accountModel);
        }
        String email = userInfoModel.getEmail();
        if(email != null){
            accountModel.setAccountName(email);
            accountModel.setAccountType(UserConstant.ACCOUNT_TYPE_EMAIL);
            accountModelMapper.insert(accountModel);
        }
        return true;
    }

    public boolean updateAccount(UserInfoModel userInfoModel) throws BizException {
        Assert.notNull(userInfoModel);
        Assert.notNull(userInfoModel.getUserId(),"用户id不能为空！");
        List<AccountModel> accountModelList = accountModelMapper.getAccountByUserId(userInfoModel.getUserId());
        Map<Integer,AccountModel> accountModelMap = new HashMap<>();
        for (AccountModel accountModel : accountModelList){
            accountModelMap.put(accountModel.getAccountType(),accountModel);
        }
        AccountModel accountModel = null;
        String username = userInfoModel.getUsername();
        if(username != null){
            accountModel = accountModelMap.get(UserConstant.ACCOUNT_TYPE_USERNAME);
            if(accountModel != null){
                if(!accountModel.getAccountName().equals(username)){
                    accountModel.setAccountName(username);
                    accountModelMapper.updateByPrimaryKey(accountModel);
                }
            } else {
                accountModel = new AccountModel();
                accountModel.setAccountName(username);
                accountModel.setAccountType(UserConstant.ACCOUNT_TYPE_USERNAME);
                accountModelMapper.insert(accountModel);
            }
        }
        String mobile = userInfoModel.getMobile();
        if(mobile != null){
            accountModel = accountModelMap.get(UserConstant.ACCOUNT_TYPE_MOBILE);
            if(accountModel != null){
                if(!accountModel.getAccountName().equals(mobile)){
                    accountModel.setAccountName(mobile);
                    accountModelMapper.updateByPrimaryKey(accountModel);
                }
            } else {
                accountModel.setAccountName(mobile);
                accountModel.setAccountType(UserConstant.ACCOUNT_TYPE_MOBILE);
                accountModelMapper.insert(accountModel);
            }
        }
        String email = userInfoModel.getEmail();
        if(email != null){
            accountModel = accountModelMap.get(UserConstant.ACCOUNT_TYPE_EMAIL);
            if(accountModel != null){
                if(!accountModel.getAccountName().equals(email)){
                    accountModel.setAccountName(email);
                    accountModelMapper.updateByPrimaryKey(accountModel);
                }
            } else {
                accountModel.setAccountName(email);
                accountModel.setAccountType(UserConstant.ACCOUNT_TYPE_EMAIL);
                accountModelMapper.insert(accountModel);
            }
        }
        return true;
    }

    public boolean deleteAccount(Long userId) {
        boolean result = accountModelMapper.deleteByUserId(userId) > 0;
        return result;
    }

    public UserInfoModel getUserInfoByAccountName(String account) throws BizException {
        Assert.notNull(account,"账户名不能为空！");
        Long userId = accountModelMapper.getUserIdByAccountName(account);
        Assert.notNull(userId, "账户名不存在！");

        UserInfoModel userInfoModel = userInfoModelMapper.selectByPrimaryKey(userId);
        Assert.notNull(userInfoModel, "用户不存在！");
        return userInfoModel;
    }

    public void batchDeleteAccount(List<Long> userIds) throws BizException {
        Assert.notEmpty(userIds, "用户id不能为空！");
        accountModelMapper.deleteByUserIds(userIds);
    }
}
