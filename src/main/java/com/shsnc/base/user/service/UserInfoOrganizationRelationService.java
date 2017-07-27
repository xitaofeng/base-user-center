package com.shsnc.base.user.service;

import com.shsnc.base.user.mapper.OrganizationModelMapper;
import com.shsnc.base.user.mapper.UserInfoOrganizationRelationModelMapper;
import com.shsnc.base.user.mapper.UserInfoModelMapper;
import com.shsnc.base.user.model.OrganizationModel;
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

    public Long addUserInfoOrganizationRelation(Long userId, Long organizationId) throws BizException {
        Assert.notNull(userId,"用户id不能为空！");
        Assert.notNull(organizationId, "组织部门id不能为空！");
        UserInfoModel userInfoModel = userInfoModelMapper.selectByPrimaryKey(userId);
        Assert.notNull(userInfoModel, "用户id不存在！");
        OrganizationModel organizationModel = organizationModelMapper.selectByPrimaryKey(organizationId);
        Assert.notNull(organizationModel, "含有不存在的组织部门id！");

        UserInfoOrganizationRelationModel userInfoOrganizationRelationModel = new UserInfoOrganizationRelationModel();
        userInfoOrganizationRelationModel.setOrganizationId(organizationId);
        userInfoOrganizationRelationModel.setUserId(userId);
        userInfoOrganizationRelationModelMapper.insert(userInfoOrganizationRelationModel);

        return userInfoOrganizationRelationModel.getRelationId();
    }

    public boolean updateUserInfoOrganizationRelation(Long userId, Long organizationId) throws BizException {
        Assert.notNull(userId, "用户id不能为空！");
        Assert.notNull(organizationId, "组织部门id不能为空！");
        UserInfoModel userInfoModel = userInfoModelMapper.selectByPrimaryKey(userId);
        Assert.notNull(userInfoModel, "用户不存在！");
        UserInfoOrganizationRelationModel userInfoOrganizationRelationModel = userInfoOrganizationRelationModelMapper.getByUserId(userId);
        if (userInfoOrganizationRelationModel != null) {
            if (organizationId.equals(userInfoOrganizationRelationModel.getOrganizationId())) {
                return true;
            }
            userInfoOrganizationRelationModelMapper.deleteByUserId(userId);
        }
        this.addUserInfoOrganizationRelation(userId, organizationId);
        return true;
    }
}
