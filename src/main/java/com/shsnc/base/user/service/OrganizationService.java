package com.shsnc.base.user.service;

import com.shsnc.base.user.mapper.OrganizationModelMapper;
import com.shsnc.base.user.mapper.OrganizationStructureModelMapper;
import com.shsnc.base.user.mapper.UserInfoOrganizationRelationModelMapper;
import com.shsnc.base.user.model.condition.OrganizationCondition;
import com.shsnc.base.user.model.OrganizationModel;
import com.shsnc.base.user.support.Assert;
import com.shsnc.base.user.support.helper.BeanHelper;
import com.shsnc.base.util.config.BizException;
import com.shsnc.base.util.sql.Pagination;
import com.shsnc.base.util.sql.QueryData;
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
public class OrganizationService {

    @Autowired
    private OrganizationModelMapper organizationModelMapper;
    @Autowired
    private OrganizationStructureModelMapper organizationStructureModelMapper;
    @Autowired
    private UserInfoOrganizationRelationModelMapper userInfoOrganizationRelationModelMapper;

    /**
     * 新增组织部门
     * @param organizationModel 组织部门model
     * @param parentId 上级组织部门，没有则为null
     * @return 返回新增记录id
     * @throws BizException 业务异常
     */
    public Long addOrganization(OrganizationModel organizationModel, Long parentId) throws BizException {
        Assert.notNull(organizationModel);
        checkName(organizationModel);
        checkCode(organizationModel);
        if(parentId != null){
            OrganizationModel parentOrganization = organizationModelMapper.selectByPrimaryKey(parentId);
            Assert.notNull(parentOrganization, "上级组织部门id不存在！");
        }
        // 添加组信息
        organizationModelMapper.insertSelective(organizationModel);

        // 添加组结构关系
        organizationStructureModelMapper.insertOrganizationStructure(organizationModel.getOrganizationId(),parentId);

        return organizationModel.getOrganizationId();
    }

    /**
     * 更新组织部门信息
     * @param organizationModel 组织部门model
     * @param parentId 上级组织部门id，不改变则为null
     * @return 如果有记录更细返回true，否则返回false
     * @throws BizException 业务异常
     */
    public boolean updateOrganization(OrganizationModel organizationModel, Long parentId) throws BizException {
        Assert.notNull(organizationModel);
        Assert.notNull(organizationModel.getOrganizationId());
        checkName(organizationModel);
        checkCode(organizationModel);
        OrganizationModel dbOrganizationModel = organizationModelMapper.selectByPrimaryKey(organizationModel.getOrganizationId());
        Assert.notNull(dbOrganizationModel,"组织部门id不存在！");

        // 更新节点信息
        BeanHelper.populateNullProperties(dbOrganizationModel, organizationModel);
        organizationModelMapper.updateByPrimaryKeySelective(organizationModel);

        // 更新父节点关系
        Long dbParentId = organizationStructureModelMapper.getParentIdByOrganizationId(organizationModel.getOrganizationId());
        if(parentId != null){
            // 跟旧的父节点不一样才需要进行节点移动
            if(!parentId.equals(dbParentId)){
                // 父节点不能等于自己
                Assert.isTrue(!organizationModel.getOrganizationId().equals(parentId), "不能指定自己为上级部门！");
                // 不能指定子节点为父节点
                List<Long> childIds = organizationStructureModelMapper.getChildIdsByOrganizationId(organizationModel.getOrganizationId());
                Assert.isTrue(!childIds.contains(parentId), "不能直接指定下级部门作为自己的上级部门！");

                // 断开与所有祖先节点的关系
                if(dbParentId != null){
                    organizationStructureModelMapper.deleteOldRelation(organizationModel.getOrganizationId());
                }
                // 连接与新的父节点的关系
                organizationStructureModelMapper.insertNewRelation(organizationModel.getOrganizationId(), parentId);
            }
        } else if (dbParentId != null) {
            // 子节点变成根节点
            organizationStructureModelMapper.deleteOldRelation(organizationModel.getOrganizationId());
        }
        return true;
    }

    /**
     * 只删除当前组织部门，所有后代节点相对根节点的层数都会少一，即所有子节点向根节点方向移动一层
     * @param organizationId 组织部门id
     * @return 如果有记录更细返回true，否则返回false
     */
    public boolean deleteOrganization(Long organizationId) throws BizException {
        Assert.notNull(organizationId,"组织部门id不能为空！");
        OrganizationModel dbOrganizationModel = organizationModelMapper.selectByPrimaryKey(organizationId);
        Assert.notNull(dbOrganizationModel,"组织部门id不存在！");
        // 删除与用户的关系
        userInfoOrganizationRelationModelMapper.deleteByOrganizationId(organizationId);
        // 更新要删除节点的后代节点与祖先节点的层级减去1
        organizationStructureModelMapper.updateChildrenLevel(organizationId);
        // 删除组织部门信息
        organizationModelMapper.deleteByPrimaryKey(organizationId);
        // 删除节点关系
        organizationStructureModelMapper.deleteOrganizationStructure(organizationId);

        return true;
    }

    /**
     * 删除组织部门以及它的所有后台节点
     * @param organizationId 组织部门id
     * @return 如果有记录更细返回true，否则返回false
     */
    public boolean deleteOrganizationTree(Long organizationId) throws BizException {
        Assert.notNull(organizationId,"组织部门id不能为空！");
        OrganizationModel dbOrganizationModel = organizationModelMapper.selectByPrimaryKey(organizationId);
        Assert.notNull(dbOrganizationModel,"组织部门id不存在！");
        // 删除组织部门以及它的所有后台节点
        organizationModelMapper.deleteOrganizationAndChildren(organizationId);
        // 删除组织部门与用户的关系
        userInfoOrganizationRelationModelMapper.deleteWithChildrenByOrganizationId(organizationId);
        // 删除节点关系
        organizationStructureModelMapper.deleteOrganizationAndChildrenRelation(organizationId);
        return true;
    }

    private void checkName(OrganizationModel organizationModel) throws BizException {
        Long organizationId = organizationModel.getOrganizationId();
        String name = organizationModel.getName();
        if(organizationId == null){
            Assert.notNull(name, "组织部门名称不能为空！");
        }
    }

    private void checkCode(OrganizationModel organizationModel) throws BizException {
        Long organizationId = organizationModel.getOrganizationId();
        String code = organizationModel.getCode();
        if(organizationId == null){
            Assert.notNull(code, "组织部门编码不能为空！");
        }
        if (code != null){
            OrganizationModel exist = new OrganizationModel();
            exist.setCode(code);
            exist = organizationModelMapper.selectOne(exist);
            Assert.isTrue(exist == null || exist.getOrganizationId().equals(organizationId), "组织部门编码已经存在！");
        }
    }

    public OrganizationModel exitsOrganization(OrganizationModel organizationModel){
        return organizationModelMapper.selectOne(organizationModel);
    }

    public boolean exitsByOrganizationIds(List<Long> organizationIds){
        if(CollectionUtils.isEmpty(organizationIds)){
            return true;
        }
        List<Long> dbOrganizationIds = organizationModelMapper.getOrganizationIdsByOrganizationIds(new ArrayList<>(new HashSet<>(organizationIds)));
        return dbOrganizationIds.size() == organizationIds.size();
    }

    /**
     * 根据用户id返回用户拥有的组织部门id以及所有的后代组织部门id
     * @param userId 用户id
     * @return 用于拥有的组织部门id以及所有后代组织部门id列表
     * @throws BizException 业务异常
     */
    public List<Long> getAllOrganizationIdsByUserId(Long userId) throws BizException {
        Assert.notNull(userId,"用户id不能为空！");
        return organizationModelMapper.getAllOrganizationIdsByUserId(userId);
    }


    public OrganizationModel getOrganization(Long organizationId) throws BizException {
        Assert.notNull(organizationId, "组织部门id不能为空！");
        OrganizationModel organizationModel = organizationModelMapper.selectByPrimaryKey(organizationId);
        organizationModel.setParentId(organizationStructureModelMapper.getParentIdByOrganizationId(organizationId));
        return organizationModel;
    }

    public QueryData getOrganizationPage(OrganizationCondition condition, Pagination pagination) {
        QueryData queryData = new QueryData(pagination);
        int totalCount = organizationModelMapper.getTotalCountByCondition(condition);
        queryData.setRowCount(totalCount);
        List<OrganizationModel> list = organizationModelMapper.getPageByCondition(condition, pagination);
        queryData.setRecords(list);
        return queryData;
    }

    public List<OrganizationModel> getNodeList(Long parentId) {
        return  organizationModelMapper.getOrganizationNodeList(parentId);
    }

    public List<OrganizationModel> getTreeList(Long parentId) {
        return organizationModelMapper.getOrganizationTree(parentId);
    }

    public OrganizationModel getOrganizationByUserId(Long userId) {
        Long organizationId = userInfoOrganizationRelationModelMapper.getOrganizationIdByUserId(userId);
        if (organizationId != null) {
            return organizationModelMapper.selectByPrimaryKey(organizationId);
        }
        return null;
    }
}
