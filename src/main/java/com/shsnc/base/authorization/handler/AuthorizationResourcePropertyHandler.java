package com.shsnc.base.authorization.handler;

import com.shsnc.api.core.RequestHandler;
import com.shsnc.api.core.annotation.Authentication;
import com.shsnc.api.core.annotation.RequestMapper;
import com.shsnc.api.core.validation.Validate;
import com.shsnc.api.core.validation.ValidationType;
import com.shsnc.base.authorization.bean.AuthorizationResourceProperty;
import com.shsnc.base.authorization.model.AuthorizationResourcePropertyModel;
import com.shsnc.base.authorization.model.condition.AuthorizationResourcePropertyCondition;
import com.shsnc.base.authorization.model.condition.AuthorizationRoleCondition;
import com.shsnc.base.authorization.service.AuthorizationResourcePropertyService;
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
    public Long addAuthorizationResourceProperty(AuthorizationResourceProperty authorizationResourceProperty) throws Exception {
        AuthorizationResourcePropertyModel authorizationResourcePropertyModel = new AuthorizationResourcePropertyModel();
        BeanUtils.copyProperties(authorizationResourceProperty, authorizationResourcePropertyModel);
        return authorizationResourcePropertyService.addAuthorizationResourcePropertyModel(authorizationResourcePropertyModel);
    }

    @RequestMapper("/edit")
    @Validate(groups = ValidationType.Update.class)
    public boolean editAuthorizationResourceProperty(AuthorizationResourceProperty authorizationResourceProperty) throws Exception {
        AuthorizationResourcePropertyModel authorizationResourcePropertyModel = new AuthorizationResourcePropertyModel();
        BeanUtils.copyProperties(authorizationResourceProperty, authorizationResourcePropertyModel);
        return authorizationResourcePropertyService.editAuthorizationResourcePropertyModel(authorizationResourcePropertyModel);
    }

    @RequestMapper("/delete")
    @Validate
    public boolean deleteAuthorizationResourceProperty(@NotNull Long id) throws Exception {
        List<Long> idList = new ArrayList<>();
        idList.add(id);
        return authorizationResourcePropertyService.batchDeleteAuthorizationRole(idList);
    }

    @RequestMapper("/batch/delete")
    @Validate
    public boolean batchDeleteAuthorizationResourceProperty(@NotEmpty List<Long> idList) throws Exception {
        return authorizationResourcePropertyService.batchDeleteAuthorizationRole(idList);
    }

    @RequestMapper("/list")
    @Validate
    public List<AuthorizationResourceProperty> getAuthorizationList(AuthorizationResourcePropertyCondition condition) throws Exception {
        List<AuthorizationResourcePropertyModel> list = authorizationResourcePropertyService.getAuthorizationResourcePropertyModelList(condition);
        List<AuthorizationResourceProperty> result = new ArrayList<>();
        if (!CollectionUtils.isEmpty(list)) {
            list.forEach(item -> {
                AuthorizationResourceProperty tempAuthorizationResourceProperty = new AuthorizationResourceProperty();
                BeanUtils.copyProperties(item, tempAuthorizationResourceProperty);
                result.add(tempAuthorizationResourceProperty);
            });
        }
        return result;
    }

    @RequestMapper("/page/list")
    public QueryData getPageList(AuthorizationResourcePropertyCondition condition, Pagination pagination) {
        pagination.buildSort(filedMapping);
        QueryData queryData = authorizationResourcePropertyService.getAuthorizationPageList(condition, pagination);
        return queryData.convert(AuthorizationResourceProperty.class);
    }

    @RequestMapper("/one")
    @Validate
    public AuthorizationResourceProperty getAuthorizationByAuthorizationId(@NotNull Long id) throws Exception {
        AuthorizationResourceProperty authorizationInfo = new AuthorizationResourceProperty();
        AuthorizationResourcePropertyModel authorizationResourcePropertyModel = authorizationResourcePropertyService.getAuthorizationResourcePropertyModelById(id);
        if (authorizationResourcePropertyModel != null) {
            BeanUtils.copyProperties(authorizationResourcePropertyModel, authorizationInfo);
        }
        return authorizationInfo;
    }
}
