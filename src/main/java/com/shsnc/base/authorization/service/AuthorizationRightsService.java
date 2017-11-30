package com.shsnc.base.authorization.service;

import com.google.common.collect.Sets;
import com.shsnc.api.core.ThreadContext;
import com.shsnc.base.authorization.config.DataObject;
import com.shsnc.base.authorization.config.DataOperation;
import com.shsnc.base.authorization.mapper.AuthorizationRightsModelMapper;
import com.shsnc.base.authorization.model.AuthorizationRightsModel;
import com.shsnc.base.bean.Condition;
import com.shsnc.base.user.mapper.GroupModelMapper;
import com.shsnc.base.user.model.GroupModel;
import com.shsnc.base.util.BizAssert;
import com.shsnc.base.util.config.BaseException;
import com.shsnc.base.util.config.BizException;
import com.shsnc.base.util.config.MessageCode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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
    private GroupModelMapper groupModelMapper;

    private boolean checkGroupId(Long groupId) {
        if (!ThreadContext.getUserInfo().isSuperAdmin()) {
            List<Long> currentGroupIds = ThreadContext.getUserInfo().getGroupIds();
            return currentGroupIds.contains(groupId);
        }
        return true;
    }

    private boolean checkGroupIds(List<Long> groupIds) {
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
        Condition condition = new Condition(true, groupIds);
        List<AuthorizationRightsModel> authorizationRightsModels = authorizationRightsModelMapper.getByObjectId(dataObject, objectId, condition);
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
        if (ThreadContext.getUserInfo().isSuperAdmin() || objectIds.isEmpty()) {
            return true;
        }
        List<Long> groupIds = ThreadContext.getUserInfo().getGroupIds();
        if (groupIds.isEmpty()) {
            return false;
        }
        Condition condition = new Condition(true, groupIds);
        List<AuthorizationRightsModel> authorizationRightsModels = authorizationRightsModelMapper.getByObjectIds(dataObject, objectIds, condition);
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
     * 更新对象授权，一个对象授权给多个用户组
     * @param dataObject 对象类型
     * @param objectId 对象id
     * @param groupIds 用户组id列表
     * @param dataOperation 操作类型
     * @param update 是否是已存在对象的授权
     */
    @Transactional(rollbackFor = Exception.class)
    public void authorize(DataObject dataObject, Long objectId, List<Long> groupIds, DataOperation dataOperation, boolean update) throws BaseException {
        authorize(dataObject, objectId, groupIds, dataOperation.getValue(), update);
    }

    /**
     * 更新对象授权，一个对象授权给多个用户组
     * @param dataObject 对象类型
     * @param objectId 对象id
     * @param groupIds 用户组id列表
     * @param permission 权限值
     * @param update 是否是已存在对象的授权
     */
    @Transactional(rollbackFor = Exception.class)
    public void authorize(DataObject dataObject, Long objectId, List<Long> groupIds, int permission, boolean update) throws BaseException {
        BizAssert.notNull(dataObject, "对象类型不能为空！");
        BizAssert.notNull(objectId, String.format("【%s】的id不能为空！", dataObject.getDescription()));
        BizAssert.notEmpty(groupIds, "用户组不能为空！");
        Condition condition = new Condition();
        if (!ThreadContext.getUserInfo().isSuperAdmin()) {
           /* if (!checkGroupIds(groupIds) || (update && !checkPermisson(dataObject, objectId, permission))) {
                throw new BaseException(MessageCode.PERMISSION_DENIED);
            }*/
            condition.permission(true, ThreadContext.getUserInfo().getGroupIds());
        }

        List<AuthorizationRightsModel> rightsModels = authorizationRightsModelMapper.getByObjectId(dataObject, objectId, condition);
        Map<Long,AuthorizationRightsModel> rightsMap = rightsModels.stream().collect(Collectors.toMap(AuthorizationRightsModel::getGroupId, v->v));

        Set<Long> groupIdSet = Sets.newHashSet(groupIds);
        Set<Long> dbGroupIds = rightsMap.keySet();
        Set<Long> deleteGroupIds = Sets.difference(dbGroupIds, groupIdSet);
        Set<Long> updateGroupIds = Sets.intersection(groupIdSet, dbGroupIds);
        Set<Long> addGroupIds = Sets.difference(groupIdSet, dbGroupIds);

        // 删除
        if (!deleteGroupIds.isEmpty()) {
            List<Long> rightIds = new ArrayList<>();
            for (Long deleteGroupId : deleteGroupIds) {
                rightIds.add(rightsMap.get(deleteGroupId).getRightId());
            }
            authorizationRightsModelMapper.deleteByRightIds(rightIds);
        }
        // 更新
        if (!updateGroupIds.isEmpty()) {
            for (Long updateGroupId : updateGroupIds) {
                AuthorizationRightsModel authorizationRightsModel = rightsMap.get(updateGroupId);
                if (!authorizationRightsModel.getPermission().equals(permission)) {
                    AuthorizationRightsModel updateRightsModel = new AuthorizationRightsModel();
                    updateRightsModel.setRightId(authorizationRightsModel.getRightId());
                    updateRightsModel.setPermission(permission);
                    authorizationRightsModelMapper.updateByPrimaryKeySelective(authorizationRightsModel);
                }
            }
        }
        // 新增
        if (!addGroupIds.isEmpty()) {
            List<AuthorizationRightsModel> authorizationRightsModels = new ArrayList<>();
            for (Long addGroupId : addGroupIds) {
                AuthorizationRightsModel authorizationRightsModel = new AuthorizationRightsModel();
                authorizationRightsModel.setGroupId(addGroupId);
                authorizationRightsModel.setObjectId(objectId);
                authorizationRightsModel.setObjectType(dataObject.getType());
                authorizationRightsModel.setObjectTypeCode(dataObject.toString());
                authorizationRightsModel.setPermission(permission);
                authorizationRightsModels.add(authorizationRightsModel);
            }
            authorizationRightsModelMapper.insertList(authorizationRightsModels);
        }

    }

    /**
     * 更新对象授权，多个对象授权给一个用户组
     * @param dataObject 对象类型
     * @param objectIds 对象id列表
     * @param groupId 用户组id
     * @param dataOperation 操作类型
     * @param update 是否是已存在对象的授权
     */
    @Transactional(rollbackFor = Exception.class)
    public void authorize(DataObject dataObject, List<Long> objectIds, Long groupId, DataOperation dataOperation, boolean update) throws BaseException {
        authorize(dataObject, objectIds, groupId, dataOperation.getValue(), update);
    }

    /**
     * 更新对象授权，多个对象授权给一个用户组
     * @param dataObject 对象类型
     * @param objectIds 对象id列表
     * @param groupId 用户组id
     * @param permission 权限值
     * @param update 是否是已存在对象的授权
     */
    @Transactional(rollbackFor = Exception.class)
    public void authorize(DataObject dataObject, List<Long> objectIds, Long groupId, int permission, boolean update) throws BaseException {
        BizAssert.notNull(dataObject, "对象类型不能为空！");
        BizAssert.notNull(objectIds, String.format("【%s】的id不能为null！", dataObject.getDescription()));
        BizAssert.notNull(groupId, "用户组id不能为空！");
        GroupModel dbGroupModel = groupModelMapper.selectByPrimaryKey(groupId);
        BizAssert.notNull(dbGroupModel, String.format("用户组id【%s】不存在！",groupId));

        Condition condition = new Condition();
        if (!ThreadContext.getUserInfo().isSuperAdmin()) {
           /* if (!checkGroupId(groupId) || (update && !checkPermisson(dataObject, objectIds, permission))) {
                throw new BaseException(MessageCode.PERMISSION_DENIED);
            }*/
            condition.permission(true, ThreadContext.getUserInfo().getGroupIds());
        }

        List<AuthorizationRightsModel> rightsModels = authorizationRightsModelMapper.getByGroupId(dataObject, groupId, condition);
        Map<Long,AuthorizationRightsModel> rightsMap = rightsModels.stream().collect(Collectors.toMap(AuthorizationRightsModel::getObjectId, v->v));

        Set<Long> objectIdSet = Sets.newHashSet(objectIds);
        Set<Long> dbObjectIds = rightsMap.keySet();
        Set<Long> deleteObjectIds = Sets.difference(dbObjectIds, objectIdSet);
        Set<Long> updateObjectIds = Sets.intersection(objectIdSet, dbObjectIds);
        Set<Long> addObjectIds = Sets.difference(objectIdSet , dbObjectIds);

        // 删除
        if (!deleteObjectIds.isEmpty()) {
            List<Long> rightIds = new ArrayList<>();
            for (Long deleteObjectId : deleteObjectIds) {
                rightIds.add(rightsMap.get(deleteObjectId).getRightId());
            }
            authorizationRightsModelMapper.deleteByRightIds(rightIds);
        }
        // 更新
        if (!updateObjectIds.isEmpty()) {
            for (Long updateObjectId : updateObjectIds) {
                AuthorizationRightsModel authorizationRightsModel = rightsMap.get(updateObjectId);
                if (!authorizationRightsModel.getPermission().equals(permission)) {
                    AuthorizationRightsModel updateRightsModel = new AuthorizationRightsModel();
                    updateRightsModel.setRightId(authorizationRightsModel.getRightId());
                    updateRightsModel.setPermission(permission);
                    authorizationRightsModelMapper.updateByPrimaryKeySelective(authorizationRightsModel);
                }
            }
        }
        // 新增
        if (!addObjectIds.isEmpty()) {
            List<AuthorizationRightsModel> authorizationRightsModels = new ArrayList<>();
            for (Long addObjectId : addObjectIds) {
                AuthorizationRightsModel authorizationRightsModel = new AuthorizationRightsModel();
                authorizationRightsModel.setGroupId(groupId);
                authorizationRightsModel.setObjectId(addObjectId);
                authorizationRightsModel.setObjectType(dataObject.getType());
                authorizationRightsModel.setObjectTypeCode(dataObject.toString());
                authorizationRightsModel.setPermission(permission);
                authorizationRightsModels.add(authorizationRightsModel);
            }
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
        Condition condition = new Condition();
        if (!ThreadContext.getUserInfo().isSuperAdmin()) {
            List<Long> groupIds = ThreadContext.getUserInfo().getGroupIds();
            condition.permission(true, groupIds);
        }
        List<AuthorizationRightsModel> rightsModelList = authorizationRightsModelMapper.getRights(dataObject, condition);
        return filterRights(rightsModelList, dataOperation);
    }

    public List<AuthorizationRightsModel> getRightsByObjectIds(List<Long> objectIds, DataObject dataObject, DataOperation dataOperation) throws BizException {
        BizAssert.notNull(dataObject, "对象类型不能为空！");
        BizAssert.notEmpty(objectIds, String.format("【%s】的id不能为空！", dataObject.getDescription()));
        Condition condition = new Condition();
        if (!ThreadContext.getUserInfo().isSuperAdmin()) {
            List<Long> groupIds = ThreadContext.getUserInfo().getGroupIds();
            condition.permission(true, groupIds);
        }
        List<AuthorizationRightsModel> rightsModelList =  authorizationRightsModelMapper.getByObjectIds(dataObject, objectIds, condition);
        return filterRights(rightsModelList, dataOperation);
    }

    /**
     *
    * @Title: getRightsByGroupIds
    * @Description: TODO(根据用户组获取资源组)
    * @param dataObject
    * @param groupIds
    * @return
    * @throws BizException            参数
    * @return List<Long>    返回类型
    * @throws
     */
    public List<Long> getRightsByGroupIds( DataObject dataObject,List<Long> groupIds) throws BizException {

        BizAssert.notEmpty(groupIds, String.format("【%s】的id不能为空！", "用户组"));

        List<AuthorizationRightsModel> rightsModelList =  authorizationRightsModelMapper.getByGroupIds(dataObject, groupIds);

         List<Long> resourceGroupIds = new ArrayList<Long>();

        for(AuthorizationRightsModel rights : rightsModelList){
            resourceGroupIds.add(rights.getObjectId());
        }
        return resourceGroupIds;
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
        int permission = 0;
        List<Long> groupIds = ThreadContext.getUserInfo().getGroupIds();
        List<Integer> permissions = authorizationRightsModelMapper.getPermissionByObjectId(dataObject, objectId, new Condition(true, groupIds));
        for (Integer p : permissions) {
            permission = permission | p;
        }
        return permission;
    }

    /**
     * 检查某些对象是否可以删除，那些用户组必选的对象删除关系前应该调用此方法校验是否可以删除
     * @param dataObject 对象类型
     * @param objectIds 对象id的集合
     * @param groupId 用户组id
     * @return 返回检查不通过对象id集合
     * @throws BaseException
     */
    public List<Long> getNotDeletableObjectIds(DataObject dataObject, List<Long> objectIds, Long groupId) throws BaseException {
        Condition condition = new Condition();
        if (!ThreadContext.getUserInfo().isSuperAdmin()) {
            if (!checkGroupId(groupId) || !checkPermisson(dataObject, objectIds, DataOperation.DELETE)) {
                throw new BaseException(MessageCode.PERMISSION_DENIED);
            }
            condition.permission(true, ThreadContext.getUserInfo().getGroupIds());
        }
        List<AuthorizationRightsModel> rightsModels = authorizationRightsModelMapper.getByGroupId(dataObject, groupId, condition);
        Map<Long,AuthorizationRightsModel> rightsMap = rightsModels.stream().collect(Collectors.toMap(AuthorizationRightsModel::getObjectId, v->v));

        Set<Long> deleteObjectIds = Sets.difference(rightsMap.keySet(), Sets.newHashSet(objectIds));
        List<Long> result = new ArrayList<>();
        if (!deleteObjectIds.isEmpty()) {
            Set<Long> notDeletableObjectIds = new HashSet<>(authorizationRightsModelMapper.getNotDeletableObjectIds(dataObject, deleteObjectIds));

            if (!notDeletableObjectIds.isEmpty()) {
                for (Long deleteObjectId : deleteObjectIds) {
                    if (notDeletableObjectIds.contains(deleteObjectId)) {
                        result.add(deleteObjectId);
                    }
                }
            }
        }
        return result;
    }

    /**
     * 删除一个对象时移除相关数据权限
     * @param dataObject 对象类型
     * @param objectIds 对象id的列表
     */
    public void deleteByObjectIds(DataObject dataObject, List<Long> objectIds) throws BaseException {
        BizAssert.notNull(dataObject, "对象类型不能为空！");
        BizAssert.notEmpty(objectIds, String.format("【%s】的id不能为空！", dataObject.getDescription()));
//        if (!checkPermisson(dataObject, objectIds, DataOperation.DELETE)) {
//            throw new BaseException(MessageCode.PERMISSION_DENIED);
//        }
        authorizationRightsModelMapper.deleteByObjectIds(dataObject, objectIds);
    }

    /**
     * 删除多个对象时移除相关数据权限
     * @param dataObject 对象类型
     * @param objectId 对象id
     */
    public void deleteByObjectId(DataObject dataObject, Long objectId) throws BaseException {
        BizAssert.notNull(dataObject, "对象类型不能为空！");
        BizAssert.notNull(objectId, String.format("【%s】的id不能为空！", dataObject.getDescription()));
   /*     if (!checkPermisson(dataObject, objectId, DataOperation.DELETE)) {
            throw new BaseException(MessageCode.PERMISSION_DENIED);
        }*/
        authorizationRightsModelMapper.deleteByObjectId(dataObject, objectId);
    }

    /**
     * 删除一个用户组时移除相关数据权限
     * @param groupId 用户组id
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteByGroupId(Long groupId) throws BaseException {
        BizAssert.notNull(groupId, "用户组id不能为空！");
        if (!checkGroupId(groupId)) {
            throw new BaseException(MessageCode.PERMISSION_DENIED);
        }
        authorizationRightsModelMapper.deleteByGroupId(groupId);
    }

    /**
     * 删除多个用户组时移除相关数据权限
     * @param groupIds 用户组id的列表
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteByGroupIds(List<Long> groupIds) throws BaseException {
        BizAssert.notEmpty(groupIds, "用户组id不能为空！");
        if (!checkGroupIds(groupIds)) {
            throw new BaseException(MessageCode.PERMISSION_DENIED);
        }
        authorizationRightsModelMapper.deleteByGroupIds(groupIds);
    }

    /**
     * 获取对象关联的用户组列表
     * @param dataObject 对象类型
     * @param objectId 对象id
     * @return
     * @throws BaseException
     */
    public List<GroupModel> getGroupsByObjectId(DataObject dataObject, Long objectId) throws BaseException {
        BizAssert.notNull(dataObject, "对象类型不能为空！");
        BizAssert.notNull(objectId, String.format("【%s】的id不能为空！", dataObject.getDescription()));
        Condition condition = new Condition();
        if (!ThreadContext.getUserInfo().isSuperAdmin()) {
            List<Long> groupIds = ThreadContext.getUserInfo().getGroupIds();
            condition.permission(true, groupIds);
        }
        List<AuthorizationRightsModel> rights = authorizationRightsModelMapper.getByObjectId(dataObject, objectId, condition);
        List<Long> groupIds = rights.stream().map(AuthorizationRightsModel::getGroupId).distinct().collect(Collectors.toList());
        if (groupIds.isEmpty()) {
            return new ArrayList<>();
        }
        return groupModelMapper.getByGroupIds(groupIds);
    }

    public List<Long> getGroupIdsByObjectId(DataObject dataObject, Long objectId) throws BizException {
        BizAssert.notNull(dataObject, "对象类型不能为空！");
        BizAssert.notNull(objectId, String.format("【%s】的id不能为空！", dataObject.getDescription()));
        Condition condition = new Condition();
        if (!ThreadContext.getUserInfo().isSuperAdmin()) {
            List<Long> groupIds = ThreadContext.getUserInfo().getGroupIds();
            condition.permission(true, groupIds);
        }
        List<AuthorizationRightsModel> rights = authorizationRightsModelMapper.getByObjectId(dataObject, objectId, condition);
        return rights.stream().map(AuthorizationRightsModel::getGroupId).distinct().collect(Collectors.toList());
    }
}
