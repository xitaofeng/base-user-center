package com.shsnc.base.authorization.service;

import com.shsnc.api.core.annotation.RequestMapper;
import com.shsnc.base.authorization.bean.DataAuthorization;
import com.shsnc.base.authorization.mapper.AuthorizationResourceAuthModelMapper;
import com.shsnc.base.authorization.model.AuthorizationResourceAuthModel;
import com.shsnc.base.authorization.model.AuthorizationResourceAuthModel.EnumAuthType;
import com.shsnc.base.authorization.model.AuthorizationResourcePropertyModel;
import com.shsnc.base.util.config.BizException;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据权限服务
 * Created by Elena on 2017/6/9.
 */
@Service
public class DataAuthorizationService {

    @Autowired
    private AuthorizationResourcePropertyService authorizationResourcePropertyService;

    @Autowired
    private AuthorizationResourceAuthModelMapper authorizationResourceAuthModelMapper;

    /**
     * 用户授权
     *
     * @param resourceType   授权资源类型
     * @param userIdList     授权用户列表
     * @param propertyIdList 授权
     * @return
     */
    public boolean userAuth(Integer resourceType, List<Long> userIdList, List<Long> resourceIdList, List<Long> propertyIdList) throws BizException {
        if (CollectionUtils.isEmpty(propertyIdList)) {
            throw new BizException("选择授权的权限");
        }
        if (!CollectionUtils.isEmpty(userIdList)) {
            List<AuthorizationResourceAuthModel> authorizationResourceAuthModels = buildAuthorizationResourceAuthModel(resourceType, EnumAuthType.USER,userIdList,propertyIdList,resourceIdList);
            return authorizationResourceAuthModelMapper.addBatchAuthorizationResourceAuthModel(authorizationResourceAuthModels) > 0;
        }
        return true;
    }

    /**
     * 角色授权
     *
     * @param resourceType    资源类型
     * @param roleIdList
     * @param resourceIdList;
     * @param propertyIdList
     * @return
     */
    public boolean roleAuth(Integer resourceType, List<Long> roleIdList, List<Long> resourceIdList, List<Long> propertyIdList) throws BizException {
        if (CollectionUtils.isEmpty(propertyIdList)) {
            throw new BizException("选择授权的权限");
        }
        if (!CollectionUtils.isEmpty(roleIdList)) {
            List<AuthorizationResourceAuthModel> authorizationResourceAuthModels = buildAuthorizationResourceAuthModel(resourceType, EnumAuthType.ROLE,roleIdList,propertyIdList,resourceIdList);
            return authorizationResourceAuthModelMapper.addBatchAuthorizationResourceAuthModel(authorizationResourceAuthModels) > 0;
        }
        return true;
    }

    /**
     * 数据授权
     *
     * @param dataAuthorizations
     * @return
     */
    public boolean auth(List<DataAuthorization> dataAuthorizations) throws BizException {
        if (CollectionUtils.isEmpty(dataAuthorizations)) {
            throw new BizException("选择授权的数据");
        }
        for (int i = 0; i < dataAuthorizations.size(); i++) {
            DataAuthorization dataAuthorization = dataAuthorizations.get(i);
            Integer resourceType = dataAuthorization.getResourceType();
            Integer authType = dataAuthorization.getAuthType(); //授权类型
            List<Long> resourceIdList = dataAuthorization.getResourceIdList();
            List<Long> propertyIdList = dataAuthorization.getPropertyIdList();

            if (AuthorizationResourceAuthModel.EnumAuthType.USER.getValue() == authType) {
                userAuth(resourceType, dataAuthorization.getAuthValueList(), resourceIdList, propertyIdList);
            } else if (AuthorizationResourceAuthModel.EnumAuthType.ROLE.getValue() == authType) {
                roleAuth(resourceType, dataAuthorization.getAuthValueList(), resourceIdList, propertyIdList);
            }

        }
        return true;
    }

    /**
     * 构建添加的权限
     * @param resourceType
     * @param enumAuthType
     * @param authValues
     * @param propertyIds
     * @param resourceIds
     * @return
     * @throws BizException
     */
    private List<AuthorizationResourceAuthModel> buildAuthorizationResourceAuthModel(Integer resourceType, AuthorizationResourceAuthModel.EnumAuthType enumAuthType, List<Long> authValues, List<Long> propertyIds, List<Long> resourceIds) throws BizException {
        List<AuthorizationResourceAuthModel> authorizationResourceAuthModels = new ArrayList<>();
        for (int i = 0; i < authValues.size(); i++) {
            Long authValue = authValues.get(i);
            for (int j = 0; j < propertyIds.size(); j++) {
                Long propertyId = propertyIds.get(i);
                //propertyId 继承权限查询
                List<Long> ids = authorizationResourcePropertyService.getAuthorizationResourcePropertyModelPidById(propertyId);
                for (int k = 0; k < resourceIds.size(); k++) {
                    Long resourceId = resourceIds.get(k);
                    for (int l = 0; l < ids.size(); l++) {
                        AuthorizationResourceAuthModel authorizationResourceAuthModel = AuthorizationResourceAuthModel.createAuthorizationResourceAuthModel(resourceType, resourceId, ids.get(l), enumAuthType, authValue);
                        authorizationResourceAuthModels.add(authorizationResourceAuthModel);
                    }
                }
            }
        }
        //TODO 清理权限
        //TODO 清理redis 权限
        return authorizationResourceAuthModels;
    }
}
