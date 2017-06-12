package com.shsnc.base.authorization.service;

import com.shsnc.base.authorization.bean.AuthorizationRole;
import com.shsnc.base.authorization.mapper.*;
import com.shsnc.base.authorization.model.AuthorizationRoleRelationModel;
import com.shsnc.base.authorization.model.AuthorizationUserRoleRelationModel;
import com.shsnc.base.util.config.BizException;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Elena on 2017/6/7.
 */
@Service
public class AuthorizationRoleRelationService {

    @Autowired
    private AuthorizationRoleRelationModelMapper authorizationRoleRelationModelMapper;

    @Autowired
    private AuthorizationUserRoleRelationModelMapper authorizationUserRoleRelationModelMapper;

    @Autowired
    private AuthorizationGroupRoleRelationModelMapper authorizationGroupRoleRelationModelMapper;

    @Autowired
    private AuthorizationInfoModelMapper authorizationInfoModelMapper;

    @Autowired
    private AuthorizationRoleModelMapper authorizationRoleModelMapper;

    /**
     * 批量插入数据返回 插入条数
     *
     * @param roleId
     * @param authorizationIdList
     * @return
     */
    @Transactional
    public boolean roleBatchAuthorization(Long roleId, List<Long> authorizationIdList) throws BizException {
        if (roleId == null) {
            throw new BizException("选择授权的角色");
        }
        if (authorizationRoleModelMapper.getAuthorizationRoleModelByRoleId(roleId) == null) {
            throw new BizException("无效角色");
        }
        if (CollectionUtils.isEmpty(authorizationIdList)) {
            throw new BizException("选择授权的权限");
        }
        //删除该角色的所有权限 ，然后重新添加该角色的权限
        authorizationRoleRelationModelMapper.deleteAuthorizationRoleRelationModelByRoleId(roleId);

        List<AuthorizationRoleRelationModel> authorizationRoleRelationModels = new ArrayList<>();
        for (int i = 0; i < authorizationIdList.size(); i++) {
            Long authorizationId = authorizationIdList.get(i);
            if (authorizationInfoModelMapper.getAuthorizationByAuthorizationId(authorizationId) == null) {
                throw new BizException("无效权限数据");
            }
            AuthorizationRoleRelationModel authorizationRoleRelationModel = new AuthorizationRoleRelationModel();
            authorizationRoleRelationModel.setAuthorizationId(authorizationId);
            authorizationRoleRelationModel.setRoleId(roleId);
            authorizationRoleRelationModels.add(authorizationRoleRelationModel);
        }
        return authorizationRoleRelationModelMapper.batchAddAuthorizationRoleRelationModel(authorizationRoleRelationModels) > 0;
    }

    /**
     * 根据角色ID 获取 角色所有权限
     *
     * @param roleId
     * @return
     */
    public List<Integer> getAuthorizationIdByRoleId(Long roleId) {
        return authorizationRoleRelationModelMapper.getAuthorizationIdByRoleId(roleId);
    }

    /**
     * 根据用户ID 获取 用户拥有的所有权限
     *
     * @param userId
     * @return
     */
    public List<Long> getAuthorizationIdByUserId(Long userId) throws BizException {
        if (userId == null) {
            throw new BizException("选择用户");
        }
        //return authorizationRoleRelationModelMapper.getAuthorizationIdByRoleId(roleId);
        Set<Long> roleIds = new HashSet<>();//角色列表

        //获取用户角色列表
        List<Long> userRoleIds = authorizationUserRoleRelationModelMapper.getRoleIdByUserId(userId);
        userRoleIds.forEach(roleId -> {
            roleIds.add(roleId);
        });
        //TODO 根据当前用户获取所属 组
        List<Long> groupIds = new ArrayList<>();//组列表

        if (!CollectionUtils.isEmpty(groupIds)) {
            List<Long> groupRoleIds = authorizationGroupRoleRelationModelMapper.getRoleIdByGroupIds(groupIds);
            groupRoleIds.forEach(roleId -> {
                roleIds.add(roleId);
            });
        }

        if (!CollectionUtils.isEmpty(roleIds)) {
            List<Long> tempList = new ArrayList<Long>();
            tempList.addAll(roleIds);
            return authorizationRoleRelationModelMapper.getAuthorizationIdByRoleIds(tempList);
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * 验证用户是否拥有该权限
     *
     * @param userId
     * @param authorizationId
     * @return
     */
    public boolean userHaveAuthorization(Long userId, Long authorizationId) throws BizException {
        if (authorizationId == null) {
            throw new BizException("选择权限");
        }
        List<Long> authorizationIdList = getAuthorizationIdByUserId(userId);
        return authorizationIdList.contains(authorizationId);
    }
}
