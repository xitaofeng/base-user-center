package com.shsnc.base.user.service;

import com.google.common.collect.Lists;
import com.shsnc.base.user.config.UserConstant;
import com.shsnc.base.user.mapper.AccountModelMapper;
import com.shsnc.base.user.mapper.UserInfoGroupRelationModelMapper;
import com.shsnc.base.user.mapper.UserInfoModelMapper;
import com.shsnc.base.user.model.AccountModel;
import com.shsnc.base.user.model.UserInfoGroupRelationModel;
import com.shsnc.base.user.model.UserInfoModel;
import com.shsnc.base.util.DateTimeUtil;
import com.shsnc.base.util.config.BizException;
import com.shsnc.base.util.crypto.SHAMaker;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 */
@Service
public class UserInfoService {

    @Autowired
    private UserInfoModelMapper userInfoModelMapper;
    @Autowired
    private UserInfoGroupRelationModelMapper userInfoGroupRelationModelMapper;
    @Autowired
    private AccountModelMapper accountModelMapper;

    @Autowired
    private GroupService groupService;

    public List<UserInfoModel> getUserInfoList(UserInfoModel userInfoModel){
        return userInfoModelMapper.findUserInfoList(userInfoModel);
    }

    /**
     * 新增用户信息
     * @param userInfoModel 用户信息
     * @param groupIds 用户所属组，用户可以属于多个用户组
     */
    public void addUserInfo(UserInfoModel userInfoModel, List<Long> groupIds) throws BizException {
        // 数据校验以及设置默认值
        checkAdd(userInfoModel,groupIds);

        // 新增用户信息
        userInfoModelMapper.insert(userInfoModel);

        // 关联用户组
        List<UserInfoGroupRelationModel> userInfoGroupRelationModels = new ArrayList<>();
        for(Long groupId : groupIds){
            UserInfoGroupRelationModel relation = new UserInfoGroupRelationModel();
            relation.setGroupId(groupId);
            relation.setUserId(userInfoModel.getUserId());
            userInfoGroupRelationModels.add(relation);
        }
        userInfoGroupRelationModelMapper.insertRelationList(userInfoGroupRelationModels);

        // 新增账户记录
        AccountModel accountModel = new AccountModel();
        accountModel.setUserId(userInfoModel.getUserId());
        String username = userInfoModel.getUsername();
        if(StringUtils.isNotEmpty(username)){
            accountModel.setAccountName(username);
            accountModel.setAccountType(UserConstant.ACCOUNT_TYPE_USERNAME);
            accountModelMapper.insert(accountModel);
        }
        String mobile = userInfoModel.getMobile();
        if(StringUtils.isNotEmpty(mobile)){
            accountModel.setAccountName(mobile);
            accountModel.setAccountType(UserConstant.ACCOUNT_TYPE_MOBILE);
            accountModelMapper.insert(accountModel);
        }
        String email = userInfoModel.getEmail();
        if(StringUtils.isNotEmpty(email)){
            accountModel.setAccountName(email);
            accountModel.setAccountType(UserConstant.ACCOUNT_TYPE_EMAIL);
            accountModelMapper.insert(accountModel);
        }

    }

    public void updateUserInfo(UserInfoModel userInfoModel, List<Long> groupIds){

        userInfoModelMapper.updateByPrimaryKeySelective(userInfoModel);
    }

    public UserInfoModel existUserInfo(UserInfoModel userInfoModel){
        return userInfoModelMapper.existUserInfo(userInfoModel);
    }

    public void checkAdd(UserInfoModel userInfoModel, List<Long> groupIds) throws BizException {
        checkUsername(userInfoModel);
        checkPassword(userInfoModel);
        checkMobile(userInfoModel);
        checkEmail(userInfoModel);
        checkGroupIds(groupIds);
        if (userInfoModel.getStatus() == null){
            userInfoModel.setStatus(UserConstant.USER_STATUS_ENABLED);
        }
        userInfoModel.setInternal(UserConstant.USER_INTERNAL_FALSE);
        userInfoModel.setIsDelete(UserConstant.USER_DELETEED_FALSE);
        userInfoModel.setAttemptIp("");
        userInfoModel.setAttemptTime(0L);
        userInfoModel.setCreateTime(new Date().getTime());
    }

    private void checkGroupIds(List<Long> groupIds) throws BizException {
        if(CollectionUtils.isEmpty(groupIds)){
            throw new BizException("至少为用户选择一个用户组！");
        }
        boolean exits = groupService.exitsByGroupIds(groupIds);
        if(!exits){
            throw new BizException("含有不存在的用户组id！");
        }
    }

    /**
     * 校验手机号
     * @param userInfoModel 手机号
     * @throws BizException 业务异常
     */
    private void checkMobile(UserInfoModel userInfoModel) throws BizException {
        String mobile = userInfoModel.getMobile();
        if(mobile == null){
            return;
        }
        if(!mobile.matches(UserConstant.Regex.MOBILE)){
            throw new BizException("手机号码格式错误！");
        }
        UserInfoModel exist = new UserInfoModel();
        exist.setMobile(mobile);
        exist = existUserInfo(exist);
        if(exist != null && (userInfoModel.getUserId() != null && !userInfoModel.getUserId().equals(exist.getUserId()))){
            throw new BizException("该手机号已被使用！");
        }
        userInfoModel.setMobile(mobile);
    }

    /**
     * 校验邮箱
     * @param userInfoModel email属性
     * @throws BizException  业务异常
     */
    private void checkEmail(UserInfoModel userInfoModel) throws BizException {
        String email = userInfoModel.getEmail();
        if(email == null){
            return;
        }
        if(!email.matches(UserConstant.Regex.EMAIL)){
            throw new BizException("邮箱格式错误！");
        }
        UserInfoModel exist = new UserInfoModel();
        exist.setEmail(email);
        exist = existUserInfo(exist);
        if(exist != null && (userInfoModel.getUserId() != null && !userInfoModel.getUserId().equals(exist.getUserId()))){
            throw new BizException("该邮箱已经被使用！");
        }
        userInfoModel.setEmail(email);
    }

    /**
     * 校验密码并且加密
     * @param userInfoModel 密码
     * @throws BizException 业务异常
     */
    private void checkPassword(UserInfoModel userInfoModel) throws BizException {
        String password = userInfoModel.getPassword();
        if(password == null){
            throw new BizException("密码不能为空！");
        }
        if(password.length()<5){
            throw new BizException("密码长度不能小于5！");
        }
        userInfoModel.setPassword(SHAMaker.sha256String(password));
    }

    /**
     * 校验用户名
     * @param userInfoModel 用户名
     * @throws BizException 业务异常
     */
    private void checkUsername(UserInfoModel userInfoModel) throws BizException {
        String username = userInfoModel.getUsername();
        if(username == null){
            throw new BizException("用户名不能为空！");
        }
        if(!username.matches(UserConstant.Regex.USERNAME)){
            throw new BizException("用户名格式错误！");
        }
        UserInfoModel exist = new UserInfoModel();
        exist.setUsername(username);
        exist = existUserInfo(exist);
        if(exist != null && (userInfoModel.getUserId() != null && !userInfoModel.getUserId().equals(exist.getUserId()))){
            throw new BizException("用户名已存在！");
        }
        userInfoModel.setUsername(username);
    }

}
