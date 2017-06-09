package com.shsnc.base.user.service;

import com.shsnc.base.user.config.UserConstant;
import com.shsnc.base.user.mapper.GroupModelMapper;
import com.shsnc.base.user.mapper.GroupStructureModelMapper;
import com.shsnc.base.user.model.GroupModel;
import com.shsnc.base.user.support.Assert;
import com.shsnc.base.user.support.helper.BeanHelper;
import com.shsnc.base.util.config.BizException;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by houguangqiang on 2017/6/7.
 */
@Service
public class GroupService {

    @Autowired
    private GroupModelMapper groupModelMapper;
    @Autowired
    private GroupStructureModelMapper groupStructureModelMapper;

    /**
     * 新增用户组
     * @param groupModel 用户组model
     * @param parentId 上级用户组，没有则为null
     * @return 返回新增记录id
     * @throws BizException 业务异常
     */
    public Long addGroup(GroupModel groupModel,Long parentId) throws BizException {
        Assert.notNull(groupModel);
        checkName(groupModel);
        checkCode(groupModel);
        if(parentId != null){
            GroupModel parentGroup = groupModelMapper.selectByPrimaryKey(parentId);
            Assert.notNull(parentGroup, "上级用户组id不存在！");
        }
        if(!Integer.valueOf(UserConstant.GROUP_STATUS_DISABLED).equals(groupModel.getStatus())){
            groupModel.setStatus(UserConstant.GROUP_STATUS_ENABLED);
        }
        // 添加组信息
        groupModelMapper.insertSelective(groupModel);

        // 添加组结构关系
        groupStructureModelMapper.insertGroupStructure(groupModel.getGroupId(),parentId);

        return groupModel.getGroupId();
    }

    /**
     * 更新用户组信息
     * @param groupModel 用户组model
     * @param parentId 上级用户组，不改变则为null
     * @return 如果有记录更细返回true，否则返回false
     * @throws BizException 业务异常
     */
    public boolean updateGroup(GroupModel groupModel,Long parentId) throws BizException {
        Assert.notNull(groupModel);
        Assert.notNull(groupModel.getGroupId());
        checkName(groupModel);
        checkCode(groupModel);
        GroupModel dbGroupModel = groupModelMapper.selectByPrimaryKey(groupModel.getGroupId());
        Assert.notNull(dbGroupModel,"用户组id不存在！");

        // 新增节点信息
        BeanHelper.populateNullProperties(dbGroupModel, groupModel);
        groupModelMapper.updateByPrimaryKeySelective(groupModel);

        // 更新父节点关系
        if(parentId != null){
            Long dbParentId = groupStructureModelMapper.getParentIdByGroupId(groupModel.getGroupId());
            if(parentId != dbParentId){
                // 断开与所有祖先节点的关系
                if(dbParentId != null){
                    groupStructureModelMapper.deleteOldRelation(groupModel.getGroupId());
                }
                // 连接与新的父节点的关系
                groupStructureModelMapper.insertNewRelation(groupModel.getGroupId(), parentId);
            }
        }
        return true;
    }

    /**
     * 只删除当前用户组，所有后代节点相对根节点的层数都会少一，即所有子节点向根节点方向移动一层
     * @param groupId 用户组id
     * @return 如果有记录更细返回true，否则返回false
     */
    public boolean deleteGroup(Long groupId) throws BizException {
        Assert.notNull(groupId,"用户组id不能为空！");
        GroupModel dbGroupModel = groupModelMapper.selectByPrimaryKey(groupId);
        Assert.notNull(dbGroupModel,"用户组id不存在！");
        // 更新要删除节点的后代节点与祖先节点的层级减去1
        groupStructureModelMapper.updateChildrenLevel(groupId);
        // 删除用户组
        groupModelMapper.deleteByPrimaryKey(groupId);
        // 删除节点关系
        groupStructureModelMapper.deleteGroupStructure(groupId);
        return true;
    }

    /**
     * 删除用户组以及它的所有后台节点
     * @param groupId 用户组id
     * @return 如果有记录更细返回true，否则返回false
     */
    public boolean deleteGroupTree(Long groupId) throws BizException {
        Assert.notNull(groupId,"用户组id不能为空！");
        GroupModel dbGroupModel = groupModelMapper.selectByPrimaryKey(groupId);
        Assert.notNull(dbGroupModel,"用户组id不存在！");
        // 删除用户组以及它的所有后台节点
        groupModelMapper.deleteGroupAndChildren(groupId);
        // 删除节点关系
        groupStructureModelMapper.deleteGroupAndChildrenRelation(groupId);
        return true;
    }

    private void checkName(GroupModel groupModel) throws BizException {
        Long groupId = groupModel.getGroupId();
        String name = groupModel.getName();
        if(groupId != null){
            Assert.notNull(name, "用户组名称不能为空！");
        }
    }

    private void checkCode(GroupModel groupModel) throws BizException {
        Long groupId = groupModel.getGroupId();
        String code = groupModel.getCode();
        if(groupId == null){
            Assert.notNull(code, "用户组编码不能为空！");
        }
        if (code != null){
            GroupModel exist = new GroupModel();
            exist.setCode(code);
            exist = groupModelMapper.selectOneGroup(exist);
            Assert.isTrue(exist == null || exist.getGroupId().equals(groupId), "用户组编码已经存在！");
        }
    }

    public GroupModel exitsGroup(GroupModel groupModel){
        return groupModelMapper.selectOneGroup(groupModel);
    }

    public boolean exitsByGroupIds(List<Long> groupIds){
        if(CollectionUtils.isEmpty(groupIds)){
            return true;
        }
        List<Long> dbGroupIds = groupModelMapper.getGroupIdsByGroupIds(new ArrayList<>(new HashSet<>(groupIds)));
        return dbGroupIds.size() == groupIds.size();
    }

}
