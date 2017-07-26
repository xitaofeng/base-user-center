package com.shsnc.base.user.service;

import com.shsnc.base.user.mapper.OrganizationModelMapper;
import com.shsnc.base.user.mapper.UserInfoOrganizationRelationModelMapper;
import com.shsnc.base.user.mapper.UserInfoModelMapper;
import com.shsnc.base.user.model.UserInfoOrganizationRelationModel;
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
public class UserInfoOrganizationRelationService {

    @Autowired
    private UserInfoOrganizationRelationModelMapper userInfoOrganizationRelationModelMapper;
    @Autowired
    private UserInfoModelMapper userInfoModelMapper;
    @Autowired
    private OrganizationModelMapper organizationModelMapper;

    public List<Long> batchAddUserInfoOrganizationRelation(Long userId, List<Long> organizationIds) throws BizException {
        Assert.notNull(userId,"用户id不能为空！");
        Assert.notNull(organizationIds, "组织机构id不能为空！");
        UserInfoModel userInfoModel = userInfoModelMapper.selectByPrimaryKey(userId);
        Assert.notNull(userInfoModel, "用户id不存在！");
        List<Long> dbOrganizationIds = organizationModelMapper.getOrganizationIdsByOrganizationIds(organizationIds);
        Assert.isTrue(dbOrganizationIds.size() == organizationIds.size(), "含有不存在的用户组id！");

        List<UserInfoOrganizationRelationModel> userInfoOrganizationRelationModels = new ArrayList<>();
        for(Long organizationId : organizationIds){
            UserInfoOrganizationRelationModel relation = new UserInfoOrganizationRelationModel();
            relation.setOrganizationId(organizationId);
            relation.setUserId(userId);
            userInfoOrganizationRelationModels.add(relation);
        }

        userInfoOrganizationRelationModelMapper.insertRelationList(userInfoOrganizationRelationModels);
        List<Long> ids = new ArrayList<>();
        for (UserInfoOrganizationRelationModel userInfoOrganizationRelationModel : userInfoOrganizationRelationModels){
            ids.add(userInfoOrganizationRelationModel.getRelationId());
        }
        return ids;
    }

    public boolean batchUpdateUserInfoOrganizationRelation(Long userId, List<Long> organizationIds) throws BizException {
        Assert.notNull(userId, "用户id不能为空！");
        Assert.notNull(organizationIds);
        UserInfoModel userInfoModel = userInfoModelMapper.selectByPrimaryKey(userId);
        Assert.notNull(userInfoModel, "用户id不存在！");
        if(organizationIds.isEmpty()){
            return userInfoOrganizationRelationModelMapper.deleteByUserId(userId) > 0;
        } else {
            List<Long> dbOrganizationIds = userInfoOrganizationRelationModelMapper.getOrganizationIdsByUserId(userId);
            List<Long> deleteOrganizationIds = ListUtils.removeAll(dbOrganizationIds, organizationIds);
            if(!deleteOrganizationIds.isEmpty()){
                userInfoOrganizationRelationModelMapper.deleteByUserId(userId);
                batchAddUserInfoOrganizationRelation(userId, organizationIds);
            } else {
                List<Long> addOrganizationIds = ListUtils.removeAll(organizationIds,dbOrganizationIds);
                if(!addOrganizationIds.isEmpty()){
                    batchAddUserInfoOrganizationRelation(userId, addOrganizationIds).size();
                }
            }
        }
        return true;
    }
}
