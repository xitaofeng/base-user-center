package com.shsnc.base.authorization.mapper;

import com.shsnc.base.authorization.model.AuthorizationResourcePropertyModel;
import com.shsnc.base.authorization.model.AuthorizationRoleModel;

import java.util.List;

/**
 * Created by Elena on 2017/6/9.
 */
public interface AuthorizationResourcePropertyModelMapper {

    public Long addAuthorizationResourcePropertyModel(AuthorizationResourcePropertyModel authorizationResourcePropertyModel);

    public Integer editAuthorizationResourcePropertyModel(AuthorizationResourcePropertyModel authorizationResourcePropertyModel);

    public Integer batchDeleteAuthorizationResourceProperty(List<Long> idList);

    public AuthorizationResourcePropertyModel getAuthorizationResourcePropertyModelById(Long id);

    public List<AuthorizationResourcePropertyModel> getAuthorizationResourcePropertyModelList(AuthorizationResourcePropertyModel authorizationResourcePropertyModel);

}
