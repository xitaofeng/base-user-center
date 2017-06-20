package com.shsnc.base.user.service;

import com.shsnc.base.user.BaseUserTest;
import com.shsnc.base.user.model.ExtendPropertyCondition;
import com.shsnc.base.user.model.ExtendPropertyModel;
import com.shsnc.base.util.JsonUtil;
import com.shsnc.base.util.sql.Pagination;
import com.shsnc.base.util.sql.QueryData;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by houguangqiang on 2017/6/7.
 */
@Rollback(true)
@Component
public class ExtendPropertyServiceTest extends BaseUserTest {

    @Autowired
    private ExtendPropertyService extendPropertyService;

    @Test
    public void getExtendPropertyPage() throws Exception {
        ExtendPropertyCondition condition = new ExtendPropertyCondition();
        condition.setPropertyName("年");
        Pagination pagination = new Pagination();
        QueryData queryData = extendPropertyService.getExtendPropertyPage(condition, pagination);
        System.out.println(JsonUtil.toJsonString(queryData));
    }

    @Test
    public void getExtendPropertyList() throws Exception {
        List<ExtendPropertyModel> result = extendPropertyService.getExtendPropertyList();
        System.out.println(JsonUtil.toJsonString(result));
        assertTrue(result.size()>1);
    }

    @Test
    public void addExtendProperty() throws Exception {
        ExtendPropertyModel extendPropertyModel = new ExtendPropertyModel();
        extendPropertyModel.setPropertyName("test");
        assertTrue(extendPropertyService.addExtendProperty(extendPropertyModel) != null);
    }

    @Test
    public void updateExtendProperty() throws Exception {
        ExtendPropertyModel extendPropertyModel = new ExtendPropertyModel();
        extendPropertyModel.setPropertyId(1L);
        extendPropertyModel.setPropertyName("性别");
        assertTrue(extendPropertyService.updateExtendProperty(extendPropertyModel));
    }

    @Test
    public void deleteExtendProperty() throws Exception {
        assertTrue(extendPropertyService.deleteExtendProperty(1L));
    }

    @Test
    public void getExtendProperty() throws Exception {
        ExtendPropertyModel extendProperty = extendPropertyService.getExtendProperty(1L);
        System.out.println(JsonUtil.toJsonString(extendProperty));
    }
}