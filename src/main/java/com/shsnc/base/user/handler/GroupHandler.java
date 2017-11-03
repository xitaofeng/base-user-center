package com.shsnc.base.user.handler;

import com.shsnc.api.core.RequestHandler;
import com.shsnc.api.core.annotation.Authentication;
import com.shsnc.api.core.annotation.LoginRequired;
import com.shsnc.api.core.annotation.RequestMapper;
import com.shsnc.api.core.util.LogRecord;
import com.shsnc.api.core.util.LogWriter;
import com.shsnc.api.core.validation.Validate;
import com.shsnc.api.core.validation.ValidationType;
import com.shsnc.base.constants.LogConstant;
import com.shsnc.base.user.bean.Group;
import com.shsnc.base.user.model.GroupModel;
import com.shsnc.base.user.model.condition.GroupCondition;
import com.shsnc.base.user.service.GroupService;
import com.shsnc.base.util.JsonUtil;
import com.shsnc.base.util.config.BaseException;
import com.shsnc.base.util.config.BizException;
import com.shsnc.base.util.sql.Pagination;
import com.shsnc.base.util.sql.QueryData;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
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
        List<GroupModel> groupList = groupService.getGroupList(condition);
        return JsonUtil.convert(groupList, List.class, Group.class) ;
    }

    private static final String[][] mapping = {{"name","name"}};

    @RequestMapper("/getObject")
    @Validate
    @Authentication("BASE_USER_GROUP_GET_OBJECT")
    public Group getObject(@NotNull Long groupId) throws BaseException {
        GroupModel groupModel = groupService.getGroup(groupId);
        List<GroupModel> groupModels = Collections.singletonList(groupModel);
        groupService.selectUsers(groupModels);
        groupService.selectResourceGroups(groupModels);
        return JsonUtil.convert(groupModel, Group.class);
    }

    @RequestMapper("/getPage")
    @Authentication("BASE_USER_GROUP_GET_PAGE")
    public QueryData getPage(GroupCondition condition, Pagination pagination) throws BizException {
        pagination.buildSort(mapping);
        QueryData queryData = groupService.getGroupPage(condition, pagination);
        return queryData.convert(Group.class);
    }


    @RequestMapper("/add")
    @Validate(groups = ValidationType.Add.class)
    @Authentication("BASE_USER_GROUP_ADD")
    public Long add(Group group) throws BizException {
        GroupModel groupModel = JsonUtil.convert(group,GroupModel.class);
        LogRecord logRecord = new LogRecord(LogConstant.Module.USER, LogConstant.Action.ADD);
        logRecord.setDescription(String.format("新增用户组【%s】", groupModel.getName()));
        LogWriter.writeLog(logRecord);
        return groupService.addGroup(groupModel);
    }

    @RequestMapper("/update")
    @Validate(groups = ValidationType.Update.class)
    @Authentication("BASE_USER_GROUP_UPDATE")
    public boolean update(Group group) throws BaseException {
        GroupModel groupModel = JsonUtil.convert(group,GroupModel.class);
        LogRecord logRecord = new LogRecord(LogConstant.Module.USER, LogConstant.Action.UPDATE);
        logRecord.setDescription(String.format("更新用户组【%s】", groupModel.getName()));
        LogWriter.writeLog(logRecord);
        return groupService.updateGroup(groupModel);
    }

    @RequestMapper("/delete")
    @Validate
    @Authentication("BASE_USER_GROUP_DELETE")
    public boolean delete(@NotNull Long groupId) throws BaseException {
        GroupModel group = groupService.getGroup(groupId);
        if (group != null) {
            LogRecord logRecord = new LogRecord(LogConstant.Module.USER, LogConstant.Action.DELETE);
            logRecord.setDescription(String.format("删除用户组【%s】", group.getName()));
            LogWriter.writeLog(logRecord);
        }
        return groupService.deleteGroup(groupId);
    }

    @RequestMapper("/batchDelete")
    @Validate
    @Authentication("BASE_USER_GROUP_BATCH_DELETE")
    public boolean batchDelete(@NotEmpty List<Long> groupIds) throws BaseException {
        List<GroupModel> groupModels = groupService.getByGroupIds(groupIds);
        if (!groupModels.isEmpty()) {
            LogRecord logRecord = new LogRecord(LogConstant.Module.USER, LogConstant.Action.DELETE);
            logRecord.setDescription(String.format("批量删除用户组【%s】", groupModels.stream().map(GroupModel::getName).reduce("】【", String::concat)));
            LogWriter.writeLog(logRecord);
        }
        return groupService.batchDeleteGroup(groupIds);
    }

    @RequestMapper("/assignUsers")
    @Validate
    @Authentication("BASE_USER_GROUP_ASSIGN_USERS")
    public boolean assignUsers(@NotNull Long groupId, @NotNull List<Long> userIds) throws BaseException {
        GroupModel group = groupService.getGroup(groupId);
        if (group != null) {
            LogRecord logRecord = new LogRecord(LogConstant.Module.USER, LogConstant.Action.UPDATE);
            logRecord.setDescription(String.format("给用户组【%s】分配用户", group.getName()));
            LogWriter.writeLog(logRecord);
        }
        return groupService.assignUsers(groupId, userIds);
    }

    @RequestMapper("/assignReresourceGroups")
    @Validate
    @Authentication("BASE_USER_GROUP_ASSIGN_RERESOURCE_GROUPS")
    public boolean assignReresourceGroups(@NotNull Long groupId, @NotNull List<Long> reresourceGroupIds) throws BaseException {
        GroupModel group = groupService.getGroup(groupId);
        if (group != null) {
            LogRecord logRecord = new LogRecord(LogConstant.Module.USER, LogConstant.Action.UPDATE);
            logRecord.setDescription(String.format("给用户组【%s】分配资源组", group.getName()));
            LogWriter.writeLog(logRecord);
        }
        return groupService.assignReresourceGroups(groupId, reresourceGroupIds);
    }
}
