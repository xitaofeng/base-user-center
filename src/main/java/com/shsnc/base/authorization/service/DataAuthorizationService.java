package com.shsnc.base.authorization.service;

import com.shsnc.api.core.validation.ValidationType;
import com.shsnc.base.authorization.bean.*;
import com.shsnc.base.authorization.config.AuthorizationUtil;
import com.shsnc.base.authorization.config.BeanUtils;
import com.shsnc.base.authorization.config.RedisConstants;
import com.shsnc.base.authorization.mapper.AuthorizationResourceAuthModelMapper;
import com.shsnc.base.authorization.mapper.AuthorizationUserRoleRelationModelMapper;
import com.shsnc.base.authorization.model.AuthorizationResourceAuthModel;
import com.shsnc.base.authorization.model.AuthorizationResourceAuthModel.EnumAuthType;
import com.shsnc.base.user.mapper.UserInfoGroupRelationModelMapper;
import com.shsnc.base.util.JsonUtil;
import com.shsnc.base.util.RedisUtil;
import com.shsnc.base.util.StringUtil;
import com.shsnc.base.util.config.BizException;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

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

    @Autowired
    private UserInfoGroupRelationModelMapper userInfoGroupRelationModelMapper;

    /**
     * 用户授权
     *
     * @param resourceTypeCode 授权资源类型
     * @param resourceId       授权用户列表
     * @param authUserList     授权
     * @return
     */
    @Transactional
    public List<AuthorizationResourceAuthModel> userAuth(String resourceTypeCode, Long resourceId, List<DataAuthorizationUser> authUserList) throws BizException {
        if (CollectionUtils.isEmpty(authUserList)) {
            throw new BizException("选择授权的用户");
        }

        List<AuthorizationResourceAuthModel> authorizationResourceAuthModels = new ArrayList<>();
        for (DataAuthorizationUser dataAuthorizationUser : authUserList) {
            Long userId = dataAuthorizationUser.getUserId();
            List<Long> propertyIdList = dataAuthorizationUser.getPropertyIdList();
            authorizationResourceAuthModels.addAll(buildAuthorizationResourceAuthModel(resourceTypeCode, resourceId, EnumAuthType.USER, userId, propertyIdList));
        }
        return authorizationResourceAuthModels;
    }

    /**
     * 角色授权
     *
     * @param resourceTypeCode  资源类型
     * @param resourceId
     * @param authUserGroupList
     * @return
     */
    @Transactional
    public List<AuthorizationResourceAuthModel> userGroupAuth(String resourceTypeCode, Long resourceId, List<DataAuthorizationUserGroup> authUserGroupList) throws BizException {
        if (CollectionUtils.isEmpty(authUserGroupList)) {
            throw new BizException("选择授权的组");
        }

        List<AuthorizationResourceAuthModel> authorizationResourceAuthModels = new ArrayList<>();
        for (DataAuthorizationUserGroup dataAuthorizationUserGroup : authUserGroupList) {
            Long groupId = dataAuthorizationUserGroup.getGroupId();
            List<Long> propertyIdList = dataAuthorizationUserGroup.getPropertyIdList();
            authorizationResourceAuthModels.addAll(buildAuthorizationResourceAuthModel(resourceTypeCode, resourceId, EnumAuthType.GROUP, groupId, propertyIdList));
        }
        return authorizationResourceAuthModels;
    }

    /**
     * 数据授权
     *
     * @param resourceTypeCode
     * @param dataAuthorizationList
     * @return
     */
    @Transactional
    public boolean auth(String resourceTypeCode, List<DataAuthorization> dataAuthorizationList) throws BizException {
        if (CollectionUtils.isEmpty(dataAuthorizationList)) {
            throw new BizException("选择授权的数据");
        }

        List<AuthorizationResourceAuthModel> authorizationResourceAuthModels = new ArrayList<>();
        for (DataAuthorization dataAuthorization : dataAuthorizationList) {
            Long resourceId = dataAuthorization.getResourceId();
            List<DataAuthorizationUserGroup> authUserGroupList = dataAuthorization.getAuthUserGroupList();
            List<DataAuthorizationUser> authUserList = dataAuthorization.getAuthUserList();

            //构建 用户数据授权对象
            if (!CollectionUtils.isEmpty(authUserList)) {
                authorizationResourceAuthModels.addAll(userAuth(resourceTypeCode, resourceId, authUserList));
            }

            //构建 用户数据授权对象
            if (!CollectionUtils.isEmpty(authUserGroupList)) {
                authorizationResourceAuthModels.addAll(userGroupAuth(resourceTypeCode, resourceId, authUserGroupList));
            }
        }

        //清理重复数据
        authorizationResourceAuthModels = BeanUtils.removeDuplicate(authorizationResourceAuthModels);

        //批量插入
        if (authorizationResourceAuthModelMapper.addBatchAuthorizationResourceAuthModel(authorizationResourceAuthModels) > 0) {
            //插入成功 - 清理redis缓存
            Set<Long> userIdList = new HashSet<>();
            List<Long> roleIdList = new ArrayList<>();
            List<Long> userGroupIdList = new ArrayList<>();
            for (AuthorizationResourceAuthModel authorizationResourceAuthModel : authorizationResourceAuthModels) {
                Integer authType = authorizationResourceAuthModel.getAuthType();
                Long authValue = authorizationResourceAuthModel.getAuthValue();
                if (EnumAuthType.USER.getValue() == authType) {
                    userIdList.add(authValue);
                }
                if (EnumAuthType.ROLE.getValue() == authType) {
                    roleIdList.add(authValue);
                }
                if (EnumAuthType.GROUP.getValue() == authType) {
                    userGroupIdList.add(authValue);
                }
            }

            //角色转用户
            if (!CollectionUtils.isEmpty(roleIdList)) {
                List<Long> tempList = authorizationUserRoleRelationModelMapper.getUserIdByRoleIds(roleIdList);
                if(!CollectionUtils.isEmpty(tempList)) {
                    userIdList.addAll(tempList);
                }
            }

            //组转用户
            if (!CollectionUtils.isEmpty(userGroupIdList)) {
                List<Long> tempList = userInfoGroupRelationModelMapper.getUserIdsByGroupIds(userGroupIdList);
                if(!CollectionUtils.isEmpty(tempList)) {
                    userIdList.addAll(tempList);
                }
            }
            AuthorizationUtil.removeUserDataAuthorization(userIdList);
        }

        return true;
    }


    /**
     * 根据用户id和资源类型获取 资源类型下所有有权限的资源数据及权限值
     *
     * @param userId
     * @param resourceTypeCode
     * @param authorizationPropertyValue 权限属性值
     * @return
     */
    public Map<Long, String> getUserAutValuehListByResourceTypeAndPropertyValue(Long userId, String resourceTypeCode, String authorizationPropertyValue) {

        //根据key 直接取用户对应资源的权限值 取不到加载用户权限数据
        Map<String, String> userAuthMap = RedisUtil.getMap(RedisConstants.userResourceDataAuthorizationKey(userId));
        if (CollectionUtils.isEmpty(userAuthMap)) {
            userAuthMap = getDataAuthorization(userId);
        }

        Map<Long, String> resourceTypeMap = new HashMap<>();
        String resourceTypeAuthStr = userAuthMap.get(resourceTypeCode);
        if (StringUtil.isNotEmpty(resourceTypeAuthStr)) {
            resourceTypeMap = JsonUtil.jsonToObject(resourceTypeAuthStr, Map.class, Long.class, String.class);
        }

        //根据权限属性过滤
        if (!CollectionUtils.isEmpty(resourceTypeMap)) {
            for (Long key : resourceTypeMap.keySet()) {
                String propertyValues = resourceTypeMap.get(key);
                if (!propertyValues.contains(authorizationPropertyValue)) {
                    resourceTypeMap.remove(key);
                }
            }
        }

        return resourceTypeMap;
    }


    /**
     * 资源权限值列表(根据资源类型)
     *
     * @param userId
     * @param resourceTypeCode
     * @return
     */
    public Map<Long, String> getUserAutValuehListByResourceType(Long userId, String resourceTypeCode) {

        //根据key 直接取用户对应资源的权限值 取不到加载用户权限数据
        Map<String, String> userAuthMap = RedisUtil.getMap(RedisConstants.userResourceDataAuthorizationKey(userId));
        if (CollectionUtils.isEmpty(userAuthMap)) {
            userAuthMap = getDataAuthorization(userId);
        }

        Map<Long, String> resourceTypeMap = new HashMap<>();
        String resourceTypeAuthStr = userAuthMap.get(resourceTypeCode);
        if (StringUtil.isNotEmpty(resourceTypeAuthStr)) {
            resourceTypeMap = JsonUtil.jsonToObject(resourceTypeAuthStr, Map.class, Long.class, String.class);
        }
        return resourceTypeMap;
    }


    /**
     * 用户资源权限值查询
     *
     * @param userId
     * @param resourceTypeCode
     * @param resourceId
     * @return
     */

    public String[] getAuthValue(Long userId, String resourceTypeCode, Long resourceId) {
        String authorizationValue = "";
        //根据key 直接取用户对应资源的权限值 取不到加载用户权限数据
        Map<String, String> userAuthMap = RedisUtil.getMap(RedisConstants.userResourceDataAuthorizationKey(userId));

        if (CollectionUtils.isEmpty(userAuthMap)) {
            userAuthMap = getDataAuthorization(userId);
        }

        String resourceTypeAuthStr = userAuthMap.get(resourceTypeCode);
        if (StringUtil.isNotEmpty(resourceTypeAuthStr)) {
            Map<String, String> resourceTypeMap = JsonUtil.jsonToObject(resourceTypeAuthStr, Map.class, String.class, String.class);
            authorizationValue = resourceTypeMap.get(resourceId.toString());
        }
        return authorizationValue.split(",");
    }


    /**
     * 用户和资源类型获取 下面所有资源列表的权限值列表
     *
     * @param userId
     * @param resourceType
     * @return
     */

    public Map<String, String> getAuthorizationByResourceType(Long userId, Integer resourceType) {
        //根据key 直接取用户对应资源的权限值 取不到加载用户权限数据
        Map<String, String> userAuthMap = RedisUtil.getMap(RedisConstants.userResourceDataAuthorizationKey(userId));

        if (CollectionUtils.isEmpty(userAuthMap)) {
            userAuthMap = getDataAuthorization(userId);
        }

        Map<String, String> resourceTypeMap = new HashMap<>();
        String resourceTypeAuthStr = userAuthMap.get(String.valueOf(resourceType));
        if (StringUtil.isNotEmpty(resourceTypeAuthStr)) {
            resourceTypeMap = JsonUtil.jsonToObject(resourceTypeAuthStr, Map.class, String.class, String.class);
        }
        return resourceTypeMap;
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

            HashMap<Long, String> map = new HashMap<>();
            for (UserDataAuthorization userDataAuthorization : userDataAuthorizations) {
                String resourceTypeCode = userDataAuthorization.getResourceTypeCode();
                Long resourceId = userDataAuthorization.getResourceId();
                String propertyValues = userDataAuthorization.getPropertyValues();

                if (dataAuthorizationMap.containsKey(resourceTypeCode)) {
                    String mapValue = dataAuthorizationMap.get(resourceTypeCode);
                    map = JsonUtil.jsonToObject(mapValue, Map.class, Long.class, String.class);
                    if (CollectionUtils.isEmpty(map)) {
                        map = new HashMap<>();
                    }
                    map.put(resourceId, propertyValues);
                } else {
                    map = new HashMap<>();
                    map.put(resourceId, propertyValues);
                }

                dataAuthorizationMap.put(resourceTypeCode, JsonUtil.toJsonString(map));
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
     * @param resourceTypeCode
     * @param resourceId
     * @param enumAuthType
     * @param authValue
     * @param propertyIds
     * @return
     * @throws BizException
     */
    //  resourceTypeCode, resourceId, EnumAuthType.USER, userId,propertyIdList
    private List<AuthorizationResourceAuthModel> buildAuthorizationResourceAuthModel(String resourceTypeCode, Long resourceId, AuthorizationResourceAuthModel.EnumAuthType enumAuthType, Long authValue, List<Long> propertyIds) throws BizException {
        List<AuthorizationResourceAuthModel> authorizationResourceAuthModels = new ArrayList<>();

        //清理权限
        deleteResourceDataAuthorization(enumAuthType, authValue, resourceTypeCode, resourceId);

        for (Long propertyId : propertyIds) {
            //propertyId 继承权限查询
            List<Long> ids = authorizationResourcePropertyService.getAuthorizationResourcePropertyModelPidById(propertyId);

            for (Long id : ids) {
                AuthorizationResourceAuthModel authorizationResourceAuthModel = AuthorizationResourceAuthModel.createAuthorizationResourceAuthModel(resourceTypeCode, resourceId, id, enumAuthType, authValue);
                authorizationResourceAuthModels.add(authorizationResourceAuthModel);
            }
        }

        return BeanUtils.removeDuplicate(authorizationResourceAuthModels);
    }

    /**
     * 清理权限
     *
     * @param enumAuthType
     * @param authValue
     * @param resourceTypeCode
     * @param resourceId
     * @return
     */
    private Integer deleteResourceDataAuthorization(EnumAuthType enumAuthType, Long authValue, String resourceTypeCode, Long resourceId) {
        return authorizationResourceAuthModelMapper.deleteResourceDataAuthorization(enumAuthType.getValue(), authValue, resourceTypeCode, resourceId);
    }
}
