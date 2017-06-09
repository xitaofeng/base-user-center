package com.shsnc.base.user.handler;

import com.shsnc.api.core.RequestHandler;
import com.shsnc.api.core.annotation.RequestMapper;
import com.shsnc.api.core.validation.Validate;
import com.shsnc.api.core.validation.ValidationType;
import com.shsnc.base.user.bean.ExtendPropertyValue;
import com.shsnc.base.user.bean.ExtendPropertyValueParam;
import com.shsnc.base.user.model.ExtendPropertyValueModel;
import com.shsnc.base.user.service.ExtendPropertyValueService;
import com.shsnc.base.util.JsonUtil;
import com.shsnc.base.util.config.BizException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by houguangqiang on 2017/6/8.
 */
@Component
@RequestMapper("/user/extendPropertyValue")
public class ExtendPropertyValueHandler implements RequestHandler {

    @Autowired
    private ExtendPropertyValueService extendPropertyValueService;

    @RequestMapper("/add")
    @Validate(groups = ValidationType.Add.class)
    public Long add(ExtendPropertyValueParam extendPropertyValue) throws BizException {
        ExtendPropertyValueModel extendPropertyValueModel = JsonUtil.convert(extendPropertyValue, ExtendPropertyValueModel.class);
        return extendPropertyValueService.addExtendPropertyValue(extendPropertyValueModel);
    }

    @RequestMapper("/update")
    @Validate(groups = ValidationType.Update.class)
    public boolean update(ExtendPropertyValueParam extendPropertyValue) throws BizException {
        ExtendPropertyValueModel extendPropertyValueModel = JsonUtil.convert(extendPropertyValue, ExtendPropertyValueModel.class);
        return extendPropertyValueService.updateExtendPropertyValue(extendPropertyValueModel);
    }

    @RequestMapper("/delete")
    public boolean delete(@NotNull Long propertyValueId) throws BizException {
        return extendPropertyValueService.deleteExtendPropertyValue(propertyValueId);
    }

    @RequestMapper("/getExtendPropertyValueByUserId")
    public List<ExtendPropertyValue> getExtendPropertyValueByUserId(@NotNull Long userId){
        List<ExtendPropertyValueModel> extendPropertyValueModelList = extendPropertyValueService.getExtendPropertyValueByUserId(userId);
        return JsonUtil.convert(extendPropertyValueModelList, List.class, ExtendPropertyValue.class);
    }
}
