package com.shsnc.base.authorization.handler;

import com.shsnc.api.core.RequestHandler;
import com.shsnc.api.core.annotation.RequestMapper;
import com.shsnc.api.core.validation.Validate;
import com.shsnc.base.authorization.bean.AuthorizationResourceProperty;
import com.shsnc.base.authorization.model.AuthorizationResourcePropertyModel;
import com.shsnc.base.authorization.service.AuthorizationResourcePropertyService;
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

    @Autowired
    private AuthorizationResourcePropertyService authorizationResourcePropertyService;

    @RequestMapper("/add")
    @Validate
    public Long addAuthorizationResourceProperty(@NotNull Integer resourceType, @NotNull String resourceTypeName,
                                                 @NotNull String propertyName, @NotNull Integer propertyValue, Long parentId) throws Exception {
        AuthorizationResourcePropertyModel authorizationResourcePropertyModel = new AuthorizationResourcePropertyModel();
        authorizationResourcePropertyModel.setResourceType(resourceType);
        authorizationResourcePropertyModel.setResourceTypeName(resourceTypeName);
        authorizationResourcePropertyModel.setPropertyName(propertyName);
        authorizationResourcePropertyModel.setPropertyValue(propertyValue);
        authorizationResourcePropertyModel.setParentId(parentId);
        return authorizationResourcePropertyService.addAuthorizationResourcePropertyModel(authorizationResourcePropertyModel);
    }

    @RequestMapper("/edit")
    @Validate
    public boolean editAuthorizationResourceProperty(@NotNull Long id, @NotNull Integer resourceType, @NotNull String resourceTypeName,
                                                     @NotNull String propertyName, @NotNull Integer propertyValue, Long parentId) throws Exception {
        AuthorizationResourcePropertyModel authorizationResourcePropertyModel = new AuthorizationResourcePropertyModel();
        authorizationResourcePropertyModel.setId(id);
        authorizationResourcePropertyModel.setResourceType(resourceType);
        authorizationResourcePropertyModel.setResourceTypeName(resourceTypeName);
        authorizationResourcePropertyModel.setPropertyName(propertyName);
        authorizationResourcePropertyModel.setPropertyValue(propertyValue);
        authorizationResourcePropertyModel.setParentId(parentId);
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
    public List<AuthorizationResourceProperty> getAuthorizationList(Integer resourceType, String resourceTypeName,
                                                                    String propertyName, Integer propertyValue, Long parentId) throws Exception {
        AuthorizationResourceProperty authorizationResourceProperty = new AuthorizationResourceProperty();
        authorizationResourceProperty.setResourceType(resourceType);
        authorizationResourceProperty.setResourceTypeName(resourceTypeName);
        authorizationResourceProperty.setPropertyName(propertyName);
        authorizationResourceProperty.setPropertyValue(propertyValue);
        authorizationResourceProperty.setParentId(parentId);

        AuthorizationResourcePropertyModel authorizationResourcePropertyModel = new AuthorizationResourcePropertyModel();
        BeanUtils.copyProperties(authorizationResourceProperty, authorizationResourcePropertyModel);

        List<AuthorizationResourcePropertyModel> list = authorizationResourcePropertyService.getAuthorizationResourcePropertyModelList(authorizationResourcePropertyModel);
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
