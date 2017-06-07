package com.shsnc.base.user.handler;

import com.shsnc.api.core.RequestHandler;
import com.shsnc.api.core.annotation.RequestMapper;
import com.shsnc.base.user.model.ExtendPropertyModel;
import com.shsnc.base.user.service.ExtendPropertyService;
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

    public List<ExtendPropertyModel> getExtendPropertyList(){
        return  extendPropertyService.getExtendPropertyList();
    }
}
