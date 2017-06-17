package com.shsnc.base.user.service;

import com.shsnc.base.user.config.UserConstant;
import com.shsnc.base.user.mapper.ExtendPropertyModelMapper;
import com.shsnc.base.user.model.ExtendPropertyCondition;
import com.shsnc.base.user.model.ExtendPropertyModel;
import com.shsnc.base.user.support.Assert;
import com.shsnc.base.util.config.BizException;
import com.shsnc.base.util.sql.Pagination;
import com.shsnc.base.util.sql.QueryData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by houguangqiang on 2017/6/7.
 */
@Service
public class ExtendPropertyService {

    @Autowired
    private ExtendPropertyModelMapper extendPropertyModelMapper;

    /**
     * 分页查询扩展属性列表
     * @param condition 条件过滤
     * @param pagination 分页
     * @return 返回分页数据
     */
    public QueryData getExtendPropertyPage(ExtendPropertyCondition condition, Pagination pagination){
        QueryData queryData = new QueryData(pagination);
        int totalCount = extendPropertyModelMapper.getTotalCountByCondition(condition);
        queryData.setRowCount(totalCount);
        List<ExtendPropertyModel> list = extendPropertyModelMapper.getPageByCondition(condition, pagination);
        queryData.setRecords(list);
        return queryData;
    }

    /**
     * 获取所有的扩展属性列表
     * @return 返回所有的扩展属性列表
     */
    public List<ExtendPropertyModel> getExtendPropertyList(){
        return extendPropertyModelMapper.getExtendPropertyList();
    }

    /**
     * 添加一个用户扩展属性
     * @param extendPropertyModel 要添加的用户扩展属性
     * @return 返回新增数据的id
     * @throws BizException 业务异常
     */
    public Long addExtendProperty(ExtendPropertyModel extendPropertyModel) throws BizException {
        checkPropertyName(extendPropertyModel);
        extendPropertyModelMapper.insertSelective(extendPropertyModel);
        return extendPropertyModel.getPropertyId();
    }

    /**
     * 根据属性id更新属性，其中属性ropertyId必须提供
     * @param extendPropertyModel 要更新的用户扩展属性
     * @return 更新成功返回ture，否则返回false
     * @throws BizException 业务异常
     */
    public boolean updateExtendProperty(ExtendPropertyModel extendPropertyModel) throws BizException {
        Assert.notNull(extendPropertyModel.getPropertyId(), "属性id不能为空！");
        checkPropertyName(extendPropertyModel);
        return extendPropertyModelMapper.updateByPrimaryKeySelective(extendPropertyModel)>0;
    }

    /**
     * 删除某个属性
     * @param propertyId 属性id
     * @return  有数据更新返回ture，否则false
     */
    public boolean deleteExtendProperty(Long propertyId) throws BizException {
        Assert.notNull(propertyId, "属性id不能为空！");
        return extendPropertyModelMapper.deleteByPrimaryKey(propertyId)>0;
    }

    /**
     * 批量删除属性
     * @param propertyIds 属性id集合
     * @return 有数据更新返回ture，否则false
     */
    public boolean batchDeleteExtendProperty(List<Long> propertyIds) throws BizException {
        Assert.notEmpty(propertyIds, "属性id不能为空！");
        return extendPropertyModelMapper.deleteByPropertyIds(propertyIds);
    }
    /**
     * 校验属性名称
     * @param extendPropertyModel 属性名称
     * @throws BizException 业务异常
     */
    private void checkPropertyName(ExtendPropertyModel extendPropertyModel) throws BizException {
        String propertyName = extendPropertyModel.getPropertyName();
        Assert.notNull(propertyName,"属性名称不能为空！");

        Assert.isTrue(propertyName.matches(UserConstant.Regex.PROPERTY_NAME), "属性名称格式错误！");

        ExtendPropertyModel exist = new ExtendPropertyModel();
        exist.setPropertyName(propertyName);
        exist = extendPropertyModelMapper.existExtendProperty(exist);

        Assert.isTrue(exist == null || exist.getPropertyId().equals(extendPropertyModel.getPropertyId()), "属性名称已经存在！");

    }

    /**
     * 获取某个扩展属性
     * @param propertyId 属性id
     * @return 返回扩展属性
     * @throws BizException 业务异常
     */
    public ExtendPropertyModel getExtendProperty(Long propertyId) throws BizException {
        Assert.notNull(propertyId, "属性id不能为空！");
        return extendPropertyModelMapper.selectByPrimaryKey(propertyId);
    }

}
