package com.shsnc.base.authorization.service;

import com.shsnc.api.core.ThreadContext;
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
import java.util.Set;
import java.util.stream.Collectors;

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

    private boolean checkGroupId(Long groupId) {
        if (!ThreadContext.getUserInfo().isSuperAdmin()) {
            List<Long> currentGroupIds = ThreadContext.getUserInfo().getGroupIds();
            return currentGroupIds.contains(groupId);
        }
        return true;
    }

    private boolean checkGroupIds(List<Long> groupIds) throws BizException {
        if (!ThreadContext.getUserInfo().isSuperAdmin()) {
            List<Long> currentGroupIds = ThreadContext.getUserInfo().getGroupIds();
            return currentGroupIds.containsAll(groupIds);
        }
        return true;
    }

    /**
     * 检查是否有某个对象的某项操作的权限
     * @param dataObject
     * @param objectId
     * @param dataOperation
     * @return
     */
    public boolean checkPermisson(DataObject dataObject, Long objectId, DataOperation dataOperation){
        return checkPermisson(dataObject, objectId, dataOperation.getValue());
    }

    /**
     * 检查是否有某个对象的某项操作的权限
     * @param dataObject
     * @param objectId
     * @param permission
     * @return
     */
    public boolean checkPermisson(DataObject dataObject, Long objectId, int permission){
        if (ThreadContext.getUserInfo().isSuperAdmin()) {
            return true;
        }
        List<Long> groupIds = ThreadContext.getUserInfo().getGroupIds();
        if (groupIds.isEmpty()) {
            return false;
        }
        List<AuthorizationRightsModel> authorizationRightsModels = authorizationRightsModelMapper.getByObjectId(objectId, dataObject, groupIds);
        boolean result = false;
        for (AuthorizationRightsModel authorizationRightsModel : authorizationRightsModels) {
            if ((authorizationRightsModel.getPermission() & permission) == permission) {
                result = true;
                break;
            }
        }
        return result;
    }

    /**
     * 检查是否有某些对象的某项操作的权限，只要其中一个没有权限就返回false
     * @param dataObject
     * @param objectIds
     * @param dataOperation
     * @return
     */
    public boolean checkPermisson(DataObject dataObject, List<Long> objectIds, DataOperation dataOperation){
        return checkPermisson(dataObject, objectIds, dataOperation.getValue());
    }

    /**
     * 检查是否有某些对象的某项操作的权限，只要其中一个没有权限就返回false
     * @param dataObject
     * @param objectIds
     * @param permission
     * @return
     */
    public boolean checkPermisson(DataObject dataObject, List<Long> objectIds, int permission){
        if (ThreadContext.getUserInfo().isSuperAdmin()) {
            return true;
        }
        List<Long> groupIds = ThreadContext.getUserInfo().getGroupIds();
        if (groupIds.isEmpty()) {
            return false;
        }
        List<AuthorizationRightsModel> authorizationRightsModels = authorizationRightsModelMapper.getByObjectIds(objectIds, dataObject, groupIds);
        Set<Long> allowedObjectIds = authorizationRightsModels.stream().map(AuthorizationRightsModel::getObjectId).collect(Collectors.toSet());
        for (AuthorizationRightsModel authorizationRightsModel : authorizationRightsModels) {
            if (allowedObjectIds.contains(authorizationRightsModel.getObjectId()) &&
                    (authorizationRightsModel.getPermission() & permission) == permission) {
                allowedObjectIds.remove(authorizationRightsModel.getObjectId());
            }
        }
        return allowedObjectIds.isEmpty();
    }

    private List<AuthorizationRightsModel> filterRights(List<AuthorizationRightsModel> rightsModelList, DataOperation dataOperation){
        List<AuthorizationRightsModel> result = new ArrayList<>();
        for (AuthorizationRightsModel authorizationRightsModel : rightsModelList) {
            if (DataOperation.hasOperation(authorizationRightsModel.getPermission(), dataOperation)) {
                result.add(authorizationRightsModel);
            }
        }
        return result;
    }

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
        if (!ThreadContext.getUserInfo().isSuperAdmin()) {
            BizAssert.isTrue(ThreadContext.getUserInfo().getGroupIds().contains(groupIds), String.format("没有用户组%s的操作权限",groupIds.toString()));
            BizAssert.isTrue(checkPermisson(dataObject, objectId, permission), "权限不足");
        }

        authorizationRightsModelMapper.deleteByObjectId(objectId, dataObject);
        if (!groupIds.isEmpty()) {
            BizAssert.isTrue(checkGroupIds(groupIds),"含有没有权限操作的用户组");
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
        if (!ThreadContext.getUserInfo().isSuperAdmin()) {
            BizAssert.isTrue(ThreadContext.getUserInfo().getGroupIds().contains(groupId), String.format("没有用户组【%s】的操作权限",groupId));
            BizAssert.isTrue(checkPermisson(dataObject, objectIds, permission), "权限不足");
        }

        authorizationRightsModelMapper.deleteByGroupId(groupId, dataObject);
        GroupModel dbGroupModel = groupModelMapper.selectByPrimaryKey(groupId);
        BizAssert.notNull(dbGroupModel, String.format("用户组id【%s】不存在！",groupId));
        BizAssert.isTrue(checkGroupId(groupId),"含有没有权限操作的用户组");
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

    /**
     * 获取用户拥有某项权限的数据
     * @param dataObject
     * @return
     * @throws BizException
     */
    public List<AuthorizationRightsModel> getRights(DataObject dataObject, DataOperation dataOperation) throws BizException {
        BizAssert.notNull(dataObject, "对象类型不能为空！");
        List<Long> groupIds = ThreadContext.getUserInfo().getGroupIds();
        if (!ThreadContext.getUserInfo().isSuperAdmin()
                && !groupIds.isEmpty()) {
            List<AuthorizationRightsModel> rightsModelList = authorizationRightsModelMapper.getByGroupIds(groupIds, dataObject);
            return filterRights(rightsModelList, dataOperation);
        }
        return new ArrayList<>();
    }

    public List<AuthorizationRightsModel> getRightsByObjectIds(List<Long> objectIds, DataObject dataObject, DataOperation dataOperation) throws BizException {
        BizAssert.notNull(dataObject, "对象类型不能为空！");
        BizAssert.notEmpty(objectIds, String.format("【%s】的id不能为空！", dataObject.getDescription()));
        List<Long> groupIds = ThreadContext.getUserInfo().getGroupIds();
        if (!ThreadContext.getUserInfo().isSuperAdmin()
                && !groupIds.isEmpty()) {
            List<AuthorizationRightsModel> rightsModelList =  authorizationRightsModelMapper.getByObjectIds(objectIds, dataObject, groupIds);
            return filterRights(rightsModelList, dataOperation);
        }
        return new ArrayList<>();
    }

    /**
     * 获取用户拥有的某类数据的所有id
     * @param dataObject 对象类型
     * @return 对象id列表
     */
    public List<Long> getObjectIds(DataObject dataObject, DataOperation dataOperation) throws BizException {
        List<AuthorizationRightsModel> rights = getRights(dataObject, dataOperation);
        return rights.stream().map(AuthorizationRightsModel::getObjectId).distinct().collect(Collectors.toList());
    }

    /**
     * 从objectIds中挑选出用户有权限的数据id
     * @param dataObject
     * @param objectIds
     * @return
     * @throws BizException
     */
    public List<Long> getObjectIdsByObjectIds(List<Long> objectIds, DataObject dataObject, DataOperation dataOperation) throws BizException {
        List<AuthorizationRightsModel> rights = getRightsByObjectIds(objectIds, dataObject, dataOperation);
        return rights.stream().map(AuthorizationRightsModel::getObjectId).distinct().collect(Collectors.toList());
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
        if (ThreadContext.getUserInfo().isSuperAdmin()){
            return DataOperation.ALL.getValue();
        }
        List<Integer> permissions = authorizationRightsModelMapper.getPermissionByObjectId(objectId, dataObject, ThreadContext.getUserInfo().getGroupIds());
        int permission = 0;
        for (Integer p : permissions) {
            permission = permission | p;
        }
        return permission;
    }

    /**
     * 移除多个对象的权限值
     * @param dataObject 对象类型
     * @param objectIds 对象id的列表
     */
    public void deleteByObjectIds(DataObject dataObject, List<Long> objectIds) throws BizException {
        BizAssert.notNull(dataObject, "对象类型不能为空！");
        BizAssert.notEmpty(objectIds, String.format("【%s】的id不能为空！", dataObject.getDescription()));
        BizAssert.isTrue(checkPermisson(dataObject, objectIds, DataOperation.DELETE), "权限不足");
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
        BizAssert.isTrue(checkPermisson(dataObject, objectId, DataOperation.DELETE), "权限不足");
        authorizationRightsModelMapper.deleteByObjectId(objectId, dataObject);
    }

    /**
     * 清空某个用户组相关数据权限
     * @param groupId 用户组
     */
    @Transactional(rollbackFor = Exception.class)
    public void clearByGroupId(Long groupId) throws BizException {
        BizAssert.notNull(groupId, "用户组id不能为空！");
        BizAssert.isTrue(checkGroupId(groupId),"含有没有权限操作的用户组");
        authorizationRightsModelMapper.clearByGroupId(groupId);
    }

    /**
     * 清空多个用户组相关数据权限
     * @param groupIds 用户组id的列表
     */
    @Transactional(rollbackFor = Exception.class)
    public void clearByGroupIds(List<Long> groupIds) throws BizException {
        BizAssert.notEmpty(groupIds, "用户组id不能为空！");
        BizAssert.isTrue(checkGroupIds(groupIds),"含有没有权限操作的用户组");
        authorizationRightsModelMapper.clearByGroupIds(groupIds);
    }

}
