package com.shsnc.base.user.handler;

import com.shsnc.api.core.RequestHandler;
import com.shsnc.api.core.annotation.RequestMapper;
import com.shsnc.api.core.validation.Validate;
import com.shsnc.api.core.validation.ValidationType;
import com.shsnc.base.user.bean.Group;
import com.shsnc.base.user.bean.GroupParam;
import com.shsnc.base.user.model.GroupCondition;
import com.shsnc.base.user.model.GroupModel;
import com.shsnc.base.user.service.GroupService;
import com.shsnc.base.util.JsonUtil;
import com.shsnc.base.util.config.BizException;
import com.shsnc.base.util.sql.Pagination;
import com.shsnc.base.util.sql.QueryData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by houguangqiang on 2017/6/9.
 */
@Component
@RequestMapper("/user/group")
public class GroupHandler implements RequestHandler {

    @Autowired
    private GroupService groupService;

    @RequestMapper("/getPage")
    public QueryData getPage(GroupCondition condition, Pagination pagination){
        QueryData queryData = groupService.getGroupPage(condition,pagination);
        queryData.setDataList(JsonUtil.convert(queryData.getDataList(),List.class,Group.class));
        return queryData;
    }

    @RequestMapper("/getObject")
    @Validate
    public Group getObjet(@NotNull Long groupId) throws BizException {
        GroupModel groupModel = groupService.getGroup(groupId);
        return JsonUtil.convert(groupModel, Group.class);
    }

    @RequestMapper("/getNodeList")
    public List<Group> getNodeList(Long parentId){
        List<GroupModel> groupModels = groupService.getNodeList(parentId);
        return JsonUtil.convert(groupModels, List.class, Group.class);
    }


    @RequestMapper("/add")
    @Validate(groups = ValidationType.Add.class)
    public Long add(GroupParam group) throws BizException {
        GroupModel groupModel = JsonUtil.convert(group, GroupModel.class);
        return groupService.addGroup(groupModel, group.getParentId());
    }

    @RequestMapper("/update")
    @Validate(groups = ValidationType.Update.class)
    public boolean update(GroupParam group) throws BizException {
        GroupModel groupModel = JsonUtil.convert(group, GroupModel.class);
        return groupService.updateGroup(groupModel, group.getParentId());
    }

    @RequestMapper("/delete")
    public boolean delete(@NotNull Long groupId) throws BizException {
        return groupService.deleteGroup(groupId);
    }

    @RequestMapper("/deleteTree")
    public boolean deleteTree(@NotNull Long groupId) throws BizException {
        return groupService.deleteGroupTree(groupId);
    }

}
