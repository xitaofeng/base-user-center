package com.shsnc.base.user.service;

import com.shsnc.base.user.mapper.GroupModelMapper;
import com.shsnc.base.user.mapper.UserInfoGroupRelationModelMapper;
import com.shsnc.base.user.mapper.UserInfoModelMapper;
import com.shsnc.base.user.model.UserInfoGroupRelationModel;
import com.shsnc.base.user.model.UserInfoModel;
import com.shsnc.base.user.support.Assert;
import com.shsnc.base.util.config.BizException;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by houguangqiang on 2017/6/8.
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
        Assert.notNull(userId,"用户id不能为空！");
        Assert.notNull(groupIds, "组id不能为空！");
        UserInfoModel userInfoModel = userInfoModelMapper.selectByPrimaryKey(userId);
        Assert.notNull(userInfoModel, "用户id不存在！");
        List<Long> dbGroupIds = groupModelMapper.getGroupIdsByGroupIds(groupIds);
        Assert.isTrue(dbGroupIds.size() == groupIds.size(), "含有不存在的用户组id！");

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
        Assert.notNull(userId, "用户id不能为空！");
        Assert.notNull(groupIds);
        UserInfoModel userInfoModel = userInfoModelMapper.selectByPrimaryKey(userId);
        Assert.notNull(userInfoModel, "用户id不存在！");
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
                    batchAddUserInfoGroupRelation(userId, addGroupIds).size();
                }
            }
        }
        return true;
    }
}
