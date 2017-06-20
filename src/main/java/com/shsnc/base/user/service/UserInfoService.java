package com.shsnc.base.user.service;

import com.shsnc.base.user.config.UserConstant;
import com.shsnc.base.user.mapper.UserInfoModelMapper;
import com.shsnc.base.user.model.ExtendPropertyModel;
import com.shsnc.base.user.model.ExtendPropertyValueModel;
import com.shsnc.base.user.model.UserInfoCondition;
import com.shsnc.base.user.model.UserInfoModel;
import com.shsnc.base.user.support.Assert;
import com.shsnc.base.util.JsonUtil;
import com.shsnc.base.util.RedisUtil;
import com.shsnc.base.util.config.BaseException;
import com.shsnc.base.util.config.BizException;
import com.shsnc.base.util.crypto.SHAMaker;
import com.shsnc.base.util.sql.Pagination;
import com.shsnc.base.util.sql.QueryData;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 *
 */
@Service
@Transactional(rollbackFor = BaseException.class)
public class UserInfoService {

    @Autowired
    private UserInfoModelMapper userInfoModelMapper;
    @Autowired
    private UserInfoGroupRelationService userInfoGroupRelationService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private ExtendPropertyValueService extendPropertyValueService;

    @Autowired
    private GroupService groupService;

    public List<UserInfoModel> getUserInfoList(UserInfoModel userInfoModel){
        return userInfoModelMapper.findUserInfoList(userInfoModel);
    }

    public UserInfoModel getUserInfo(Long userId) throws BizException {
        Assert.notNull(userId,"用户id不能为空！");
        String userInfoJson = RedisUtil.getFieldValue(UserConstant.REDIS_USER_INFO_KEY, userId.toString());
        if(userInfoJson != null){
            return JsonUtil.jsonToObject(userInfoJson, UserInfoModel.class );
        }
        return userInfoModelMapper.selectByPrimaryKey(userId);
    }

    public QueryData getUserInfoPage(UserInfoCondition condition, Pagination pagination) {
        QueryData queryData = new QueryData(pagination);
        int totalCount = userInfoModelMapper.getTotalCountByCondition(condition);
        queryData.setRowCount(totalCount);
        List<ExtendPropertyModel> list = userInfoModelMapper.getPageByCondition(condition, pagination);
        queryData.setRecords(list);
        return queryData;
    }

    /**
     * 新增用户信息
     * @param userInfoModel 用户信息
     * @param groupIds 用户所属组，用户可以属于多个用户组
     * @param extendPropertyValues 用户扩展属性值
     * @return
     * @throws BizException
     */
    public Long addUserInfo(UserInfoModel userInfoModel, List<Long> groupIds, List<ExtendPropertyValueModel> extendPropertyValues) throws BizException {
        // 数据校验以及设置默认值
        checkAdd(userInfoModel);

        // 新增用户信息
        userInfoModelMapper.insert(userInfoModel);

        // 新增账户记录
        accountService.addAccount(userInfoModel);

        // 关联用户组
        if(CollectionUtils.isNotEmpty(groupIds)){
            userInfoGroupRelationService.batchAddUserInfoGroupRelation(userInfoModel.getUserId(), groupIds);
        }

        Long userId = userInfoModel.getUserId();
        if(CollectionUtils.isNotEmpty(extendPropertyValues)){
            for (ExtendPropertyValueModel extendPropertyValue : extendPropertyValues){
                extendPropertyValue.setUserId(userId);
            }
            extendPropertyValueService.batchAddExtendPropertyValue(extendPropertyValues);
        }
        return userId;
    }

    public boolean updateUserInfo(UserInfoModel userInfoModel, List<Long> groupIds, List<ExtendPropertyValueModel> extendPropertyValues) throws BizException {
        Assert.notNull(userInfoModel);

        // 数据校验以及设置默认值
        checkUpdate(userInfoModel);

        // 更新用户信息
        userInfoModelMapper.updateByPrimaryKeySelective(userInfoModel);

        // 更新账户信息
        accountService.updateAccount(userInfoModel);

        // 更新用户组关联
        if(groupIds != null){
            userInfoGroupRelationService.batchUpdateUserInfoGroupRelation(userInfoModel.getUserId(), groupIds);
        }

        // 更新用户扩展属性值
        Long userId = userInfoModel.getUserId();
        if(CollectionUtils.isNotEmpty(extendPropertyValues)){
            for (ExtendPropertyValueModel extendPropertyValue : extendPropertyValues){
                extendPropertyValue.setUserId(userId);
            }
            extendPropertyValueService.batchUpdateExtendPropertyValue(extendPropertyValues);
        }
        RedisUtil.delMapField(UserConstant.REDIS_USER_INFO_KEY, userId.toString());
        return true;
    }

    public boolean deleteUserInfo(Long userId) throws BizException {
        Assert.notNull(userId,"用户id不能为空！");
        // 逻辑删除账户信息
        UserInfoModel userInfoModel = new UserInfoModel();
        userInfoModel.setUserId(userId);
        userInfoModel.setIsDelete(UserConstant.USER_DELETEED_TRUE);
        boolean result = userInfoModelMapper.updateByPrimaryKeySelective(userInfoModel) > 0;
        if(result){
            // 删除账户表记录
            accountService.deleteAccount(userId);
            RedisUtil.delMapField(UserConstant.REDIS_USER_INFO_KEY, userId.toString());
        }
        return result;
    }

    public boolean batchDeleteUserInfo(List<Long> userIds) throws BizException {
        Assert.notEmpty(userIds,"用户id不能为空！");
        // 逻辑删除账户信息
        UserInfoModel userInfoModel = new UserInfoModel();
        userInfoModel.setIsDelete(UserConstant.USER_DELETEED_TRUE);
        boolean result = userInfoModelMapper.updateUserInfoByUserIds(userInfoModel, userIds) > 0;
        if(result) {
            // 删除账户表记录
            accountService.batchDeleteAccount(userIds);
            for(Long userId : userIds){
                RedisUtil.delMapField(UserConstant.REDIS_USER_INFO_KEY, userId.toString());
            }
        }
        return result;
    }


    public UserInfoModel existUserInfo(UserInfoModel userInfoModel) throws BizException {
        Assert.notNull(userInfoModel);
        return userInfoModelMapper.existUserInfo(userInfoModel);
    }

    /**
     * 新增校验以及设置默认值
     * @param userInfoModel 用户信息model
     * @throws BizException 业务异常
     */
    public void checkAdd(UserInfoModel userInfoModel) throws BizException {
        checkUsername(userInfoModel);
        checkPassword(userInfoModel);
        checkMobile(userInfoModel);
        checkEmail(userInfoModel);
        if (!Integer.valueOf(UserConstant.USER_STATUS_DISABLED).equals(userInfoModel.getStatus())){
            userInfoModel.setStatus(UserConstant.USER_STATUS_ENABLED);
        }
        if(userInfoModel.getAlias() == null){
            userInfoModel.setAlias("");
        }
        userInfoModel.setInternal(UserConstant.USER_INTERNAL_FALSE);
        userInfoModel.setIsDelete(UserConstant.USER_DELETEED_FALSE);
        userInfoModel.setAttemptIp("");
        userInfoModel.setAttemptTime(0L);
        userInfoModel.setCreateTime(new Date().getTime());
    }

    /**
     * 更新校验
     * @param userInfoModel 用户信息model
     * @throws BizException 业务异常
     */
    private void checkUpdate(UserInfoModel userInfoModel) throws BizException {
        Long userId = userInfoModel.getUserId();
        Assert.notNull(userId,"用户id不能为空！");
        UserInfoModel dbUserInfoModel = userInfoModelMapper.selectByPrimaryKey(userId);
        Assert.notNull(dbUserInfoModel,"用户id不存在！");
        Assert.isTrue(dbUserInfoModel.getInternal() == UserConstant.USER_INTERNAL_FALSE, "不能更新内部用户！");
        Assert.isTrue(dbUserInfoModel.getIsDelete() == UserConstant.USER_DELETEED_FALSE, "用户已经被删除！");

        String username = userInfoModel.getUsername();
        if(username != null && !username.equals(dbUserInfoModel.getUsername())){
            checkUsername(userInfoModel);
        } else {
            userInfoModel.setUsername(dbUserInfoModel.getUsername());
        }

        String mobile = userInfoModel.getMobile();
        if(mobile != null && !mobile.equals(dbUserInfoModel.getMobile())){
            checkMobile(userInfoModel);
        }

        String email = userInfoModel.getEmail();
        if(email != null && !email.equals(dbUserInfoModel.getEmail())){
            checkEmail(userInfoModel);
        }
        checkPassword(userInfoModel);
        userInfoModel.setCreateTime(null);
        userInfoModel.setInternal(null);
        userInfoModel.setIsDelete(null);
    }

    /**
     * 校验手机号
     * @param userInfoModel 手机号
     * @throws BizException 业务异常
     */
    private void checkMobile(UserInfoModel userInfoModel) throws BizException {
        Long userId = userInfoModel.getUserId();
        String mobile = userInfoModel.getMobile();
        if(mobile != null){
            Assert.isTrue(mobile.matches(UserConstant.Regex.MOBILE), "手机号码格式错误！");

            UserInfoModel exist = new UserInfoModel();
            exist.setMobile(mobile);
            exist = existUserInfo(exist);
            Assert.isTrue(exist == null || exist.getUserId().equals (userId), "该手机号已被使用！");
        } else{
            userInfoModel.setMobile("");
        }
    }

    /**
     * 校验邮箱
     * @param userInfoModel email属性
     * @throws BizException  业务异常
     */
    private void checkEmail(UserInfoModel userInfoModel) throws BizException {
        Long userId = userInfoModel.getUserId();
        String email = userInfoModel.getEmail();
        if(email != null){
            if(!email.matches(UserConstant.Regex.EMAIL)){
                throw new BizException("邮箱格式错误！");
            }
            UserInfoModel exist = new UserInfoModel();
            exist.setEmail(email);
            exist = existUserInfo(exist);
            Assert.isTrue(exist == null || exist.getUserId().equals (userId),"该邮箱已经被使用！");
        } else {
            userInfoModel.setEmail("");
        }
    }

    /**
     * 校验密码
     * @param userInfoModel 密码
     * @throws BizException 业务异常
     */
    private void checkPassword(UserInfoModel userInfoModel) throws BizException {
        Long userId = userInfoModel.getUserId();
        String password = userInfoModel.getPassword();
        if(userId == null){
            Assert.notNull(password, "密码不能为空！");
        }
        if(password != null){
            Assert.isTrue(password.length()>4, "密码长度不能小于5！");
            userInfoModel.setPassword(SHAMaker.sha256String(password));
        }
    }

    /**
     * 校验用户名
     * @param userInfoModel 用户名
     * @throws BizException 业务异常
     */
    private void checkUsername(UserInfoModel userInfoModel) throws BizException {
        Long userId = userInfoModel.getUserId();
        String username = userInfoModel.getUsername();
        if(userId == null){
            Assert.notNull(username, "用户名不能为空！");
        }
        if(username != null){
            Assert.isTrue(username.matches(UserConstant.Regex.USERNAME), "用户名格式错误！");

            UserInfoModel exist = new UserInfoModel();
            exist.setUsername(username);
            exist = existUserInfo(exist);
            Assert.isTrue(exist == null || exist.getUserId().equals(userId), "用户名已存在！");
        }
    }

}
