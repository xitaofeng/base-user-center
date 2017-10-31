package com.shsnc.base.user.service;

import com.google.common.collect.Sets;
import com.shsnc.api.core.ThreadContext;
import com.shsnc.base.bean.Condition;
import com.shsnc.base.user.mapper.GroupModelMapper;
import com.shsnc.base.user.mapper.UserInfoGroupRelationModelMapper;
import com.shsnc.base.user.mapper.UserInfoModelMapper;
import com.shsnc.base.user.model.GroupModel;
import com.shsnc.base.user.model.UserInfoGroupRelationModel;
import com.shsnc.base.user.model.UserInfoModel;
import com.shsnc.base.util.BizAssert;
import com.shsnc.base.util.config.BaseException;
import com.shsnc.base.util.config.BizException;
import com.shsnc.base.util.config.MessageCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author houguangqiang
 * @date 2017-07-26
 * @since 1.0
 */
@Service
public class UserInfoGroupRelationService {

    @Autowired
    private UserInfoGroupRelationModelMapper userInfoGroupRelationModelMapper;
    @Autowired
    private UserInfoModelMapper userInfoModelMapper;
    @Autowired
    private GroupModelMapper groupModelMapper;

    public boolean batchUpdateUserInfoGroupRelation(Long userId, List<Long> groupIds, boolean update) throws BaseException {
        BizAssert.notNull(userId, "用户id不能为空！");
        UserInfoModel userInfoModel = userInfoModelMapper.selectByPrimaryKey(userId);
        BizAssert.notNull(userInfoModel, String.format("用户id【%s】不存在！",userId));
        BizAssert.notEmpty(groupIds, String.format("用户【%s】必须至少归属于一个用户组", userInfoModel.getUsername()));
        List<Long> checkGroupIds = groupModelMapper.getGroupIdsByGroupIds(groupIds);
        BizAssert.isTrue(checkGroupIds.size() == groupIds.size(), "含有不存在的用户组id！");

        // 获取当前用户能看得到关系数据
        Condition condition = new Condition();
        if (!ThreadContext.getUserInfo().isSuperAdmin()) {
            List<Long> currentGroupIds = ThreadContext.getUserInfo().getGroupIds();
            if (!currentGroupIds.containsAll(groupIds)) {
                throw new BaseException(MessageCode.PERMISSION_DENIED);
            }
            if (update) {
                List<Long> checkUserIds = userInfoGroupRelationModelMapper.getUserIdsByGroupIds(currentGroupIds);
                if (!checkUserIds.contains(userId)) {
                    throw new BaseException(MessageCode.PERMISSION_DENIED);
                }
            }
            condition.permission(true, currentGroupIds);
        }
        List<UserInfoGroupRelationModel> dbGroup = userInfoGroupRelationModelMapper.getByUserId(userId, condition);
        Map<Long, UserInfoGroupRelationModel> dbGroupMap = dbGroup.stream().collect(Collectors.toMap(UserInfoGroupRelationModel::getGroupId, v->v));

        Set<Long> groupIdSet = Sets.newHashSet(groupIds);
        Set<Long> dbGroupIds = dbGroupMap.keySet();
        Set<Long> deleteGroupIds = Sets.difference(dbGroupIds, groupIdSet);
        Set<Long> addGroupIds = Sets.difference(groupIdSet, dbGroupIds);

        // 删除
        if (!deleteGroupIds.isEmpty()) {
            List<Long> deleteRelationIds = new ArrayList<>();
            for (Long deleteGroupId : deleteGroupIds) {
                deleteRelationIds.add(dbGroupMap.get(deleteGroupId).getRelationId());
            }
            userInfoGroupRelationModelMapper.deleteByRelationIds(deleteRelationIds);
        }
        // 新增
        if (!addGroupIds.isEmpty()) {
            List<UserInfoGroupRelationModel> userInfoGroupRelationModels = new ArrayList<>();
            for (Long groupId : addGroupIds) {
                UserInfoGroupRelationModel relation = new UserInfoGroupRelationModel();
                relation.setGroupId(groupId);
                relation.setUserId(userId);
                userInfoGroupRelationModels.add(relation);
            }
            userInfoGroupRelationModelMapper.insertRelationList(userInfoGroupRelationModels);
        }
        return true;
    }

    public void batchUpdateGroupUserInfoRelation(Long groupId, List<Long> userIds, boolean update) throws BaseException {
        BizAssert.notNull(groupId, "用户组id不能为空！");
        BizAssert.notNull(userIds, "用户id不能为空！");
        GroupModel groupModel = groupModelMapper.selectByPrimaryKey(groupId);
        BizAssert.notNull(groupModel, String.format("用户组id【%s】不存在！",groupId));

        List<Long> dbUserIds = userIds;
        if (!userIds.isEmpty()) {
            dbUserIds = userInfoModelMapper.getUserIdsByUserIds(userIds);
            BizAssert.isTrue(userIds.size() == dbUserIds.size(), "含有不存在的用户id");
        }

        // 获取当前用户能看得到关系数据
        Condition condition = new Condition();
        if (!ThreadContext.getUserInfo().isSuperAdmin()) {
            List<Long> groupIds = ThreadContext.getUserInfo().getGroupIds();
            if (!groupIds.contains(groupId)) {
                throw new BaseException(MessageCode.PERMISSION_DENIED);
            }
            if (update) {
                List<Long> checkUserIds = userInfoGroupRelationModelMapper.getUserIdsByGroupIds(groupIds);
                if (!checkUserIds.containsAll(userIds)) {
                    throw new BaseException(MessageCode.PERMISSION_DENIED);
                }
            }
            condition.permission(true, groupIds);
        }

        List<UserInfoGroupRelationModel> dbGroup = userInfoGroupRelationModelMapper.getByGroupId(groupId);
        Map<Long, UserInfoGroupRelationModel> dbGroupMap = dbGroup.stream().collect(Collectors.toMap(UserInfoGroupRelationModel::getUserId, v->v));

        Set<Long> dbGroupIds = dbGroupMap.keySet();
        Set<Long> userIdSet = Sets.newHashSet(userIds);
        Set<Long> deleteUserIds = Sets.difference(dbGroupIds, userIdSet);
        Set<Long> addUserIds = Sets.difference(userIdSet, dbGroupIds);

        // 删除
        if (!deleteUserIds.isEmpty()) {
            List<Long> notDeletableUserIds = userInfoGroupRelationModelMapper.getNotDeletableUserIds(deleteUserIds);
            if (!notDeletableUserIds.isEmpty()) {
                for (Long userId : notDeletableUserIds) {
                    UserInfoModel userInfoModel = userInfoModelMapper.selectByPrimaryKey(userId);
                    if (userInfoModel != null) {
                        throw new BizException(String.format("用户【%s】必须至少归属于一个用户组", userInfoModel.getUsername()));
                    }
                }
            }
            List<Long> deleteRelationIds = new ArrayList<>();
            for (Long deleteUserId : deleteUserIds) {
                deleteRelationIds.add(dbGroupMap.get(deleteUserId).getRelationId());
            }
            userInfoGroupRelationModelMapper.deleteByRelationIds(deleteRelationIds);
        }

        // 新增
        if (!addUserIds.isEmpty()) {
            List<UserInfoGroupRelationModel> userInfoGroupRelationModels = new ArrayList<>();
            for (Long addUserId : addUserIds) {
                UserInfoGroupRelationModel relation = new UserInfoGroupRelationModel();
                relation.setGroupId(groupId);
                relation.setUserId(addUserId);
                userInfoGroupRelationModels.add(relation);
            }
            userInfoGroupRelationModelMapper.insertRelationList(userInfoGroupRelationModels);
        }

    }

    public void deleteByGroupId(Long groupId) throws BaseException {
        BizAssert.notNull(groupId, "用户组id不能为空！");
        GroupModel groupModel = groupModelMapper.selectByPrimaryKey(groupId);
        BizAssert.notNull(groupModel, "用户组id不存在！");

        if (!ThreadContext.getUserInfo().isSuperAdmin()) {
            List<Long> groupIds = ThreadContext.getUserInfo().getGroupIds();
            if (!groupIds.contains(groupId)) {
                throw new BaseException(MessageCode.PERMISSION_DENIED);
            }
        }

        List<Long> dbUserIds = userInfoGroupRelationModelMapper.getUserIdsByGroupId(groupId);
        if(dbUserIds.size()>0){
            List<Long> notDeletableUserIds = userInfoGroupRelationModelMapper.getNotDeletableUserIds(dbUserIds);
            for (Long userId : notDeletableUserIds) {
                UserInfoModel userInfoModel = userInfoModelMapper.selectByPrimaryKey(userId);
                if (userInfoModel != null) {
                    throw new BizException(String.format("不能删除用户组【%s】，用户【%s】必须至少归属于一个用户组", groupModel.getName(), userInfoModel.getUsername()));
                }
            }
            
        }
      
        userInfoGroupRelationModelMapper.deleteByGroupId(groupId);
    }

    public void deleteByGroupIds(List<Long> groupIds) throws BaseException {
        BizAssert.notEmpty(groupIds, "用户组id不能为空！");
        List<GroupModel> groupModels = groupModelMapper.getByGroupIds(groupIds);
        BizAssert.notNull(groupModels.size() == groupIds.size(), "含有不存在的用户组id！");

        if (!ThreadContext.getUserInfo().isSuperAdmin()) {
            List<Long> currentGroupIds = ThreadContext.getUserInfo().getGroupIds();
            if (!currentGroupIds.containsAll(groupIds)) {
                throw new BaseException(MessageCode.PERMISSION_DENIED);
            }
        }

        List<Long> dbUserIds = userInfoGroupRelationModelMapper.getUserIdsByGroupIds(groupIds);
        List<Long> notDeletableUserIds = userInfoGroupRelationModelMapper.getNotDeletableUserIds(dbUserIds);
        for (Long userId : notDeletableUserIds) {
            UserInfoModel userInfoModel = userInfoModelMapper.selectByPrimaryKey(userId);
            if (userInfoModel != null) {
                throw new BizException(String.format("用户【%s】必须至少归属于一个用户组", userInfoModel.getUsername()));
            }
        }
        userInfoGroupRelationModelMapper.deleteByGroupIds(groupIds);
    }

}
