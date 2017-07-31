package com.shsnc.base.user.service;

import com.shsnc.base.user.mapper.GroupModelMapper;
import com.shsnc.base.user.mapper.UserInfoGroupRelationModelMapper;
import com.shsnc.base.user.mapper.UserInfoModelMapper;
import com.shsnc.base.user.model.GroupModel;
import com.shsnc.base.user.model.UserInfoGroupRelationModel;
import com.shsnc.base.user.model.UserInfoModel;
import com.shsnc.base.util.BizAssert;
import com.shsnc.base.util.config.BizException;
import org.apache.commons.collections.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    public List<Long> batchAddUserInfoGroupRelation(Long userId, List<Long> groupIds) throws BizException {
        BizAssert.notNull(userId,"用户id不能为空！");
        BizAssert.notEmpty(groupIds, "用户组id不能为空！");
        UserInfoModel userInfoModel = userInfoModelMapper.selectByPrimaryKey(userId);
        BizAssert.notNull(userInfoModel, "用户id不存在！");
        List<Long> dbGroupIds = groupModelMapper.getGroupIdsByGroupIds(groupIds);
        BizAssert.isTrue(dbGroupIds.size() == groupIds.size(), "含有不存在的用户组id！");

        List<UserInfoGroupRelationModel> userInfoGroupRelationModels = new ArrayList<>();
        for(Long groupId : groupIds){
            UserInfoGroupRelationModel relation = new UserInfoGroupRelationModel();
            relation.setGroupId(groupId);
            relation.setUserId(userId);
            userInfoGroupRelationModels.add(relation);
        }

        userInfoGroupRelationModelMapper.insertRelationList(userInfoGroupRelationModels);
        List<Long> ids = new ArrayList<>();
        for (UserInfoGroupRelationModel userInfoGroupRelationModel : userInfoGroupRelationModels){
            ids.add(userInfoGroupRelationModel.getRelationId());
        }
        return ids;
    }

    public boolean batchUpdateUserInfoGroupRelation(Long userId, List<Long> groupIds) throws BizException {
        BizAssert.notNull(userId, "用户id不能为空！");
        BizAssert.notNull(groupIds, "用户组id不能为空！");
        UserInfoModel userInfoModel = userInfoModelMapper.selectByPrimaryKey(userId);
        BizAssert.notNull(userInfoModel, "用户id不存在！");
        if(groupIds.isEmpty()){
            return userInfoGroupRelationModelMapper.deleteByUserId(userId) > 0;
        } else {
            List<Long> dbGroupIds = userInfoGroupRelationModelMapper.getGroupIdsByUserId(userId);
            List<Long> deleteGroupIds = ListUtils.removeAll(dbGroupIds, groupIds);
            if(!deleteGroupIds.isEmpty()){
                userInfoGroupRelationModelMapper.deleteByUserId(userId);
                batchAddUserInfoGroupRelation(userId, groupIds);
            } else {
                List<Long> addGroupIds = ListUtils.removeAll(groupIds,dbGroupIds);
                if(!addGroupIds.isEmpty()){
                    batchAddUserInfoGroupRelation(userId, addGroupIds);
                }
            }
        }
        return true;
    }

    public List<Long> batchAddGroupUserInfoRelation(Long groupId, List<Long> userIds) throws BizException {
        BizAssert.notNull(groupId, "用户组id不能为空！");
        BizAssert.notEmpty(userIds, "用户id不能为空！");
        GroupModel groupModel = groupModelMapper.selectByPrimaryKey(groupId);
        BizAssert.notNull(groupModel, String.format("用户id【%s】不存在！",groupId));
        List<Long> dbUserIds = userInfoModelMapper.getUserIdsByUserIds(userIds);
        BizAssert.isTrue(userIds.size() == dbUserIds.size(), "含有不存在的用户组id");


        List<UserInfoGroupRelationModel> userInfoGroupRelationModels = new ArrayList<>();
        for(Long userId : userIds){
            UserInfoGroupRelationModel relation = new UserInfoGroupRelationModel();
            relation.setGroupId(groupId);
            relation.setUserId(userId);
            userInfoGroupRelationModels.add(relation);
        }

        userInfoGroupRelationModelMapper.insertRelationList(userInfoGroupRelationModels);
        List<Long> ids = new ArrayList<>();
        for (UserInfoGroupRelationModel userInfoGroupRelationModel : userInfoGroupRelationModels){
            ids.add(userInfoGroupRelationModel.getRelationId());
        }
        return ids;
    }

    public boolean batchUpdateGroupUserInfoRelation(Long groupId, List<Long> userIds) throws BizException {
        BizAssert.notNull(groupId, "用户组id不能为空！");
        BizAssert.notNull(userIds, "用户id不能为空！");
        GroupModel groupModel = groupModelMapper.selectByPrimaryKey(groupId);
        BizAssert.notNull(groupModel, "用户组id不存在！");
        if(userIds.isEmpty()){
            return userInfoGroupRelationModelMapper.deleteByGroupId(groupId) > 0;
        } else {
            List<Long> dbUserIds = userInfoGroupRelationModelMapper.getUserIdsByGroupId(groupId);
            List<Long> deleteUserIds = ListUtils.removeAll(dbUserIds, userIds);
            if(!deleteUserIds.isEmpty()){
                userInfoGroupRelationModelMapper.deleteByGroupId(groupId);
                batchAddGroupUserInfoRelation(groupId, userIds);
            } else {
                List<Long> addUserIds = ListUtils.removeAll(userIds,dbUserIds);
                if(!addUserIds.isEmpty()){
                    batchAddGroupUserInfoRelation(groupId, addUserIds);
                }
            }
        }
        return true;
    }

}
