package com.shsnc.base.user.service;

import com.shsnc.base.user.mapper.GroupModelMapper;
import com.shsnc.base.user.mapper.UserGroupResourceGroupRelationModelMapper;
import com.shsnc.base.user.model.GroupModel;
import com.shsnc.base.user.model.UserGroupResourceGroupRelationModel;
import com.shsnc.base.util.BizAssert;
import com.shsnc.base.util.config.BizException;
import org.apache.commons.collections.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author houguangqiang
 * @date 2017-07-26
 * @since 1.0
 */
@Service
public class UserGroupResourceGroupRelationService {

    @Autowired
    private UserGroupResourceGroupRelationModelMapper userGroupResourceGroupRelationModelMapper;
    @Autowired
    private GroupModelMapper groupModelMapper;

    public List<Long> batchAddUserGroupResourceGroupRelation(Long userGroupId, List<Long> resourceGroupIds) throws BizException {
        BizAssert.notNull(userGroupId, "用户id不能为空！");
        BizAssert.notEmpty(resourceGroupIds, "资源id不能为空！");
        GroupModel groupModel = groupModelMapper.selectByPrimaryKey(userGroupId);
        BizAssert.notNull(groupModel, String.format("用户组id【%s】不存在！",userGroupId));

        List<UserGroupResourceGroupRelationModel> userGroupResourceGroupRelationModels = new ArrayList<>();
        for (Long resourceGroupId : resourceGroupIds) {
            UserGroupResourceGroupRelationModel relation = new UserGroupResourceGroupRelationModel();
            relation.setResourceGroupId(resourceGroupId);
            relation.setUserGroupId(userGroupId);
            userGroupResourceGroupRelationModels.add(relation);
        }

        userGroupResourceGroupRelationModelMapper.insertRelationList(userGroupResourceGroupRelationModels);
        return userGroupResourceGroupRelationModels.stream().map(UserGroupResourceGroupRelationModel::getRelationId).collect(Collectors.toList());
    }

    public boolean batchUpdateUserGroupResourceGroupRelation(Long userGroupId, List<Long> resourceGroupIds) throws BizException {
        BizAssert.notNull(userGroupId, "用户id不能为空！");
        BizAssert.notNull(resourceGroupIds, "资源id不能为空！");
        GroupModel groupModel = groupModelMapper.selectByPrimaryKey(userGroupId);
        BizAssert.notNull(groupModel, String.format("用户组id【%s】不存在！",userGroupId));

        if (resourceGroupIds.isEmpty()) {
            return userGroupResourceGroupRelationModelMapper.deleteByUserGroupId(userGroupId) > 0;
        } else {
            List<Long> dbResourceGroupIds = userGroupResourceGroupRelationModelMapper.getResourceGroupIdsByUserGroupId(userGroupId);
            List<Long> deleteResourceGroupIds = ListUtils.removeAll(dbResourceGroupIds, resourceGroupIds);
            if (!deleteResourceGroupIds.isEmpty()) {
                userGroupResourceGroupRelationModelMapper.deleteByUserGroupId(userGroupId);
                batchAddUserGroupResourceGroupRelation(userGroupId, resourceGroupIds);
            } else {
                List<Long> addResourceGroupIds = ListUtils.removeAll(resourceGroupIds, dbResourceGroupIds);
                if (!addResourceGroupIds.isEmpty()) {
                    batchAddUserGroupResourceGroupRelation(userGroupId, addResourceGroupIds);
                }
            }
        }
        return true;
    }

}
