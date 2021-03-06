package com.shsnc.base.user.service;

import com.shsnc.api.core.ThreadContext;
import com.shsnc.base.authorization.config.DataObject;
import com.shsnc.base.authorization.config.DataOperation;
import com.shsnc.base.authorization.mapper.AuthorizationRightsModelMapper;
import com.shsnc.base.authorization.model.AuthorizationRightsModel;
import com.shsnc.base.authorization.service.AuthorizationRightsService;
import com.shsnc.base.bean.Condition;
import com.shsnc.base.module.bean.ResourceGroupInfo;
import com.shsnc.base.module.config.BaseResourceService;
import com.shsnc.base.user.mapper.GroupModelMapper;
import com.shsnc.base.user.mapper.UserInfoGroupRelationModelMapper;
import com.shsnc.base.user.mapper.UserInfoModelMapper;
import com.shsnc.base.user.model.GroupModel;
import com.shsnc.base.user.model.UserInfoGroupRelationModel;
import com.shsnc.base.user.model.UserInfoModel;
import com.shsnc.base.user.model.condition.GroupCondition;
import com.shsnc.base.util.BizAssert;
import com.shsnc.base.util.bean.RelationMap;
import com.shsnc.base.util.config.BaseException;
import com.shsnc.base.util.config.BizException;
import com.shsnc.base.util.config.MessageCode;
import com.shsnc.base.util.sql.Pagination;
import com.shsnc.base.util.sql.QueryData;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户组业务类
 * @author houguangqiang
 * @date 2017-07-26
 * @since 1.0
 */
@Service
public class GroupService {

    @Autowired
    private GroupModelMapper groupModelMapper;
    @Autowired
    private UserInfoGroupRelationService userInfoGroupRelationService;
    @Autowired
    private UserInfoGroupRelationModelMapper userInfoGroupRelationModelMapper;
    @Autowired
    private UserInfoModelMapper userInfoModelMapper;
    @Autowired
    private AuthorizationRightsService authorizationRightsService;
    @Autowired
    private AuthorizationRightsModelMapper authorizationRightsModelMapper;

    /**
     * 获取所有用户组
     * @return 返回用户组列表
     */
    public List<GroupModel> getGroupList(GroupCondition condition) {
        if (!ThreadContext.getUserInfo().isSuperAdmin()) {
            List<Long> groupIds = ThreadContext.getUserInfo().getGroupIds();
            if (!groupIds.isEmpty()) {
                condition.setGroupIds(groupIds);
            } else {
                return new ArrayList<>();
            }
        }
        return groupModelMapper.getListByCondition(condition);
    }

    /**
     * 根据id获取用户组信息
     * @param groupId 用户组id
     * @return 用户组model
     */
    public GroupModel getGroup(Long groupId) throws BaseException {
        BizAssert.notNull(groupId, "用户组id不能为空！");
        if (!ThreadContext.getUserInfo().isSuperAdmin()) {
            List<Long> groupIds = ThreadContext.getUserInfo().getGroupIds();
            if (!groupIds.contains(groupId)) {
                throw new BaseException(MessageCode.PERMISSION_DENIED);
            }
        }
        return groupModelMapper.selectByPrimaryKey(groupId);
    }

    /**
     * 用户组分页查询
     * @param condition 查询条件
     * @param pagination 分页对象
     * @return 分页信息
     */
    public QueryData getGroupPage(GroupCondition condition, Pagination pagination) throws BizException {
        if (!ThreadContext.getUserInfo().isSuperAdmin()) {
            List<Long> groupIds = ThreadContext.getUserInfo().getGroupIds();
            if (!groupIds.isEmpty()) {
                condition.setGroupIds(groupIds);
            } else {
                return new QueryData(pagination);
            }
        }
        QueryData queryData = new QueryData(pagination);
        int totalCount = groupModelMapper.getTotalCountByCondition(condition);
        queryData.setRowCount(totalCount);
        List<GroupModel> list = groupModelMapper.getPageByCondition(condition, pagination);
        selectUsers(list);
        selectResourceGroups(list);
        queryData.setRecords(list);
        return queryData;
    }

    public void selectResourceGroups(List<GroupModel> groupModels) throws BizException {
        if (CollectionUtils.isEmpty(groupModels)) {
            return;
        }
        List<Long> groupIds = groupModels.stream().map(GroupModel::getGroupId).collect(Collectors.toList());
        if (!groupIds.isEmpty()) {
            List<AuthorizationRightsModel> relations = authorizationRightsModelMapper.getByGroupIds(DataObject.RESOURCE_GROUP, groupIds);
            RelationMap relationMap = new RelationMap();
            for (AuthorizationRightsModel relation : relations) {
                relationMap.addRelation(relation.getGroupId(),relation.getObjectId());
            }
            if (relationMap.hasRelatedIds()) {
                List<ResourceGroupInfo> resourceGroupInfos = BaseResourceService.getResourceGroupsByResourceGroupIds(new ArrayList<>(relationMap.getRelatedIds()));
                Map<Long, ResourceGroupInfo> resourceGroupInfoMap = resourceGroupInfos.stream().collect(Collectors.toMap(ResourceGroupInfo::getGroupId, v -> v));
                for (GroupModel groupModel : groupModels) {
                    groupModel.setResourceGroups(relationMap.getRelatedObjects(groupModel.getGroupId(), resourceGroupInfoMap));
                }
            }
        }
    }

    public void selectUsers(List<GroupModel> groupModels) {
        if (CollectionUtils.isEmpty(groupModels)) {
            return;
        }
        List<Long> groupIds = groupModels.stream().map(GroupModel::getGroupId).collect(Collectors.toList());
        if (!groupIds.isEmpty()) {
            List<UserInfoGroupRelationModel> relations = userInfoGroupRelationModelMapper.getByGroupids(groupIds);
            RelationMap relationMap = new RelationMap();
            for (UserInfoGroupRelationModel relation : relations) {
                relationMap.addRelation(relation.getGroupId(),relation.getUserId());
            }
            if (relationMap.hasRelatedIds()) {
                List<UserInfoModel> userInfoModels = userInfoModelMapper.getByUserIds(relationMap.getRelatedIds());
                Map<Long, UserInfoModel> userInfoModelMap = userInfoModels.stream().collect(Collectors.toMap(UserInfoModel::getUserId, x -> x, (oldValue, newValue)->oldValue));
                for (GroupModel groupModel : groupModels) {
                    groupModel.setUsers(relationMap.getRelatedObjects(groupModel.getGroupId(),userInfoModelMap));
                }
            }
        }
    }
    /**
     * 新增用户组
     * @param group 用户组
     * @return 返回新增记录id
     */
    public Long addGroup(GroupModel group) throws BizException {
        group.setCode(UUID.randomUUID().toString());
        checkInput(group);
        groupModelMapper.insert(group);
        if (!ThreadContext.getUserInfo().isSuperAdmin()) {
            UserInfoGroupRelationModel userInfoGroupRelationModel = new UserInfoGroupRelationModel();
            userInfoGroupRelationModel.setGroupId(group.getGroupId());
            userInfoGroupRelationModel.setUserId(ThreadContext.getUserInfo().getUserId());
            userInfoGroupRelationModelMapper.insert(userInfoGroupRelationModel);
        }
        return group.getGroupId();
    }

    /**
     * 更新用户组
     * @param group 用户组
     * @return 如果有记录更细返回true，否则返回false
     */
    public boolean updateGroup(GroupModel group) throws BaseException {
        BizAssert.notNull(group.getGroupId(), "用户组id不能为空！");
        if (!ThreadContext.getUserInfo().isSuperAdmin()) {
            List<Long> groupIds = ThreadContext.getUserInfo().getGroupIds();
            if (!groupIds.contains(group.getGroupId())) {
                throw new BaseException(MessageCode.PERMISSION_DENIED);
            }
        }
        checkInput(group);
        return groupModelMapper.updateByPrimaryKeySelective(group) > 0;
    }

    /**
     * 根据id删除用户组
     * @param groupId 用户组id
     * @return 如果有记录更细返回true，否则返回false
     */
    public boolean deleteGroup(Long groupId) throws BaseException {
        BizAssert.notNull(groupId, "用户组id不能为空！");
        if (!ThreadContext.getUserInfo().isSuperAdmin()) {
            List<Long> groupIds = ThreadContext.getUserInfo().getGroupIds();
            if (!groupIds.contains(groupId)) {
                throw new BaseException(MessageCode.PERMISSION_DENIED);
            }
        }
        userInfoGroupRelationService.deleteByGroupId(groupId);
        return groupModelMapper.deleteByPrimaryKey(groupId) > 0;
    }

    /**
     * 根据id集合批量删除用户组
     * @param groupIds 用户组id的集合
     * @return 如果有记录更细返回true，否则返回false
     */
    public boolean batchDeleteGroup(List<Long> groupIds) throws BaseException {
        BizAssert.notEmpty(groupIds, "用户组id不能为空！");
        if (!ThreadContext.getUserInfo().isSuperAdmin()) {
            List<Long> currentGroupIds = ThreadContext.getUserInfo().getGroupIds();
            if (!currentGroupIds.containsAll(groupIds)) {
                throw new BaseException(MessageCode.PERMISSION_DENIED);
            }
        }
        userInfoGroupRelationService.deleteByGroupIds(groupIds);
        return groupModelMapper.deleteByGroupIds(groupIds);
    }

    private void checkInput(GroupModel group) throws BizException {
        Long groupId = group.getGroupId();
        String name = group.getName();
        BizAssert.hasLength(name, "用户组名称不能为空！");
        if (groupId == null) {
            BizAssert.hasLength(group.getCode(), "用户组编码不能为空！");
        }
        if (name != null) {
            GroupModel exist = new GroupModel();
            exist.setName(name);
            exist = groupModelMapper.selectOne(exist);
            BizAssert.isTrue(exist == null || exist.getGroupId().equals(groupId), String.format("用户组【%s】已经存在！", name));
        }
    }

    /**
     * 给用户组分配用户
     * @param groupId 用户组id
     * @param userIds 用户id列表
     * @return
     */
    public boolean assignUsers(Long groupId, List<Long> userIds) throws BaseException {
        userInfoGroupRelationService.batchUpdateGroupUserInfoRelation(groupId, userIds, true);
        return true;
    }

    /**
     * 用户组关联资源组
     * @param groupId 用户组id
     * @param reresourceGroupIds 资源组id
     * @return
     */
    public boolean assignReresourceGroups(Long groupId, List<Long> reresourceGroupIds) throws BaseException {
        List<Long> notDeletableObjectIds = authorizationRightsService.getNotDeletableObjectIds(DataObject.RESOURCE_GROUP, reresourceGroupIds, groupId);
        if (!notDeletableObjectIds.isEmpty()) {
            for (Long notDeletableObjectId : notDeletableObjectIds) {
                List<ResourceGroupInfo> resourceGroups = BaseResourceService.getResourceGroupsByResourceGroupIds(Collections.singletonList(notDeletableObjectId));
                if (resourceGroups != null) {
                    for (ResourceGroupInfo resourceGroup : resourceGroups) {
                        throw new BizException(String.format("资源组【%s】必须至少归属于一个用户组！",resourceGroup.getGroupName()));
                    }
                }
            }
        }
        authorizationRightsService.authorize(DataObject.RESOURCE_GROUP, reresourceGroupIds, groupId, DataOperation.ALL, true);
        return true;
    }

    public List<GroupModel> getGroupByUserId(Long userId, Condition condition) {
        List<Long> groupIds = userInfoGroupRelationModelMapper.getGroupIdsByUserId(userId, condition);
        if (!groupIds.isEmpty()) {
            return groupModelMapper.getByGroupIds(groupIds);
        }
        return new ArrayList<>();
    }

    public List<GroupModel> getByGroupIds(List<Long> groupIds) {
        return groupModelMapper.getByGroupIds(groupIds);
    }
}
