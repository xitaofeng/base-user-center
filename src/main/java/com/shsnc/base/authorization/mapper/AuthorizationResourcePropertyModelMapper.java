package com.shsnc.base.authorization.mapper;

import com.shsnc.base.authorization.model.AuthorizationResourcePropertyModel;

import java.util.List;

/**
 * Created by Elena on 2017/6/9.
 */
public interface AuthorizationResourcePropertyModelMapper {

    public int addAuthorizationResourcePropertyModel(AuthorizationResourcePropertyModel authorizationResourcePropertyModel);

    public Integer editAuthorizationResourcePropertyModel(AuthorizationResourcePropertyModel authorizationResourcePropertyModel);

    public Integer batchDeleteAuthorizationResourceProperty(List<Long> idList);

    public AuthorizationResourcePropertyModel getAuthorizationResourcePropertyModelById(Long id);

    public List<AuthorizationResourcePropertyModel> getAuthorizationResourcePropertyModelList(AuthorizationResourcePropertyModel authorizationResourcePropertyModel);

}
