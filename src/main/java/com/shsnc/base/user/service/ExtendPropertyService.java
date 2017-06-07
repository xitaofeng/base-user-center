package com.shsnc.base.user.service;

import com.shsnc.base.user.config.UserConstant;
import com.shsnc.base.user.mapper.ExtendPropertyModelMapper;
import com.shsnc.base.user.model.ExtendPropertyCondition;
import com.shsnc.base.user.model.ExtendPropertyModel;
import com.shsnc.base.util.config.BizException;
import com.shsnc.base.util.sql.Pagination;
import com.shsnc.base.util.sql.QueryData;
import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIXDom;
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
        QueryData queryData = new QueryData();
        queryData.setPageSize(pagination.getPageSize());
        queryData.setCurrPage(pagination.getCurrentPage());
        int totalCount = extendPropertyModelMapper.getTotalCountByCondition(condition);
        queryData.setTotalCount(totalCount);
        queryData.build();
        List<ExtendPropertyModel> list = extendPropertyModelMapper.getPageByCondition(condition, pagination);
        queryData.setDataList(list);
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
     * @return 新增成功返回ture，否则返回false
     * @throws BizException 业务异常
     */
    public boolean addExtendProperty(ExtendPropertyModel extendPropertyModel) throws BizException {
        checkExtendProperty(extendPropertyModel);
        return extendPropertyModelMapper.insertSelective(extendPropertyModel)>0;
    }

    /**
     * 根据属性id更新属性，其中属性ropertyId必须提供
     * @param extendPropertyModel 要更新的用户扩展属性
     * @return 更新成功返回ture，否则返回false
     * @throws BizException 业务异常
     */
    public boolean updateExtendProperty(ExtendPropertyModel extendPropertyModel) throws BizException {
        if(extendPropertyModel.getPropertyId() == null){
            throw new BizException("属性id不能为空！");
        }
        checkExtendProperty(extendPropertyModel);
        return extendPropertyModelMapper.updateByPrimaryKeySelective(extendPropertyModel)>0;
    }

    /**
     * 删除某个属性
     * @param propertyId 属性id
     * @return  删除成功返回true，否则false
     */
    public boolean deleteExtendProperty(Long propertyId){
        return extendPropertyModelMapper.deleteByPrimaryKey(propertyId)>0;
    }

    /**
     * 校验属性名称
     * @param extendPropertyModel 属性名称
     * @throws BizException 业务异常
     */
    private void checkExtendProperty(ExtendPropertyModel extendPropertyModel) throws BizException {
        String propertyName = extendPropertyModel.getPropertyName();
        if(propertyName == null){
            throw new BizException("属性名称不能为空！");
        }
        if(!propertyName.matches(UserConstant.Regex.PROPERTY_NAME)){
            throw new BizException("属性名称格式错误！");
        }
        ExtendPropertyModel exist = new ExtendPropertyModel();
        exist.setPropertyName(propertyName);
        exist = extendPropertyModelMapper.existExtendProperty(exist);

        if(exist != null && (extendPropertyModel.getPropertyId() == null || !extendPropertyModel.getPropertyId().equals(exist.getPropertyId()))){
            throw new BizException("属性名称已经存在！");
        }
    }


}
