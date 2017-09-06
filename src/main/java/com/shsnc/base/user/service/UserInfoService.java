package com.shsnc.base.user.service;

import com.shsnc.api.core.ThreadContext;
import com.shsnc.base.authorization.mapper.AuthorizationRoleModelMapper;
import com.shsnc.base.authorization.mapper.AuthorizationUserRoleRelationModelMapper;
import com.shsnc.base.authorization.model.AuthorizationRoleModel;
import com.shsnc.base.authorization.model.AuthorizationUserRoleRelationModel;
import com.shsnc.base.authorization.service.AssignService;
import com.shsnc.base.user.config.UserConstant;
import com.shsnc.base.user.mapper.*;
import com.shsnc.base.user.model.*;
import com.shsnc.base.bean.Condition;
import com.shsnc.base.user.model.condition.UserInfoCondition;
import com.shsnc.base.user.support.Assert;
import com.shsnc.base.util.BizAssert;
import com.shsnc.base.util.JsonUtil;
import com.shsnc.base.util.RedisUtil;
import com.shsnc.base.util.bean.RelationMap;
import com.shsnc.base.util.config.BaseException;
import com.shsnc.base.util.config.BizException;
import com.shsnc.base.util.config.MessageCode;
import com.shsnc.base.util.crypto.SHAMaker;
import com.shsnc.base.util.sql.Pagination;
import com.shsnc.base.util.sql.QueryData;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 *
 */
@Service
public class UserInfoService {

    @Autowired
    private UserInfoModelMapper userInfoModelMapper;
    @Autowired
    private OrganizationModelMapper organizationModelMapper;
    @Autowired
    private GroupModelMapper groupModelMapper;
    @Autowired
    private UserInfoGroupRelationModelMapper userInfoGroupRelationModelMapper;
    @Autowired
    private UserInfoGroupRelationService userInfoGroupRelationService;
    @Autowired
    private UserInfoOrganizationRelationModelMapper userInfoOrganizationRelationModelMapper;
    @Autowired
    private UserInfoOrganizationRelationService userInfoOrganizationRelationService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private ExtendPropertyValueService extendPropertyValueService;
    @Autowired
    private AssignService assignService;

    @Autowired
    private AuthorizationRoleModelMapper authorizationRoleModelMapper;
    @Autowired
    private AuthorizationUserRoleRelationModelMapper authorizationUserRoleRelationModelMapper;

    public UserInfoModel getUserInfo(Long userId) throws BaseException {
        Assert.notNull(userId,"用户id不能为空！");
//        if (!ThreadContext.getUserInfo().isSuperAdmin()) {
//            Condition condition = new Condition(true, ThreadContext.getUserInfo().getGroupIds());
//            Long dbUserId = userInfoGroupRelationModelMapper.getUserIdByUserId(userId, condition);
//            if (dbUserId == null) {
//                throw new BaseException(MessageCode.PERMISSION_DENIED);
//            }
//        }
        return getUserInfoByCache(userId);
    }

    public UserInfoModel getUserInfoByCache(Long userId) throws BizException {
        Assert.notNull(userId,"用户id不能为空！");
        String userInfoJson = RedisUtil.getFieldValue(UserConstant.REDIS_USER_INFO_KEY, userId.toString());
        if(userInfoJson != null){
            return JsonUtil.jsonToObject(userInfoJson, UserInfoModel.class );
        }
        UserInfoModel userInfoModel = userInfoModelMapper.selectByPrimaryKey(userId);
        if (userInfoModel != null) {
            RedisUtil.setFieldValue(UserConstant.REDIS_USER_INFO_KEY, userId.toString(),JsonUtil.toJsonString(userInfoModel),1000000);
        }
        return userInfoModel;
    }

    public QueryData getUserInfoPage(UserInfoCondition condition, Pagination pagination) {
        // 权限
        if (!ThreadContext.getUserInfo().isSuperAdmin()) {
            List<Long> groupIds = ThreadContext.getUserInfo().getGroupIds();
            if (!groupIds.isEmpty()) {
                List<Long> userIds = userInfoGroupRelationModelMapper.getUserIdsByGroupIds(groupIds);
                condition.permission(true, userIds);
            } else {
                return new QueryData(pagination);
            }
        }
        QueryData queryData = new QueryData(pagination);
        int totalCount = userInfoModelMapper.getTotalCountByCondition(condition);
        queryData.setRowCount(totalCount);
        List<UserInfoModel> list = userInfoModelMapper.getPageByCondition(condition, pagination);
        selectOrganization(list);
        selectGroups(list);
        selectRoles(list);
        queryData.setRecords(list);
        return queryData;
    }

    public void selectRoles(List<UserInfoModel> userInfoModels) {
        if (CollectionUtils.isEmpty(userInfoModels)) {
            return;
        }
        List<Long> userIds = userInfoModels.stream().map(UserInfoModel::getUserId).collect(Collectors.toList());
        if (!userIds.isEmpty()) {
            List<AuthorizationUserRoleRelationModel> relations = authorizationUserRoleRelationModelMapper.getByUserIds(userIds);
            RelationMap relationMap = new RelationMap();
            for (AuthorizationUserRoleRelationModel relation : relations) {
                relationMap.addRelation(relation.getUserId(),relation.getRoleId());
            }
            if (relationMap.hasRelatedIds()) {
                List<AuthorizationRoleModel> authorizationRoleModels = authorizationRoleModelMapper.getByRoleIds(relationMap.getRelatedIds());
                Map<Long, AuthorizationRoleModel> authorizationRoleModelMap = authorizationRoleModels.stream().collect(Collectors.toMap(AuthorizationRoleModel::getRoleId, x -> x, (oldValue, newValue)->oldValue));
                for (UserInfoModel userInfoModel : userInfoModels) {
                    userInfoModel.setRoles(relationMap.getRelatedObjects(userInfoModel.getUserId(), authorizationRoleModelMap));
                }
            }
        }
    }

    public void selectOrganization(List<UserInfoModel> userInfoModels) {
        if (CollectionUtils.isEmpty(userInfoModels)) {
            return;
        }
        List<Long> userIds = userInfoModels.stream().map(UserInfoModel::getUserId).collect(Collectors.toList());
        if (!userIds.isEmpty()) {
            List<UserInfoOrganizationRelationModel> relations = userInfoOrganizationRelationModelMapper.getByUserIds(userIds);
            RelationMap relationMap = new RelationMap();
            for (UserInfoOrganizationRelationModel relation : relations) {
                relationMap.addRelation(relation.getUserId(),relation.getOrganizationId());
            }
            if (relationMap.hasRelatedIds()) {
                List<OrganizationModel> organizationModels = organizationModelMapper.getByOrganizationIdIds(relationMap.getRelatedIds());
                Map<Long, OrganizationModel> organizationModelMap = organizationModels.stream().collect(Collectors.toMap(OrganizationModel::getOrganizationId, x -> x, (oldValue, newValue)->oldValue));
                for (UserInfoModel userInfoModel : userInfoModels) {
                    userInfoModel.setOrganization(relationMap.getRelatedObject(userInfoModel.getUserId(),organizationModelMap));
                }
            }
        }
    }

    public void selectGroups(List<UserInfoModel> userInfoModels) {
        if (CollectionUtils.isEmpty(userInfoModels)) {
            return;
        }
        List<Long> userIds = userInfoModels.stream().map(UserInfoModel::getUserId).collect(Collectors.toList());
        if (!userIds.isEmpty()) {
            Condition condition = new Condition();
            if (!ThreadContext.getUserInfo().isSuperAdmin()) {
                List<Long> groupIds = ThreadContext.getUserInfo().getGroupIds();
                condition.permission(true, groupIds);
            }
            List<UserInfoGroupRelationModel> relations = userInfoGroupRelationModelMapper.getByUserIds(userIds, condition);
            RelationMap relationMap = new RelationMap();
            for (UserInfoGroupRelationModel relation : relations) {
                relationMap.addRelation(relation.getUserId(),relation.getGroupId());
            }
            if (relationMap.hasRelatedIds()) {
                List<GroupModel> groupModels = groupModelMapper.getByGroupIds(relationMap.getRelatedIds());
                Map<Long, GroupModel> groupModelMap = groupModels.stream().collect(Collectors.toMap(GroupModel::getGroupId, x -> x, (oldValue, newValue)->oldValue));
                for (UserInfoModel userInfoModel : userInfoModels) {
                    Long defaultGroupId = userInfoModel.getDefaultGroupId();
                    if (defaultGroupId != null) {
                        userInfoModel.setDefaultGroup(groupModelMap.get(defaultGroupId));
                    }
                    userInfoModel.setGroups(relationMap.getRelatedObjects(userInfoModel.getUserId(),groupModelMap));
                }
            }
        }
    }

    /**
     * 新增用户信息
     * @param userInfoModel 用户信息
     * @param extendPropertyValues 用户扩展属性值
     * @return
     * @throws BizException
     */
    public Long addUserInfo(UserInfoModel userInfoModel, List<ExtendPropertyValueModel> extendPropertyValues, List<Long> roleIds) throws BaseException {
        Assert.notNull(userInfoModel);

        // 数据校验以及设置默认值
        checkAdd(userInfoModel);

        // 新增用户信息
        userInfoModelMapper.insert(userInfoModel);

        // 新增账户记录
        accountService.addAccount(userInfoModel);

        // 关联组织结构
        userInfoOrganizationRelationService.updateUserInfoOrganizationRelation(userInfoModel.getUserId(), userInfoModel.getOrganizationId());

        // 关联用户组
        userInfoGroupRelationService.batchUpdateUserInfoGroupRelation(userInfoModel.getUserId(), userInfoModel.getGroupIds(), false);

        // 添加扩展属性
        Long userId = userInfoModel.getUserId();
        if(CollectionUtils.isNotEmpty(extendPropertyValues)){
            for (ExtendPropertyValueModel extendPropertyValue : extendPropertyValues){
                extendPropertyValue.setUserId(userId);
            }
            extendPropertyValueService.batchAddExtendPropertyValue(extendPropertyValues);
        }

        // 为用户添加角色
        if (roleIds != null) {
            assignService.userAssignRole(userId,roleIds);
        }
        return userId;
    }

    public boolean updateUserInfo(UserInfoModel userInfoModel, List<ExtendPropertyValueModel> extendPropertyValues, List<Long> roleIds) throws BaseException {
        Assert.notNull(userInfoModel);

        // 数据校验以及设置默认值
        checkUpdate(userInfoModel);

        // 更新用户信息
        userInfoModelMapper.updateByPrimaryKeySelective(userInfoModel);

        // 更新账户信息
        accountService.updateAccount(userInfoModel);

        // 更新与组织部门的关联
        if (userInfoModel.getOrganizationId() != null) {
            userInfoOrganizationRelationService.updateUserInfoOrganizationRelation(userInfoModel.getUserId(), userInfoModel.getOrganizationId());
        }

        // 更新与用户组的关系
        List<Long> groupIds = userInfoModel.getGroupIds();
        if(groupIds != null){
            try{
                userInfoGroupRelationService.batchUpdateUserInfoGroupRelation(userInfoModel.getUserId(), groupIds, true);
            }catch(Exception e){
                throw new BaseException(MessageCode.BIZ_ERROR,"存在重复的用户与用户组关系！");
            }
           
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

        // 为用户添加角色
        if (roleIds != null) {
            assignService.userAssignRole(userId,roleIds);
        }
        return true;
    }

    public boolean deleteUserInfo(Long userId) throws BaseException {
        Assert.notNull(userId,"用户id不能为空！");
        if (!ThreadContext.getUserInfo().isSuperAdmin()) {
            List<Long> groupIds = ThreadContext.getUserInfo().getGroupIds();
            Condition condition = new Condition(true, groupIds);
            Long dbUserId = userInfoGroupRelationModelMapper.getUserIdByUserId(userId, condition);
            if (dbUserId == null) {
                throw new BaseException(MessageCode.PERMISSION_DENIED);
            }
        }
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

    public boolean batchDeleteUserInfo(List<Long> userIds) throws BaseException {
        Assert.notEmpty(userIds,"用户id不能为空！");
        if (!ThreadContext.getUserInfo().isSuperAdmin()) {
            List<Long> groupIds = ThreadContext.getUserInfo().getGroupIds();
            Condition condition = new Condition(true, groupIds);
            List<Long> dbUserIds = userInfoGroupRelationModelMapper.getUserIdsByUserIds(userIds, condition);
            if (!dbUserIds.containsAll(userIds)) {
                throw new BaseException(MessageCode.PERMISSION_DENIED);
            }
        }
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
    public void checkAdd(UserInfoModel userInfoModel) throws BaseException {
        checkUsername(userInfoModel);
        checkPassword(userInfoModel);
        checkMobile(userInfoModel);
        checkEmail(userInfoModel);
        BizAssert.hasLength(userInfoModel.getAlias(),"用户姓名不能为空！");
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
        Long organizationId = userInfoModel.getOrganizationId();
        BizAssert.notNull(organizationId, "用户所属组织不能为空！");
        OrganizationModel organizationModel = organizationModelMapper.selectByPrimaryKey(organizationId);
        BizAssert.notNull(organizationModel, String.format("用户所属组织id【%s】不存在！", organizationId));

        BizAssert.notNull(userInfoModel.getDefaultGroupId(), "所属默认用户组不能为空！");
        BizAssert.notEmpty(userInfoModel.getGroupIds(), "管辖用户组不能为空！");
        BizAssert.isTrue(userInfoModel.getGroupIds().contains(userInfoModel.getDefaultGroupId()),"管辖用户组必须包含所属默认用户组");
        if (!ThreadContext.getUserInfo().isSuperAdmin()) {
            List<Long> groupIds = ThreadContext.getUserInfo().getGroupIds();
            if (!groupIds.containsAll(userInfoModel.getGroupIds())) {
                throw new BaseException(MessageCode.PERMISSION_DENIED);
            }
        }
    }

    /**
     * 更新校验
     * @param userInfoModel 用户信息model
     * @throws BizException 业务异常
     */
    private void checkUpdate(UserInfoModel userInfoModel) throws BaseException {
        Long userId = userInfoModel.getUserId();
        Assert.notNull(userId,"用户id不能为空！");

        UserInfoModel dbUserInfoModel = userInfoModelMapper.selectByPrimaryKey(userId);
        Assert.notNull(dbUserInfoModel,"用户id不存在！");
        //Assert.isTrue(dbUserInfoModel.getInternal() == UserConstant.USER_INTERNAL_FALSE, "不能更新内部用户！");
        Assert.isTrue(dbUserInfoModel.getIsDelete() == UserConstant.USER_DELETEED_FALSE, "用户已经被删除！");

        if (!ThreadContext.getUserInfo().isSuperAdmin()) {
            Condition condition = new Condition(true, ThreadContext.getUserInfo().getGroupIds());
            Long dbUserId = userInfoGroupRelationModelMapper.getUserIdByUserId(userId, condition);
            if (dbUserId == null) {
                throw new BaseException(MessageCode.PERMISSION_DENIED);
            }
        }

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
        userInfoModel.setPassword(null);
        userInfoModel.setCreateTime(null);
        userInfoModel.setInternal(null);
        userInfoModel.setIsDelete(null);

        String alias = userInfoModel.getAlias();
        if (alias != null) {
            BizAssert.hasLength(alias, "用户姓名不能为空！");
        }

        List<Long> groupIds = userInfoModel.getGroupIds();
        Long defaultGroupId = userInfoModel.getDefaultGroupId();
        if (defaultGroupId != null) {
            BizAssert.notEmpty(groupIds, "管辖用户组不能为空！");
            BizAssert.isTrue(groupIds.contains(defaultGroupId), "管辖用户组必须包含所属默认用户组！");
            if (!ThreadContext.getUserInfo().isSuperAdmin()) {
                List<Long> currentGroupIds = ThreadContext.getUserInfo().getGroupIds();
                if (!currentGroupIds.containsAll(userInfoModel.getGroupIds())) {
                    throw new BaseException(MessageCode.PERMISSION_DENIED);
                }
            }
        } else if(groupIds != null){
            BizAssert.notEmpty(groupIds, "管辖用户组不能为空！");
            BizAssert.notNull(defaultGroupId, "默认用户组不能为空！");
            BizAssert.isTrue(groupIds.contains(defaultGroupId), "管辖用户组必须包含所属默认用户组！");
            if (!ThreadContext.getUserInfo().isSuperAdmin()) {
                List<Long> currentGroupIds = ThreadContext.getUserInfo().getGroupIds();
                if (!currentGroupIds.containsAll(userInfoModel.getGroupIds())) {
                    throw new BaseException(MessageCode.PERMISSION_DENIED);
                }
            }
        }
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

    public boolean updatePassword(Long userId, String newPassword) throws BaseException {
        Assert.notNull(userId,"用户id不能为空！");
        UserInfoModel dbUserInfoModel = userInfoModelMapper.selectByPrimaryKey(userId);
        Assert.notNull(dbUserInfoModel,"用户id不存在！");
        Assert.isTrue(dbUserInfoModel.getInternal() == UserConstant.USER_INTERNAL_FALSE, "不能更新内部用户！");

        if (!ThreadContext.getUserInfo().isSuperAdmin()) {
            List<Long> groupIds = ThreadContext.getUserInfo().getGroupIds();
            Condition condition = new Condition(true, groupIds);
            Long dbUserId = userInfoGroupRelationModelMapper.getUserIdByUserId(userId, condition);
            if (dbUserId == null) {
                throw new BaseException(MessageCode.PERMISSION_DENIED);
            }
        }
        UserInfoModel userInfoModel = new UserInfoModel();
        userInfoModel.setUserId(userId);
        userInfoModel.setPassword(newPassword);
        checkPassword(userInfoModel);
        return userInfoModelMapper.updateByPrimaryKeySelective(userInfoModel)>0;
    }

    public List<UserInfoModel> getUserInfoListByUserIds(List<Long> userIds) throws BaseException {
        BizAssert.notEmpty(userIds,"用户ID不能为空！");
        List<UserInfoModel> userInfoModels = new ArrayList<>();
        for (Long userId : userIds) {
            UserInfoModel userInfo = getUserInfo(userId);
            if (userInfo != null) {
                userInfoModels.add(userInfo);
            }
        }
        return userInfoModels;
    }

    /**
     * 移动用户到指定组织部门
     * @param userIds
     * @param organizationId
     * @return
     */
    public boolean moveToOrganization(List<Long> userIds, Long organizationId) throws BizException {
        return userInfoOrganizationRelationService.batchUpdateUserToOrganizationRelation(userIds, organizationId);
    }

    public List<UserInfoModel> findUsers(UserInfoCondition condition, boolean checkPermission) {
        if (checkPermission && !ThreadContext.getUserInfo().isSuperAdmin()) {
            List<Long> groupIds = ThreadContext.getUserInfo().getGroupIds();
            if (!groupIds.isEmpty()) {
                List<Long> userIds = userInfoGroupRelationModelMapper.getUserIdsByGroupIds(groupIds);
                condition.permission(true,userIds);
            } else {
                return new ArrayList<>();
            }
        }
        List<UserInfoModel> userInfoModels = userInfoModelMapper.getListByCondition(condition);
        selectOrganization(userInfoModels);
        return userInfoModels;
    }

    public List<Long> getCurrentUserIds(long userId) {
        return userInfoGroupRelationModelMapper.getCurrentUserIds(userId);
    }

    public List<Long> getUserIdsByCondition(UserInfoCondition condition) {
        return userInfoModelMapper.getUserIdsByCondition(condition);
    }
}
