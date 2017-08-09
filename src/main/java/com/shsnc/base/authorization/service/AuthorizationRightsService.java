package com.shsnc.base.authorization.service;

import com.shsnc.base.authorization.config.DataAuthorization;
import com.shsnc.base.authorization.config.DataOperation;
import com.shsnc.base.authorization.mapper.AuthorizationRightsModelMapper;
import com.shsnc.base.authorization.model.AuthorizationRightsModel;
import com.shsnc.base.user.mapper.GroupModelMapper;
import com.shsnc.base.user.mapper.UserInfoGroupRelationModelMapper;
import com.shsnc.base.user.model.GroupModel;
import com.shsnc.base.user.service.GroupService;
import com.shsnc.base.util.BizAssert;
import com.shsnc.base.util.config.BizException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author houguangqiang
 * @date 2017-08-08
 * @since 1.0
 */
@Service
public class AuthorizationRightsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizationRightsService.class);

    @Autowired
    private AuthorizationRightsModelMapper authorizationRightsModelMapper;
    @Autowired
    private GroupService groupService;
    @Autowired
    private GroupModelMapper groupModelMapper;
    @Autowired
    private UserInfoGroupRelationModelMapper userInfoGroupRelationModelMapper;

    /**
     * 更新对象授权
     * @param dataAuthorization 对象类型
     * @param objectId 对象id
     * @param groupIds 用户组id列表
     * @param dataOperation 操作类型
     */
    @Transactional(rollbackFor = Exception.class)
    public void authorize(DataAuthorization dataAuthorization, Long objectId, List<Long> groupIds, DataOperation dataOperation) throws BizException {
        authorize(dataAuthorization, objectId, groupIds, dataOperation.getValue());
    }

    /**
     * 更新对象授权
     * @param dataAuthorization 对象类型
     * @param objectId 对象id
     * @param groupIds 用户组id列表
     * @param permission 权限值
     */
    @Transactional(rollbackFor = Exception.class)
    public void authorize(DataAuthorization dataAuthorization, Long objectId, List<Long> groupIds, int permission) throws BizException {
        BizAssert.notNull(dataAuthorization, "对象类型不能为空！");
        BizAssert.notNull(objectId, String.format("【%s】的id不能为空！", dataAuthorization.getDescription()));
        BizAssert.notNull(groupIds, "用户组id不能为空！");

        authorizationRightsModelMapper.deleteByObjectId(objectId, dataAuthorization);
        if (!groupIds.isEmpty()) {
            List<GroupModel> dbGroupModels = groupModelMapper.getByGroupIds(groupIds);
            List<AuthorizationRightsModel> authorizationRightsModels = new ArrayList<>();
            for (GroupModel dbGroupModel : dbGroupModels) {
                AuthorizationRightsModel authorizationRightsModel = new AuthorizationRightsModel();
                authorizationRightsModel.setGroupId(dbGroupModel.getGroupId());
                authorizationRightsModel.setObjectId(objectId);
                authorizationRightsModel.setObjectType(dataAuthorization.getType());
                authorizationRightsModel.setObjectTypeCode(dataAuthorization.toString());
                authorizationRightsModel.setPermission(permission);
                authorizationRightsModels.add(authorizationRightsModel);
            }
            if (!authorizationRightsModels.isEmpty()) {
                authorizationRightsModelMapper.insertList(authorizationRightsModels);
            }
        }
    }

    /**
     * 更新对象授权
     * @param dataAuthorization 对象类型
     * @param objectIds 对象id列表
     * @param groupId 用户组id
     * @param dataOperation 操作类型
     */
    @Transactional(rollbackFor = Exception.class)
    public void authorize(DataAuthorization dataAuthorization, List<Long> objectIds, Long groupId, DataOperation dataOperation) throws BizException {
        authorize(dataAuthorization, objectIds, groupId, dataOperation.getValue());
    }

    /**
     * 更新对象授权
     * @param dataAuthorization 对象类型
     * @param objectIds 对象id列表
     * @param groupId 用户组id
     * @param permission 权限值
     */
    @Transactional(rollbackFor = Exception.class)
    public void authorize(DataAuthorization dataAuthorization, List<Long> objectIds, Long groupId, int permission) throws BizException {
        BizAssert.notNull(dataAuthorization, "对象类型不能为空！");
        BizAssert.notEmpty(objectIds, String.format("【%s】的id不能为空！", dataAuthorization.getDescription()));
        BizAssert.notNull(groupId, "用户组id不能为空！");
        authorizationRightsModelMapper.deleteByGroupId(groupId, dataAuthorization);
        GroupModel dbGroupModel = groupModelMapper.selectByPrimaryKey(groupId);
        BizAssert.notNull(dbGroupModel, String.format("用户组id【%s】不存在！",groupId));
        List<AuthorizationRightsModel> authorizationRightsModels = new ArrayList<>();
        for (Long objectId : objectIds) {
            AuthorizationRightsModel authorizationRightsModel = new AuthorizationRightsModel();
            authorizationRightsModel.setGroupId(groupId);
            authorizationRightsModel.setObjectId(objectId);
            authorizationRightsModel.setObjectType(dataAuthorization.getType());
            authorizationRightsModel.setObjectTypeCode(dataAuthorization.toString());
            authorizationRightsModel.setPermission(permission);
            authorizationRightsModels.add(authorizationRightsModel);
        }
        if (!authorizationRightsModels.isEmpty()) {
            authorizationRightsModelMapper.insertList(authorizationRightsModels);
        }
    }

    public List<AuthorizationRightsModel> getRightsByUserId(DataAuthorization dataAuthorization, Long userId) throws BizException {
        BizAssert.notNull(dataAuthorization, "对象类型不能为空！");
        BizAssert.notNull(userId, "用户id不能为空！");
        List<Long> groupIds = userInfoGroupRelationModelMapper.getGroupIdsByUserId(userId);
        if (!groupIds.isEmpty()) {
            return authorizationRightsModelMapper.getByGroupIds(groupIds, dataAuthorization);
        }
        return new ArrayList<>();
    }

    public List<AuthorizationRightsModel> getRightsByObjectIds(DataAuthorization dataAuthorization, List<Long> objectIds) throws BizException {
        BizAssert.notNull(dataAuthorization, "对象类型不能为空！");
        BizAssert.notEmpty(objectIds, String.format("【%s】的id不能为空！", dataAuthorization.getDescription()));
        return authorizationRightsModelMapper.getByObjectIds(objectIds, dataAuthorization);
    }

    public List<AuthorizationRightsModel> getRightsByGroupIds(DataAuthorization dataAuthorization, List<Long> groupIds) throws BizException {
        BizAssert.notNull(dataAuthorization, "对象类型不能为空！");
        BizAssert.notEmpty(groupIds, "用户组id不能为空！");
        return authorizationRightsModelMapper.getByGroupIds(groupIds, dataAuthorization);
    }

    /**
     * 获取某个用户拥有的某类数据的所有id
     * @param dataAuthorization 对象类型
     * @param userId 用户id
     * @return 对象id列表
     */
    public List<Long> getObjectIdsByUserId(DataAuthorization dataAuthorization, Long userId) throws BizException {
        BizAssert.notNull(dataAuthorization, "对象类型不能为空！");
        BizAssert.notNull(userId, "用户id不能为空！");
        List<Long> groupIds = userInfoGroupRelationModelMapper.getGroupIdsByUserId(userId);
        if (!groupIds.isEmpty()) {
            return authorizationRightsModelMapper.getObjectIdsByGroupIds(groupIds, dataAuthorization);
        }
        return new ArrayList<>();
    }

    /**
     * 获取某个对象的权限值
     * @param dataAuthorization 对象类型
     * @param objectId 对象id
     * @return 权限值
     */
    public int getPermissionByObjectId(DataAuthorization dataAuthorization, Long objectId) throws BizException {
        BizAssert.notNull(dataAuthorization, "对象类型不能为空！");
        BizAssert.notNull(objectId, String.format("【%s】的id不能为空！", dataAuthorization.getDescription()));
        return authorizationRightsModelMapper.getPermissionByObjectId(objectId, dataAuthorization);
    }

    /**
     * 移除多个对象的权限值
     * @param dataAuthorization 对象类型
     * @param objectIds 对象id的列表
     */
    public void deleteByObjectIds(DataAuthorization dataAuthorization, List<Long> objectIds) throws BizException {
        BizAssert.notNull(dataAuthorization, "对象类型不能为空！");
        BizAssert.notEmpty(objectIds, String.format("【%s】的id不能为空！", dataAuthorization.getDescription()));
        authorizationRightsModelMapper.deleteByObjectIds(objectIds, dataAuthorization);
    }

    /**
     * 移除某个对象的权限值
     * @param dataAuthorization 对象类型
     * @param objectId 对象id
     */
    public void deleteByObjectId(DataAuthorization dataAuthorization, Long objectId) throws BizException {
        BizAssert.notNull(dataAuthorization, "对象类型不能为空！");
        BizAssert.notNull(objectId, String.format("【%s】的id不能为空！", dataAuthorization.getDescription()));
        authorizationRightsModelMapper.deleteByObjectId(objectId, dataAuthorization);
    }

    /**
     * 清空某个用户组相关数据权限
     * @param groupId 用户组
     */
    @Transactional(rollbackFor = Exception.class)
    public void clearByGroupId(Long groupId) throws BizException {
        BizAssert.notNull(groupId, "用户组id不能为空！");
        authorizationRightsModelMapper.clearByGroupId(groupId);
    }

    /**
     * 清空多个用户组相关数据权限
     * @param groupIds 用户组id的列表
     */
    @Transactional(rollbackFor = Exception.class)
    public void clearByGroupIds(List<Long> groupIds) throws BizException {
        BizAssert.notEmpty(groupIds, "用户组id不能为空！");
        authorizationRightsModelMapper.clearByGroupIds(groupIds);
    }

}
