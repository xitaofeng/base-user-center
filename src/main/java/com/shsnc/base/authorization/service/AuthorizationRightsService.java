package com.shsnc.base.authorization.service;

import com.shsnc.base.authorization.config.DataObject;
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
     * @param dataObject 对象类型
     * @param objectId 对象id
     * @param groupIds 用户组id列表
     * @param dataOperation 操作类型
     */
    @Transactional(rollbackFor = Exception.class)
    public void authorize(DataObject dataObject, Long objectId, List<Long> groupIds, DataOperation dataOperation) throws BizException {
        authorize(dataObject, objectId, groupIds, dataOperation.getValue());
    }

    /**
     * 更新对象授权
     * @param dataObject 对象类型
     * @param objectId 对象id
     * @param groupIds 用户组id列表
     * @param permission 权限值
     */
    @Transactional(rollbackFor = Exception.class)
    public void authorize(DataObject dataObject, Long objectId, List<Long> groupIds, int permission) throws BizException {
        BizAssert.notNull(dataObject, "对象类型不能为空！");
        BizAssert.notNull(objectId, String.format("【%s】的id不能为空！", dataObject.getDescription()));
        BizAssert.notNull(groupIds, "用户组id不能为空！");

        authorizationRightsModelMapper.deleteByObjectId(objectId, dataObject);
        if (!groupIds.isEmpty()) {
            List<GroupModel> dbGroupModels = groupModelMapper.getByGroupIds(groupIds);
            List<AuthorizationRightsModel> authorizationRightsModels = new ArrayList<>();
            for (GroupModel dbGroupModel : dbGroupModels) {
                AuthorizationRightsModel authorizationRightsModel = new AuthorizationRightsModel();
                authorizationRightsModel.setGroupId(dbGroupModel.getGroupId());
                authorizationRightsModel.setObjectId(objectId);
                authorizationRightsModel.setObjectType(dataObject.getType());
                authorizationRightsModel.setObjectTypeCode(dataObject.toString());
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
     * @param dataObject 对象类型
     * @param objectIds 对象id列表
     * @param groupId 用户组id
     * @param dataOperation 操作类型
     */
    @Transactional(rollbackFor = Exception.class)
    public void authorize(DataObject dataObject, List<Long> objectIds, Long groupId, DataOperation dataOperation) throws BizException {
        authorize(dataObject, objectIds, groupId, dataOperation.getValue());
    }

    /**
     * 更新对象授权
     * @param dataObject 对象类型
     * @param objectIds 对象id列表
     * @param groupId 用户组id
     * @param permission 权限值
     */
    @Transactional(rollbackFor = Exception.class)
    public void authorize(DataObject dataObject, List<Long> objectIds, Long groupId, int permission) throws BizException {
        BizAssert.notNull(dataObject, "对象类型不能为空！");
        BizAssert.notEmpty(objectIds, String.format("【%s】的id不能为空！", dataObject.getDescription()));
        BizAssert.notNull(groupId, "用户组id不能为空！");
        authorizationRightsModelMapper.deleteByGroupId(groupId, dataObject);
        GroupModel dbGroupModel = groupModelMapper.selectByPrimaryKey(groupId);
        BizAssert.notNull(dbGroupModel, String.format("用户组id【%s】不存在！",groupId));
        List<AuthorizationRightsModel> authorizationRightsModels = new ArrayList<>();
        for (Long objectId : objectIds) {
            AuthorizationRightsModel authorizationRightsModel = new AuthorizationRightsModel();
            authorizationRightsModel.setGroupId(groupId);
            authorizationRightsModel.setObjectId(objectId);
            authorizationRightsModel.setObjectType(dataObject.getType());
            authorizationRightsModel.setObjectTypeCode(dataObject.toString());
            authorizationRightsModel.setPermission(permission);
            authorizationRightsModels.add(authorizationRightsModel);
        }
        if (!authorizationRightsModels.isEmpty()) {
            authorizationRightsModelMapper.insertList(authorizationRightsModels);
        }
    }

    public List<AuthorizationRightsModel> getRightsByUserId(DataObject dataObject, Long userId) throws BizException {
        BizAssert.notNull(dataObject, "对象类型不能为空！");
        BizAssert.notNull(userId, "用户id不能为空！");
        List<Long> groupIds = userInfoGroupRelationModelMapper.getGroupIdsByUserId(userId);
        if (!groupIds.isEmpty()) {
            return authorizationRightsModelMapper.getByGroupIds(groupIds, dataObject);
        }
        return new ArrayList<>();
    }

    public List<AuthorizationRightsModel> getRightsByObjectIds(DataObject dataObject, List<Long> objectIds) throws BizException {
        BizAssert.notNull(dataObject, "对象类型不能为空！");
        BizAssert.notEmpty(objectIds, String.format("【%s】的id不能为空！", dataObject.getDescription()));
        return authorizationRightsModelMapper.getByObjectIds(objectIds, dataObject);
    }

    public List<AuthorizationRightsModel> getRightsByGroupIds(DataObject dataObject, List<Long> groupIds) throws BizException {
        BizAssert.notNull(dataObject, "对象类型不能为空！");
        BizAssert.notEmpty(groupIds, "用户组id不能为空！");
        return authorizationRightsModelMapper.getByGroupIds(groupIds, dataObject);
    }

    /**
     * 获取某个用户拥有的某类数据的所有id
     * @param dataObject 对象类型
     * @param userId 用户id
     * @return 对象id列表
     */
    public List<Long> getObjectIdsByUserId(DataObject dataObject, Long userId) throws BizException {
        BizAssert.notNull(dataObject, "对象类型不能为空！");
        BizAssert.notNull(userId, "用户id不能为空！");
        List<Long> groupIds = userInfoGroupRelationModelMapper.getGroupIdsByUserId(userId);
        if (!groupIds.isEmpty()) {
            return authorizationRightsModelMapper.getObjectIdsByGroupIds(groupIds, dataObject);
        }
        return new ArrayList<>();
    }

    /**
     * 获取某个对象的权限值
     * @param dataObject 对象类型
     * @param objectId 对象id
     * @return 权限值
     */
    public int getPermissionByObjectId(DataObject dataObject, Long objectId) throws BizException {
        BizAssert.notNull(dataObject, "对象类型不能为空！");
        BizAssert.notNull(objectId, String.format("【%s】的id不能为空！", dataObject.getDescription()));
        return authorizationRightsModelMapper.getPermissionByObjectId(objectId, dataObject);
    }

    /**
     * 移除多个对象的权限值
     * @param dataObject 对象类型
     * @param objectIds 对象id的列表
     */
    public void deleteByObjectIds(DataObject dataObject, List<Long> objectIds) throws BizException {
        BizAssert.notNull(dataObject, "对象类型不能为空！");
        BizAssert.notEmpty(objectIds, String.format("【%s】的id不能为空！", dataObject.getDescription()));
        authorizationRightsModelMapper.deleteByObjectIds(objectIds, dataObject);
    }

    /**
     * 移除某个对象的权限值
     * @param dataObject 对象类型
     * @param objectId 对象id
     */
    public void deleteByObjectId(DataObject dataObject, Long objectId) throws BizException {
        BizAssert.notNull(dataObject, "对象类型不能为空！");
        BizAssert.notNull(objectId, String.format("【%s】的id不能为空！", dataObject.getDescription()));
        authorizationRightsModelMapper.deleteByObjectId(objectId, dataObject);
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
