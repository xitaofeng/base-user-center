package com.shsnc.base.authorization.service;

import com.shsnc.base.authorization.bean.DataAuthorization;
import com.shsnc.base.authorization.bean.UserDataAuthorization;
import com.shsnc.base.authorization.config.BeanUtils;
import com.shsnc.base.authorization.config.RedisConstants;
import com.shsnc.base.authorization.config.RedisUtils;
import com.shsnc.base.authorization.mapper.AuthorizationResourceAuthModelMapper;
import com.shsnc.base.authorization.model.AuthorizationResourceAuthModel;
import com.shsnc.base.authorization.model.AuthorizationResourceAuthModel.EnumAuthType;
import com.shsnc.base.util.RedisUtil;
import com.shsnc.base.util.StringUtil;
import com.shsnc.base.util.config.BizException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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

    @Autowired
    private UserModuleService userModuleService;

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
     * @param dataAuthorization
     * @return
     */
    @Transactional
    public boolean auth(DataAuthorization dataAuthorization) throws BizException {
        if (dataAuthorization == null) {
            throw new BizException("选择授权的数据");
        }

        Integer resourceType = dataAuthorization.getResourceType();
        List<Long> resourceIdList = dataAuthorization.getResourceIdList();
        List<Long> propertyIdList = dataAuthorization.getPropertyIdList();

        if(!CollectionUtils.isEmpty(dataAuthorization.getAuthUserList())){
            userAuth(resourceType, dataAuthorization.getAuthUserList(), resourceIdList, propertyIdList);
        }

        if(!CollectionUtils.isEmpty(dataAuthorization.getAuthRoleList())){
            userAuth(resourceType, dataAuthorization.getAuthRoleList(), resourceIdList, propertyIdList);
        }

        return true;
    }

    /**
     * 用户资源权限值查询
     *
     * @param userId
     * @param resourceType
     * @param resourceId
     * @return
     */
    public Integer getAuthValue(Long userId, Integer resourceType, Long resourceId) {
        Integer authorizationValue = 0;
        //根据key 直接取用户对应资源的权限值 取不到加载用户权限数据
        String key = RedisUtils.buildRedisKey(RedisConstants.userResourceDataAuthorizationKey(userId), resourceType.toString(), resourceId.toString());
        String propertyValue = RedisUtil.getString(key);
        if (StringUtil.isNotEmpty(propertyValue)) {
            authorizationValue = Integer.parseInt(propertyValue);
        } else {
            Map<String, String> dataAuthorizationMap = getDataAuthorization(userId);
            if (!CollectionUtils.isEmpty(dataAuthorizationMap)) {
                propertyValue = dataAuthorizationMap.get(RedisUtils.buildRedisKey(resourceType.toString(), resourceId.toString()));
                if (StringUtil.isNotEmpty(propertyValue)) {
                    authorizationValue = Integer.parseInt(propertyValue);
                }
            }
        }
        return authorizationValue;
    }

    /**
     * 获取用户 所有的资源权限
     *
     * @return
     */
    public Map<String, String> getDataAuthorization(Long userId) {
        Map<String, String> dataAuthorizationMap = new HashMap<>();
        //读取redis 数据不存在 重新加载
        String resourceDataAuthorizationKey = RedisConstants.userResourceDataAuthorizationKey(userId);
        dataAuthorizationMap = RedisUtil.getMap(resourceDataAuthorizationKey);

        if (CollectionUtils.isEmpty(dataAuthorizationMap)) {
            //加载数据 map 数据格式 Map<userId,dataAuthorizationMap>
            List<Long> roleIds = userModuleService.getRoleIdsByUserId(userId);
            List<UserDataAuthorization> userDataAuthorizations = authorizationResourceAuthModelMapper.getUserDataAuthorization(userId, roleIds);
            for (UserDataAuthorization userDataAuthorization : userDataAuthorizations) {
                String key = RedisUtils.buildRedisKey(userDataAuthorization.getResourceType().toString(), userDataAuthorization.getResourceId().toString());
                String value = userDataAuthorization.getPropertyValue().toString();
                dataAuthorizationMap.put(key, value);
            }
            RedisUtil.saveMap(resourceDataAuthorizationKey, dataAuthorizationMap);
        }
        return dataAuthorizationMap;
    }


    /**
     * 获取资源 所有的用户权限
     *
     * @return
     */
    public Map<String, String> getDataAuthorization(Integer resourceType, Long resourceId) {
        Map<String, String> dataAuthorizationMap = new HashMap<>();
        //读取redis 数据不存在 重新加载
        String resourceDataAuthorizationKey = RedisConstants.resourceDataAuthorizationKey(resourceType, resourceId);
        dataAuthorizationMap = RedisUtil.getMap(resourceDataAuthorizationKey);

        if (CollectionUtils.isEmpty(dataAuthorizationMap)) {
            //加载数据 map 数据格式 Map<userId,dataAuthorizationMap>
            // TODO 根据资源类型和资源id 获取 资源所用户的用户权限
            RedisUtil.saveMap(resourceDataAuthorizationKey, dataAuthorizationMap);
        }
        return dataAuthorizationMap;
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

        for (Long resourceId : resourceIds) {
            //清理redis 权限
            String resourceDataAuthorization = RedisConstants.resourceDataAuthorizationKey(resourceType, resourceId);
            RedisUtil.remove(resourceDataAuthorization);

            for (Long authValue : authValues) {
                //清理权限
                deleteResourceDataAuthorization(enumAuthType, authValue, resourceType, resourceId);

                for (Long propertyId : propertyIds) {
                    //propertyId 继承权限查询
                    List<Long> ids = authorizationResourcePropertyService.getAuthorizationResourcePropertyModelPidById(propertyId);

                    for (Long id : ids) {
                        AuthorizationResourceAuthModel authorizationResourceAuthModel = AuthorizationResourceAuthModel.createAuthorizationResourceAuthModel(resourceType, resourceId, id, enumAuthType, authValue);
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
