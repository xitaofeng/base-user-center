package com.shsnc.base.authorization.service;

import com.shsnc.base.authorization.bean.DataAuthorization;
import com.shsnc.base.authorization.config.BeanUtils;
import com.shsnc.base.authorization.config.RedisConstants;
import com.shsnc.base.authorization.config.RedisUtils;
import com.shsnc.base.authorization.mapper.AuthorizationResourceAuthModelMapper;
import com.shsnc.base.authorization.model.AuthorizationResourceAuthModel;
import com.shsnc.base.authorization.model.AuthorizationResourceAuthModel.EnumAuthType;
import com.shsnc.base.util.RedisUtil;
import com.shsnc.base.util.config.BizException;
import jdk.nashorn.internal.ir.ReturnNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Transactional
    public boolean userAuth(Integer resourceType, List<Long> userIdList, List<Long> resourceIdList, List<Long> propertyIdList) throws BizException {
        if (CollectionUtils.isEmpty(propertyIdList)) {
            throw new BizException("选择授权的权限");
        }
        if (!CollectionUtils.isEmpty(userIdList)) {
            List<AuthorizationResourceAuthModel> authorizationResourceAuthModels = buildAuthorizationResourceAuthModel(resourceType, EnumAuthType.USER, userIdList, propertyIdList, resourceIdList);
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
    @Transactional
    public boolean roleAuth(Integer resourceType, List<Long> roleIdList, List<Long> resourceIdList, List<Long> propertyIdList) throws BizException {
        if (CollectionUtils.isEmpty(propertyIdList)) {
            throw new BizException("选择授权的权限");
        }
        if (!CollectionUtils.isEmpty(roleIdList)) {
            List<AuthorizationResourceAuthModel> authorizationResourceAuthModels = buildAuthorizationResourceAuthModel(resourceType, EnumAuthType.ROLE, roleIdList, propertyIdList, resourceIdList);
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
    @Transactional
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
     * 用户权限值查询
     *
     * @param userId
     * @param resourceType
     * @param resourceId
     * @return
     */
    public Integer getAuthValue(Long userId, Integer resourceType, Long resourceId) {
        Integer authorizationValue = 0;
        Map<String, String> dataAuthorizationMap = getDataAuthorization(resourceType, resourceId);
        if (!CollectionUtils.isEmpty(dataAuthorizationMap)) {
            authorizationValue = new Integer(dataAuthorizationMap.get(userId.toString()));
        }
        return authorizationValue;
    }

    /**
     * 获取数据权限 数据
     *
     * @param resourceType
     * @param resourceId
     * @return
     */
    public Map<String, String> getDataAuthorization(Integer resourceType, Long resourceId) {
        Map<String, String> dataAuthorizationMap = new HashMap<>();
        String resourceDataAuthorization = buildResourceDataAuthorizationKey(resourceType, resourceId);

        dataAuthorizationMap = RedisUtil.getMap(resourceDataAuthorization);

        if (CollectionUtils.isEmpty(dataAuthorizationMap)) {
            //TODO 加载数据 map 数据格式 Map<userId,authorizationValue>
            RedisUtil.saveMap(resourceDataAuthorization, dataAuthorizationMap);
        }
        return dataAuthorizationMap;
    }

    /**
     * 构建资源数据权限key
     *
     * @param resourceType
     * @param resourceId
     * @return
     */
    private String buildResourceDataAuthorizationKey(Integer resourceType, Long resourceId) {
        return RedisUtils.buildRedisKey(RedisConstants.RESOURCE_DATA_AUTHORIZATION, resourceType.toString(), resourceId.toString());
    }

    /**
     * 构建添加的权限
     *
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

        for (int i = 0; i < resourceIds.size(); i++) {
            Long resourceId = resourceIds.get(i);
            //清理redis 权限
            String resourceDataAuthorization = buildResourceDataAuthorizationKey(resourceType, resourceId);
            RedisUtil.remove(resourceDataAuthorization);

            for (int k = 0; k < authValues.size(); k++) {
                Long authValue = authValues.get(k);

                //清理权限
                deleteResourceDataAuthorization(enumAuthType, authValue, resourceType, resourceId);

                for (int j = 0; j < propertyIds.size(); j++) {
                    Long propertyId = propertyIds.get(j);
                    //propertyId 继承权限查询
                    List<Long> ids = authorizationResourcePropertyService.getAuthorizationResourcePropertyModelPidById(propertyId);

                    for (int l = 0; l < ids.size(); l++) {
                        AuthorizationResourceAuthModel authorizationResourceAuthModel = AuthorizationResourceAuthModel.createAuthorizationResourceAuthModel(resourceType, resourceId, ids.get(l), enumAuthType, authValue);
                        authorizationResourceAuthModels.add(authorizationResourceAuthModel);
                    }
                }
            }
        }
        return BeanUtils.removeDuplicate(authorizationResourceAuthModels);
    }

    /**
     * 清理权限
     *
     * @param enumAuthType
     * @param authValue
     * @param resourceType
     * @param resourceId
     * @return
     */
    private Integer deleteResourceDataAuthorization(EnumAuthType enumAuthType, Long authValue, Integer resourceType, Long resourceId) {
        return authorizationResourceAuthModelMapper.deleteResourceDataAuthorization(enumAuthType.getValue(), authValue, resourceType, resourceId);
    }
}
