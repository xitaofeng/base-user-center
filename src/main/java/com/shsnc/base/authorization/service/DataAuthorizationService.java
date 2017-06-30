package com.shsnc.base.authorization.service;

import com.shsnc.base.authorization.bean.DataAuthorization;
import com.shsnc.base.authorization.bean.UserDataAuthorization;
import com.shsnc.base.authorization.config.BeanUtils;
import com.shsnc.base.authorization.config.RedisConstants;
import com.shsnc.base.authorization.config.RedisUtils;
import com.shsnc.base.authorization.mapper.AuthorizationResourceAuthModelMapper;
import com.shsnc.base.authorization.mapper.AuthorizationUserRoleRelationModelMapper;
import com.shsnc.base.authorization.model.AuthorizationResourceAuthModel;
import com.shsnc.base.authorization.model.AuthorizationResourceAuthModel.EnumAuthType;
import com.shsnc.base.util.JsonUtil;
import com.shsnc.base.util.RedisUtil;
import com.shsnc.base.util.StringUtil;
import com.shsnc.base.util.config.BizException;
import com.shsnc.base.utils.DataAuthorizationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    Logger LOG = LoggerFactory.getLogger(DataAuthorizationService.class);

    @Autowired
    private AuthorizationResourcePropertyService authorizationResourcePropertyService;

    @Autowired
    private AuthorizationResourceAuthModelMapper authorizationResourceAuthModelMapper;

    @Autowired
    private UserModuleService userModuleService;

    @Autowired
    private AuthorizationUserRoleRelationModelMapper authorizationUserRoleRelationModelMapper;

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
            if (authorizationResourceAuthModelMapper.addBatchAuthorizationResourceAuthModel(authorizationResourceAuthModels) > 0) {
                RedisConstants.romverUserDataAuthorization(userIdList, resourceType);
            }
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
            if (authorizationResourceAuthModelMapper.addBatchAuthorizationResourceAuthModel(authorizationResourceAuthModels) > 0) {
                List<Long> userIdList = authorizationUserRoleRelationModelMapper.getUserIdByRoleIds(roleIdList);
                RedisConstants.romverUserDataAuthorization(userIdList, resourceType);
            }
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

        if (!CollectionUtils.isEmpty(dataAuthorization.getAuthUserList())) {
            userAuth(resourceType, dataAuthorization.getAuthUserList(), resourceIdList, propertyIdList);
        }

        if (!CollectionUtils.isEmpty(dataAuthorization.getAuthRoleList())) {
            userAuth(resourceType, dataAuthorization.getAuthRoleList(), resourceIdList, propertyIdList);
        }

        return true;
    }


    /**
     * 根据用户id和资源类型获取 资源类型下所有有权限的资源数据及权限值
     *
     * @param userId
     * @param resourceType
     * @param authorizationPropertyValue 权限属性值
     * @return
     */
    public Map<String, Integer> getUserResourceTypeAutValuehList(Long userId, Integer resourceType, Integer authorizationPropertyValue) {
        String redisKey = RedisUtils.buildRedisKey(RedisConstants.userResourceDataAuthorizationKey(userId), resourceType.toString());
        String value = RedisUtil.getString(redisKey);

        Map<String, Integer> map = new HashMap<>();
        if (StringUtil.isNotEmpty(value)) {
            map = JsonUtil.jsonToObject(value, Map.class, String.class, Integer.class);
        } else {
            Map<String, String> dataAuthorizationMap = getDataAuthorization(userId);
            if (!CollectionUtils.isEmpty(dataAuthorizationMap)) {
                value = dataAuthorizationMap.get(resourceType.toString());
                if (StringUtil.isNotEmpty(value)) {
                    map = JsonUtil.jsonToObject(value, Map.class, String.class, Integer.class);
                }
            }
        }

        //根据权限属性过滤
        if (authorizationPropertyValue != null && authorizationPropertyValue != 0) {
            for (String key : map.keySet()) {
                List<DataAuthorizationUtils.EnumDataAuthorization> list = DataAuthorizationUtils.analysisAuthorizationValue(map.get(key));
                if (!list.contains(authorizationPropertyValue)) {
                    map.remove(key);
                }
            }
        }

        return map;
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
        String key = RedisUtils.buildRedisKey(RedisConstants.userResourceDataAuthorizationKey(userId), resourceType.toString());
        String value = RedisUtil.getString(key);
        if (StringUtil.isNotEmpty(value)) {
            if (StringUtil.isNotEmpty(value)) {
                Map<String, Integer> map = JsonUtil.jsonToObject(value, Map.class, String.class, Integer.class);
                authorizationValue = map.get(resourceId.toString());
            }
        } else {
            Map<String, String> dataAuthorizationMap = getDataAuthorization(userId);
            if (!CollectionUtils.isEmpty(dataAuthorizationMap)) {
                value = dataAuthorizationMap.get(resourceType.toString());
                if (StringUtil.isNotEmpty(value)) {
                    Map<String, Integer> map = JsonUtil.jsonToObject(value, Map.class, String.class, Integer.class);
                    authorizationValue = map.get(resourceId.toString());
                }
            }
        }
        return authorizationValue;
    }


    /**
     * 用户和资源类型获取 下面所有资源列表的权限值列表
     *
     * @param userId
     * @param resourceType
     * @return
     */

    public Integer getAuthorizationByResourceType(Long userId, Integer resourceType) {
        Integer authorizationValue = 0;
        //根据key 直接取用户对应资源的权限值 取不到加载用户权限数据
        String key = RedisUtils.buildRedisKey(RedisConstants.userResourceDataAuthorizationKey(userId), resourceType.toString());
        String propertyValue = RedisUtil.getString(key);
        if (StringUtil.isNotEmpty(propertyValue)) {
            authorizationValue = Integer.parseInt(propertyValue);
        } else {
            Map<String, String> dataAuthorizationMap = getDataAuthorization(userId);
            if (!CollectionUtils.isEmpty(dataAuthorizationMap)) {
                propertyValue = dataAuthorizationMap.get(RedisUtils.buildRedisKey(resourceType.toString()));
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
        Map<String, String> dataAuthorizationMap = new HashMap<String, String>();
        //读取redis 数据不存在 重新加载
        String resourceDataAuthorizationKey = RedisConstants.userResourceDataAuthorizationKey(userId);
        dataAuthorizationMap = RedisUtil.getMap(resourceDataAuthorizationKey);

        if (CollectionUtils.isEmpty(dataAuthorizationMap)) {
            //加载数据 map 数据格式 Map<userId,dataAuthorizationMap>
            List<Long> roleIds = userModuleService.getRoleIdsByUserId(userId);
            List<UserDataAuthorization> userDataAuthorizations = authorizationResourceAuthModelMapper.getUserDataAuthorization(userId, roleIds);

            Map<String, Integer> map = new HashMap<>();
            for (UserDataAuthorization userDataAuthorization : userDataAuthorizations) {
                String resourceType = userDataAuthorization.getResourceType().toString();
                String resourceId = userDataAuthorization.getResourceId().toString();
                Integer propertyValue = userDataAuthorization.getPropertyValue();

                if (dataAuthorizationMap.containsKey(resourceType)) {
                    String mapValue = dataAuthorizationMap.get(resourceType);
                    map = JsonUtil.jsonToObject(mapValue, Map.class, String.class, Integer.class);
                    if (CollectionUtils.isEmpty(map)) {
                        map = new HashMap<>();
                    }
                    map.put(resourceId, propertyValue);
                } else {
                    map = new HashMap<>();
                    map.put(resourceId, propertyValue);
                }

                dataAuthorizationMap.put(resourceType, JsonUtil.toJsonString(map));
            }
            if (!CollectionUtils.isEmpty(dataAuthorizationMap)) {
                RedisUtil.saveMap(resourceDataAuthorizationKey, dataAuthorizationMap);
            }
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
