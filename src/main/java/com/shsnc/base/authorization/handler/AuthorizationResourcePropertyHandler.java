package com.shsnc.base.authorization.handler;

import com.shsnc.api.core.RequestHandler;
import com.shsnc.api.core.annotation.Authentication;
import com.shsnc.api.core.annotation.RequestMapper;
import com.shsnc.api.core.validation.Validate;
import com.shsnc.api.core.validation.ValidationType;
import com.shsnc.base.authorization.bean.AuthorizationResourceProperty;
import com.shsnc.base.authorization.bean.AuthorizationResourcePropertyParam;
import com.shsnc.base.authorization.config.DictionaryConstant;
import com.shsnc.base.authorization.model.AuthorizationResourcePropertyModel;
import com.shsnc.base.authorization.model.condition.AuthorizationResourcePropertyCondition;
import com.shsnc.base.authorization.service.AuthorizationResourcePropertyService;
import com.shsnc.base.module.base.dictionary.DictionaryMapInfo;
import com.shsnc.base.module.config.DictionaryService;
import com.shsnc.base.util.JsonUtil;
import com.shsnc.base.util.config.BizException;
import com.shsnc.base.util.sql.Pagination;
import com.shsnc.base.util.sql.QueryData;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据权限 资源属性管理
 * Created by Elena on 2017/6/12.
 */
@Component
@RequestMapper("/authorization/resource/property")
public class AuthorizationResourcePropertyHandler implements RequestHandler {

    private String[][] filedMapping = {{"propertyName", "property_name"}, {"resourceType", "resource_type"}, {"resourceTypeName", "resource_type_name"}};

    @Autowired
    private AuthorizationResourcePropertyService authorizationResourcePropertyService;

    @RequestMapper("/add")
    @Validate(groups = ValidationType.Add.class)
    @Authentication("BASE_USER_CENTER_AUTHORIZATION_RESOURCE_PROPERTY_ADD")
    public Long addAuthorizationResourceProperty(AuthorizationResourcePropertyParam authorizationResourceProperty) throws Exception {

        AuthorizationResourcePropertyModel authorizationResourcePropertyModel = getAuthorizationResourcePropertyModel(authorizationResourceProperty);

        return authorizationResourcePropertyService.addAuthorizationResourcePropertyModel(authorizationResourcePropertyModel);
    }

    @RequestMapper("/edit")
    @Validate(groups = ValidationType.Update.class)
    @Authentication("BASE_USER_CENTER_AUTHORIZATION_RESOURCE_PROPERTY_UPDATE")
    public boolean editAuthorizationResourceProperty(AuthorizationResourcePropertyParam authorizationResourceProperty) throws Exception {

        AuthorizationResourcePropertyModel authorizationResourcePropertyModel = getAuthorizationResourcePropertyModel(authorizationResourceProperty);

        return authorizationResourcePropertyService.editAuthorizationResourcePropertyModel(authorizationResourcePropertyModel);
    }

    @RequestMapper("/delete")
    @Validate
    @Authentication("BASE_USER_CENTER_AUTHORIZATION_RESOURCE_PROPERTY_DELETE")
    public boolean deleteAuthorizationResourceProperty(@NotNull Long id) throws Exception {
        List<Long> idList = new ArrayList<>();
        idList.add(id);
        return authorizationResourcePropertyService.batchDeleteAuthorizationRole(idList);
    }

    @RequestMapper("/batch/delete")
    @Validate
    @Authentication("BASE_USER_CENTER_AUTHORIZATION_RESOURCE_PROPERTY_DELETE_BATCH")
    public boolean batchDeleteAuthorizationResourceProperty(@NotEmpty List<Long> idList) throws Exception {
        return authorizationResourcePropertyService.batchDeleteAuthorizationRole(idList);
    }

    @RequestMapper("/list")
    @Validate
    @Authentication("BASE_USER_CENTER_AUTHORIZATION_RESOURCE_PROPERTY_GET_LIST")
    public List<AuthorizationResourceProperty> getAuthorizationList(AuthorizationResourcePropertyCondition condition) throws Exception {
        List<AuthorizationResourcePropertyModel> list = authorizationResourcePropertyService.getAuthorizationResourcePropertyModelList(condition);
        return  JsonUtil.convert(list,List.class,AuthorizationResourceProperty.class);
    }

    @RequestMapper("/page/list")
    @Authentication("BASE_USER_CENTER_AUTHORIZATION_RESOURCE_PROPERTY_GET_PAGE_LIST")
    public QueryData getPageList(AuthorizationResourcePropertyCondition condition, Pagination pagination) {
        pagination.buildSort(filedMapping);
        QueryData queryData = authorizationResourcePropertyService.getAuthorizationPageList(condition, pagination);
        return queryData.convert(AuthorizationResourceProperty.class);
    }

    @RequestMapper("/one")
    @Validate
    @Authentication("BASE_USER_CENTER_AUTHORIZATION_RESOURCE_PROPERTY_GET_ONE")
    public AuthorizationResourceProperty getAuthorizationByAuthorizationId(@NotNull Long id) throws Exception {
        AuthorizationResourceProperty authorizationInfo = new AuthorizationResourceProperty();
        AuthorizationResourcePropertyModel authorizationResourcePropertyModel = authorizationResourcePropertyService.getAuthorizationResourcePropertyModelById(id);
        if (authorizationResourcePropertyModel != null) {
            BeanUtils.copyProperties(authorizationResourcePropertyModel, authorizationInfo);
        }
        return authorizationInfo;
    }


    /**
     * 添加编辑的时候构建 权限资源属性
     * @param authorizationResourceProperty
     * @return
     */
    private AuthorizationResourcePropertyModel getAuthorizationResourcePropertyModel(AuthorizationResourcePropertyParam authorizationResourceProperty) throws BizException {
        AuthorizationResourcePropertyModel authorizationResourcePropertyModel = new AuthorizationResourcePropertyModel();
        BeanUtils.copyProperties(authorizationResourceProperty, authorizationResourcePropertyModel);


        Integer resourceType = null;
        String resourceTypeName = "";
        DictionaryMapInfo typeDictionaryMapInfo = DictionaryService.getDictionaryMap(DictionaryConstant.ATM_CODE, DictionaryConstant.DATA_AUTHORIZATION_RESOURCE_TYPE, authorizationResourceProperty.getResourceTypeCode());
        if (typeDictionaryMapInfo != null) {
            resourceType = Integer.parseInt(typeDictionaryMapInfo.getMapValue());
            resourceTypeName = typeDictionaryMapInfo.getMapDesc();
        }
        authorizationResourcePropertyModel.setResourceType(resourceType);
        authorizationResourcePropertyModel.setResourceTypeName(resourceTypeName);

        String propertyName = "";
        String propertyValue = "";
        DictionaryMapInfo dictionaryMapInfo = DictionaryService.getDictionaryMap(DictionaryConstant.ATM_CODE, DictionaryConstant.DATA_AUTHORIZATION, authorizationResourceProperty.getResourcePropertyCode());
        if (dictionaryMapInfo != null) {
            propertyValue = dictionaryMapInfo.getMapValue();
            propertyName = dictionaryMapInfo.getMapDesc();
        }

        authorizationResourcePropertyModel.setPropertyName(propertyName);
        authorizationResourcePropertyModel.setPropertyValue(propertyValue);
        return authorizationResourcePropertyModel;
    }
}
