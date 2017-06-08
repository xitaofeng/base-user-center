package com.shsnc.base.user.handler;

import com.shsnc.api.core.RequestHandler;
import com.shsnc.api.core.annotation.RequestMapper;
import com.shsnc.api.core.validation.Validate;
import com.shsnc.api.core.validation.ValidationType;
import com.shsnc.base.user.bean.ExtendProperty;
import com.shsnc.base.user.model.ExtendPropertyCondition;
import com.shsnc.base.user.model.ExtendPropertyModel;
import com.shsnc.base.user.service.ExtendPropertyService;
import com.shsnc.base.util.JsonUtil;
import com.shsnc.base.util.config.BizException;
import com.shsnc.base.util.sql.Pagination;
import com.shsnc.base.util.sql.QueryData;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by houguangqiang on 2017/6/7.
 */
@Component
@RequestMapper("/user/extendProperty")
public class ExtendPropertyHandler implements RequestHandler {

    @Autowired
    private ExtendPropertyService extendPropertyService;

    @RequestMapper("/list")
    public List<ExtendProperty> list(){
        List<ExtendPropertyModel> extendPropertyList = extendPropertyService.getExtendPropertyList();
        return JsonUtil.convert(extendPropertyList, List.class, ExtendProperty.class) ;
    }

    @RequestMapper("/page")
    public QueryData page(ExtendPropertyCondition condition, Pagination pagination){
        QueryData queryData = extendPropertyService.getExtendPropertyPage(condition, pagination);
        queryData.setDataList(JsonUtil.convert(queryData.getDataList(),List.class,ExtendProperty.class));
        return queryData;
    }

    @RequestMapper("/getObject")
    public ExtendProperty getObject(@NotEmpty Long propertyId) throws BizException {
        ExtendPropertyModel extendPropertyModel = extendPropertyService.getExtendProperty(propertyId);
        return JsonUtil.convert(extendPropertyModel, ExtendProperty.class);
    }

    @RequestMapper("/add")
    @Validate(groups = ValidationType.Add.class)
    public Long add(ExtendProperty extendProperty) throws BizException {
        ExtendPropertyModel extendPropertyModel = JsonUtil.convert(extendProperty,ExtendPropertyModel.class);
        return extendPropertyService.addExtendProperty(extendPropertyModel);
    }

    @RequestMapper("/update")
    @Validate(groups = ValidationType.Update.class)
    public boolean update(ExtendProperty extendProperty) throws BizException {
        ExtendPropertyModel extendPropertyModel = JsonUtil.convert(extendProperty,ExtendPropertyModel.class);
        return extendPropertyService.updateExtendProperty(extendPropertyModel);
    }

    @RequestMapper("/delete")
    @Validate
    public boolean delete(@NotEmpty Long propertyId) throws BizException {
        return extendPropertyService.deleteExtendProperty(propertyId);
    }

}
