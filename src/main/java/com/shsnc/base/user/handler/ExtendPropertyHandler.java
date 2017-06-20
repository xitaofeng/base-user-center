package com.shsnc.base.user.handler;

import com.shsnc.api.core.RequestHandler;
import com.shsnc.api.core.annotation.Authentication;
import com.shsnc.api.core.annotation.LoginRequired;
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

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by houguangqiang on 2017/6/7.
 */
@Component
@RequestMapper("/user/extendProperty")
@LoginRequired
public class ExtendPropertyHandler implements RequestHandler {

    @Autowired
    private ExtendPropertyService extendPropertyService;

    @RequestMapper("/getList")
    @Authentication("BASE_USER_EXTEND_PROPERTY_GET_LIST")
    public List<ExtendProperty> getList(){
        List<ExtendPropertyModel> extendPropertyList = extendPropertyService.getExtendPropertyList();
        return JsonUtil.convert(extendPropertyList, List.class, ExtendProperty.class) ;
    }

    private  String[][] mapping = {{"propertyName","property_name"}};

    @RequestMapper("/getObject")
    @Validate
    @Authentication("BASE_USER_EXTEND_PROPERTY_GET_OBJECT")
    public ExtendProperty getObject(@NotNull Long propertyId) throws BizException {
        ExtendPropertyModel extendPropertyModel = extendPropertyService.getExtendProperty(propertyId);
        return JsonUtil.convert(extendPropertyModel, ExtendProperty.class);
    }

    @RequestMapper("/getPage")
    @Authentication("BASE_USER_EXTEND_PROPERTY_GET_PAGE")
    public QueryData getPage(ExtendPropertyCondition condition, Pagination pagination){
        pagination.buildSort(mapping);
        QueryData queryData = extendPropertyService.getExtendPropertyPage(condition, pagination);
        return queryData.convert(ExtendProperty.class);
    }


    @RequestMapper("/add")
    @Validate(groups = ValidationType.Add.class)
    @Authentication("BASE_USER_EXTEND_PROPERTY_ADD")
    public Long add(ExtendProperty extendProperty) throws BizException {
        ExtendPropertyModel extendPropertyModel = JsonUtil.convert(extendProperty,ExtendPropertyModel.class);
        return extendPropertyService.addExtendProperty(extendPropertyModel);
    }

    @RequestMapper("/update")
    @Validate(groups = ValidationType.Update.class)
    @Authentication("BASE_USER_EXTEND_PROPERTY_UPDATE")
    public boolean update(ExtendProperty extendProperty) throws BizException {
        ExtendPropertyModel extendPropertyModel = JsonUtil.convert(extendProperty,ExtendPropertyModel.class);
        return extendPropertyService.updateExtendProperty(extendPropertyModel);
    }

    @RequestMapper("/delete")
    @Validate
    @Authentication("BASE_USER_EXTEND_PROPERTY_DELETE")
    public boolean delete(@NotNull Long propertyId) throws BizException {
        return extendPropertyService.deleteExtendProperty(propertyId);
    }
    @RequestMapper("/batchDelete")
    @Validate
    @Authentication("BASE_USER_EXTEND_PROPERTY_BATCH_DELETE")
    public boolean batchDelete(@NotEmpty List<Long> propertyIds) throws BizException {
        return extendPropertyService.batchDeleteExtendProperty(propertyIds);
    }

}
