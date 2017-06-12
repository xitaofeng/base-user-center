package com.shsnc.base.authorization.mapper;

import com.shsnc.base.authorization.model.AuthorizationInfoModel;
import com.shsnc.base.authorization.model.AuthorizationResourceAuthModel;

import java.util.List;

/**
 * Created by Elena on 2017/6/9.
 */
public interface AuthorizationResourceAuthModelMapper {

    Long addAuthorizationResourceAuthModel(AuthorizationResourceAuthModel authorizationResourceAuthModel);

    Integer addBatchAuthorizationResourceAuthModel(List<AuthorizationResourceAuthModel> authorizationResourceAuthModels);

}
