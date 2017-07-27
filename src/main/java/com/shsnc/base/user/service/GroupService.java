package com.shsnc.base.user.service;

import com.shsnc.base.user.bean.GroupCondition;
import com.shsnc.base.user.mapper.GroupModelMapper;
import com.shsnc.base.user.model.GroupModel;
import com.shsnc.base.util.BizAssert;
import com.shsnc.base.util.config.BizException;
import com.shsnc.base.util.sql.Pagination;
import com.shsnc.base.util.sql.QueryData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

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

    /**
     * 获取所有用户组
     * @return 返回用户组列表
     */
    public List<GroupModel> getGroupList() {
        return groupModelMapper.selectAll();
    }

    /**
     * 根据id获取用户组信息
     * @param groupId 用户组id
     * @return 用户组model
     */
    public GroupModel getGroup(Long groupId) throws BizException {
        BizAssert.notNull(groupId, "用户组id不能为空！");
        return groupModelMapper.selectByPrimaryKey(groupId);
    }

    /**
     * 用户组分页查询
     * @param condition 查询条件
     * @param pagination 分页对象
     * @return 分页信息
     */
    public QueryData getGroupPage(GroupCondition condition, Pagination pagination) {
        QueryData queryData = new QueryData(pagination);
        int totalCount = groupModelMapper.getTotalCountByCondition(condition);
        queryData.setRowCount(totalCount);
        List<GroupModel> list = groupModelMapper.getPageByCondition(condition, pagination);
        queryData.setRecords(list);
        return queryData;
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
        return group.getGroupId();
    }

    /**
     * 更新用户组
     * @param group 用户组
     * @return 如果有记录更细返回true，否则返回false
     */
    public boolean updateGroup(GroupModel group) throws BizException {
        BizAssert.notNull(group.getGroupId(), "用户组id不能为空！");
        checkInput(group);
        return groupModelMapper.updateByPrimaryKeySelective(group) > 0;
    }

    /**
     * 根据id删除用户组
     * @param groupId 用户组id
     * @return 如果有记录更细返回true，否则返回false
     */
    public boolean deleteGroup(Long groupId) throws BizException {
        BizAssert.notNull(groupId, "用户组id不能为空！");
        return groupModelMapper.deleteByPrimaryKey(groupId) > 0;
    }

    /**
     * 根据id集合批量删除用户组
     * @param groupIds 用户组id的集合
     * @return 如果有记录更细返回true，否则返回false
     */
    public boolean batchDeleteGroup(List<Long> groupIds) throws BizException {
        BizAssert.notEmpty(groupIds, "用户组id不能为空！");
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
}
