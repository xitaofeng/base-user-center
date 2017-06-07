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

    public List<ExtendPropertyModel> getExtendPropertyList(){
        return extendPropertyModelMapper.getExtendPropertyList();
    }

    public boolean addExtendProperty(ExtendPropertyModel extendPropertyModel) throws BizException {
        checkExtendProperty(extendPropertyModel);
        return extendPropertyModelMapper.insertSelective(extendPropertyModel)>0;
    }

    public boolean updateExtendProperty(ExtendPropertyModel extendPropertyModel) throws BizException {
        if(extendPropertyModel.getPropertyId() == null){
            throw new BizException("属性id不能为空！");
        }
        checkExtendProperty(extendPropertyModel);
        return extendPropertyModelMapper.updateByPrimaryKeySelective(extendPropertyModel)>0;
    }

    public boolean deleteExtendProperty(Long propertyId){
        return extendPropertyModelMapper.deleteByPrimaryKey(propertyId)>0;
    }

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
