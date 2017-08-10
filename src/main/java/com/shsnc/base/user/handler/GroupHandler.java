package com.shsnc.base.user.handler;

import com.shsnc.api.core.RequestHandler;
import com.shsnc.api.core.ThreadContext;
import com.shsnc.api.core.annotation.Authentication;
import com.shsnc.api.core.annotation.LoginRequired;
import com.shsnc.api.core.annotation.RequestMapper;
import com.shsnc.api.core.validation.Validate;
import com.shsnc.api.core.validation.ValidationType;
import com.shsnc.base.user.bean.Group;
import com.shsnc.base.user.bean.GroupCondition;
import com.shsnc.base.user.model.GroupModel;
import com.shsnc.base.user.service.GroupService;
import com.shsnc.base.util.JsonUtil;
import com.shsnc.base.util.config.BaseException;
import com.shsnc.base.util.config.BizException;
import com.shsnc.base.util.config.MessageCode;
import com.shsnc.base.util.sql.Pagination;
import com.shsnc.base.util.sql.QueryData;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 *
 * @author houguangqiang
 * @since 1.0
 * @date 2017-07-26
 */
@Component
@RequestMapper("/user/group")
@LoginRequired
public class GroupHandler implements RequestHandler {

    @Autowired
    private GroupService groupService;


    @RequestMapper("/getList")
    @Authentication("BASE_USER_GROUP_GET_LIST")
    public List<Group> getList(GroupCondition condition){
        if (!ThreadContext.getUserInfo().isSuperAdmin()) {
            List<Long> groupIds = ThreadContext.getUserInfo().getGroupIds();
            if (!groupIds.isEmpty()) {
                condition.setCheckPermission(true);
                condition.setObjectIds(groupIds);
            } else {
                return new ArrayList<>();
            }
        }
        List<GroupModel> groupList = groupService.getGroupList(condition);
        return JsonUtil.convert(groupList, List.class, Group.class) ;
    }

    private static final String[][] mapping = {{"name","name"}};

    @RequestMapper("/getObject")
    @Validate
    @Authentication("BASE_USER_GROUP_GET_OBJECT")
    public Group getObject(@NotNull Long groupId) throws BaseException {
        if (!ThreadContext.getUserInfo().isSuperAdmin()) {
            List<Long> groupIds = ThreadContext.getUserInfo().getGroupIds();
            if (!groupIds.contains(groupId)) {
                throw new BaseException(MessageCode.PERMISSION_DENIED);
            }
        }
        GroupModel groupModel = groupService.getGroup(groupId);
        List<GroupModel> groupModels = Collections.singletonList(groupModel);
        groupService.selectUsers(groupModels);
        groupService.selectResourceGroups(groupModels);
        return JsonUtil.convert(groupModel, Group.class);
    }

    @RequestMapper("/getPage")
    @Authentication("BASE_USER_GROUP_GET_PAGE")
    public QueryData getPage(GroupCondition condition, Pagination pagination) throws BizException {
        if (!ThreadContext.getUserInfo().isSuperAdmin()) {
            List<Long> groupIds = ThreadContext.getUserInfo().getGroupIds();
            if (!groupIds.isEmpty()) {
                condition.setCheckPermission(true);
                condition.setObjectIds(groupIds);
            } else {
                return new QueryData(pagination);
            }
        }
        pagination.buildSort(mapping);
        QueryData queryData = groupService.getGroupPage(condition, pagination);
        return queryData.convert(Group.class);
    }


    @RequestMapper("/add")
    @Validate(groups = ValidationType.Add.class)
    @Authentication("BASE_USER_GROUP_ADD")
    public Long add(Group group) throws BizException {
        GroupModel groupModel = JsonUtil.convert(group,GroupModel.class);
        return groupService.addGroup(groupModel);
    }

    @RequestMapper("/update")
    @Validate(groups = ValidationType.Update.class)
    @Authentication("BASE_USER_GROUP_UPDATE")
    public boolean update(Group group) throws BaseException {
        if (!ThreadContext.getUserInfo().isSuperAdmin()) {
            List<Long> groupIds = ThreadContext.getUserInfo().getGroupIds();
            if (!groupIds.contains(group.getGroupId())) {
                throw new BaseException(MessageCode.PERMISSION_DENIED);
            }
        }
        GroupModel groupModel = JsonUtil.convert(group,GroupModel.class);
        return groupService.updateGroup(groupModel);
    }

    @RequestMapper("/delete")
    @Validate
    @Authentication("BASE_USER_GROUP_DELETE")
    public boolean delete(@NotNull Long groupId) throws BaseException {
        if (!ThreadContext.getUserInfo().isSuperAdmin()) {
            List<Long> groupIds = ThreadContext.getUserInfo().getGroupIds();
            if (!groupIds.contains(groupId)) {
                throw new BaseException(MessageCode.PERMISSION_DENIED);
            }
        }
        return groupService.deleteGroup(groupId);
    }

    @RequestMapper("/batchDelete")
    @Validate
    @Authentication("BASE_USER_GROUP_BATCH_DELETE")
    public boolean batchDelete(@NotEmpty List<Long> groupIds) throws BaseException {
        if (!ThreadContext.getUserInfo().isSuperAdmin()) {
            List<Long> currentGroupIds = ThreadContext.getUserInfo().getGroupIds();
            if (!currentGroupIds.containsAll(groupIds)) {
                throw new BaseException(MessageCode.PERMISSION_DENIED);
            }
        }
        return groupService.batchDeleteGroup(groupIds);
    }

    @RequestMapper("/assignUsers")
    @Validate
    @Authentication("BASE_USER_GROUP_ASSIGN_USERS")
    public boolean assignUsers(@NotNull Long groupId, @NotEmpty List<Long> userIds) throws BaseException {
        if (!ThreadContext.getUserInfo().isSuperAdmin()) {
            List<Long> groupIds = ThreadContext.getUserInfo().getGroupIds();
            if (!groupIds.contains(groupId)) {
                throw new BaseException(MessageCode.PERMISSION_DENIED);
            }
        }
        return groupService.assignUsers(groupId, userIds);
    }

    @RequestMapper("/assignReresourceGroups")
    @Validate
    @Authentication("BASE_USER_GROUP_ASSIGN_RERESOURCE_GROUPS")
    public boolean assignReresourceGroups(@NotNull Long groupId, @NotEmpty List<Long> reresourceGroupIds) throws BaseException {
        if (!ThreadContext.getUserInfo().isSuperAdmin()) {
            List<Long> groupIds = ThreadContext.getUserInfo().getGroupIds();
            if (!groupIds.contains(groupId)) {
                throw new BaseException(MessageCode.PERMISSION_DENIED);
            }
        }
        return groupService.assignReresourceGroups(groupId, reresourceGroupIds);
    }
}
