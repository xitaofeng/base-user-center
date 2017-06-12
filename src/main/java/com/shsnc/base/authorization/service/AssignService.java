package com.shsnc.base.authorization.service;

import com.shsnc.base.authorization.mapper.AuthorizationGroupRoleRelationModelMapper;
import com.shsnc.base.authorization.mapper.AuthorizationRoleModelMapper;
import com.shsnc.base.authorization.mapper.AuthorizationUserRoleRelationModelMapper;
import com.shsnc.base.authorization.model.AuthorizationGroupRoleRelationModel;
import com.shsnc.base.authorization.model.AuthorizationRoleRelationModel;
import com.shsnc.base.authorization.model.AuthorizationUserRoleRelationModel;
import com.shsnc.base.user.mapper.GroupStructureModelMapper;
import com.shsnc.base.user.mapper.UserInfoModelMapper;
import com.shsnc.base.util.config.BizException;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * 分配Service
 * Created by Elena on 2017/6/9.
 */
@Service
public class AssignService {

    @Autowired
    private AuthorizationRoleModelMapper authorizationRoleModelMapper;

    @Autowired
    private UserInfoModelMapper userInfoModelMapper;

    @Autowired
    private GroupStructureModelMapper groupStructureModelMapper;

    @Autowired
    private AuthorizationUserRoleRelationModelMapper authorizationUserRoleRelationModelMapper;

    @Autowired
    private AuthorizationGroupRoleRelationModelMapper authorizationGroupRoleRelationModelMapper;

    /**
     * 用户分配角色
     *
     * @param userId
     * @param roleIdList
     * @return
     */
    @Transactional
    public boolean userAssignRole(@NotNull Long userId, @NotEmpty List<Long> roleIdList) throws BizException {
        if (userId == null) {
            throw new BizException("选择授权的用户");
        }
        if (userInfoModelMapper.selectByPrimaryKey(userId) == null) {
            throw new BizException("无效用户");
        }
        if (CollectionUtils.isEmpty(roleIdList)) {
            throw new BizException("选择分配的角色");
        }
        //删除该角色的所有权限 ，然后重新添加该角色的权限
        authorizationUserRoleRelationModelMapper.deleteAuthorizationUserRoleRelationModelByUserId(userId);

        List<AuthorizationUserRoleRelationModel> authorizationUserRoleRelationModels = new ArrayList<>();
        for (int i = 0; i < roleIdList.size(); i++) {
            Long roleId = roleIdList.get(i);
            if (authorizationRoleModelMapper.getByRoleId(roleId) == null) {
                throw new BizException("无效角色数据");
            }
            AuthorizationUserRoleRelationModel authorizationUserRoleRelationModel = new AuthorizationUserRoleRelationModel();
            authorizationUserRoleRelationModel.setUserId(userId);
            authorizationUserRoleRelationModel.setRoleId(roleId);
            authorizationUserRoleRelationModels.add(authorizationUserRoleRelationModel);
        }
        return authorizationUserRoleRelationModelMapper.batchAddAuthorizationUserRoleRelationModel(authorizationUserRoleRelationModels) > 0;
    }

    /**
     * 角色分配用户
     *
     * @param roleId
     * @param userIdList
     * @return
     */
    @Transactional
    public boolean roleAssignUser(@NotNull Long roleId, @NotEmpty List<Long> userIdList) throws BizException {
        if (roleId == null) {
            throw new BizException("选择授权的角色");
        }
        if (authorizationRoleModelMapper.getAuthorizationRoleModelByRoleId(roleId) == null) {
            throw new BizException("无效角色");
        }
        if (CollectionUtils.isEmpty(userIdList)) {
            throw new BizException("选择分配的用户");
        }
        //删除该角色的所有权限 ，然后重新添加该角色的权限
        authorizationUserRoleRelationModelMapper.deleteAuthorizationUserRoleRelationModelByRoleId(roleId);

        List<AuthorizationUserRoleRelationModel> authorizationUserRoleRelationModels = new ArrayList<>();
        for (int i = 0; i < userIdList.size(); i++) {
            Long userId = userIdList.get(i);
            if (userInfoModelMapper.selectByPrimaryKey(userId) == null) {
                throw new BizException("无效用户数据");
            }
            AuthorizationUserRoleRelationModel authorizationUserRoleRelationModel = new AuthorizationUserRoleRelationModel();
            authorizationUserRoleRelationModel.setUserId(userId);
            authorizationUserRoleRelationModel.setRoleId(roleId);
            authorizationUserRoleRelationModels.add(authorizationUserRoleRelationModel);
        }
        return authorizationUserRoleRelationModelMapper.batchAddAuthorizationUserRoleRelationModel(authorizationUserRoleRelationModels) > 0;
    }

    /**
     * 组分配角色
     *
     * @param groupId
     * @param roleIdList
     * @return
     */
    @Transactional
    public boolean groupAssignRole(@NotNull Long groupId, @NotEmpty List<Long> roleIdList) throws BizException {
        if (groupId == null) {
            throw new BizException("选择分配的用户组");
        }
        if (groupStructureModelMapper.selectByPrimaryKey(groupId) == null) {
            throw new BizException("无效组织架构");
        }
        if (CollectionUtils.isEmpty(roleIdList)) {
            throw new BizException("选择分配的用户");
        }
        //删除该角色的所有权限 ，然后重新添加该角色的权限
        authorizationGroupRoleRelationModelMapper.deleteAuthorizationGroupRoleRelationModelByGroupId(groupId);

        List<AuthorizationGroupRoleRelationModel> authorizationGroupRoleRelationModels = new ArrayList<>();
        for (int i = 0; i < roleIdList.size(); i++) {
            Long roleId = roleIdList.get(i);
            if (authorizationRoleModelMapper.getByRoleId(roleId) == null) {
                throw new BizException("无效角色数据");
            }
            AuthorizationGroupRoleRelationModel authorizationGroupRoleRelationModel = new AuthorizationGroupRoleRelationModel();
            authorizationGroupRoleRelationModel.setGroupId(groupId);
            authorizationGroupRoleRelationModel.setRoleId(roleId);
            authorizationGroupRoleRelationModels.add(authorizationGroupRoleRelationModel);
        }
        return authorizationGroupRoleRelationModelMapper.batchAddAuthorizationGroupRoleRelationModel(authorizationGroupRoleRelationModels) > 0;
    }

    /**
     * 角色分配组
     *
     * @param roleId
     * @param groupIdList
     * @return
     */
    @Transactional
    public boolean roleAssignGroup(@NotNull Long roleId, @NotEmpty List<Long> groupIdList) throws BizException {
        if (roleId == null) {
            throw new BizException("选择分配的角色");
        }
        if (authorizationRoleModelMapper.getAuthorizationRoleModelByRoleId(roleId) == null) {
            throw new BizException("无效角色");
        }
        if (CollectionUtils.isEmpty(groupIdList)) {
            throw new BizException("选择分配的用户");
        }
        //删除该角色的所有权限 ，然后重新添加该角色的权限
        authorizationGroupRoleRelationModelMapper.deleteAuthorizationGroupRoleRelationModelByRoleId(roleId);

        List<AuthorizationGroupRoleRelationModel> authorizationGroupRoleRelationModels = new ArrayList<>();
        for (int i = 0; i < groupIdList.size(); i++) {
            Long groupId = groupIdList.get(i);
            if (groupStructureModelMapper.selectByPrimaryKey(groupId) == null) {
                throw new BizException("无效组织架构");
            }
            AuthorizationGroupRoleRelationModel authorizationGroupRoleRelationModel = new AuthorizationGroupRoleRelationModel();
            authorizationGroupRoleRelationModel.setGroupId(groupId);
            authorizationGroupRoleRelationModel.setRoleId(roleId);
            authorizationGroupRoleRelationModels.add(authorizationGroupRoleRelationModel);
        }
        return authorizationGroupRoleRelationModelMapper.batchAddAuthorizationGroupRoleRelationModel(authorizationGroupRoleRelationModels) > 0;
    }
}
