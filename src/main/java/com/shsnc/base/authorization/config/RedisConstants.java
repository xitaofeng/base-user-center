package com.shsnc.base.authorization.config;

import com.shsnc.base.util.RedisUtil;

import java.util.List;

/**
 * Created by Elena on 2017/6/12.
 */
public class RedisConstants {

    public static final Integer REDIS_TIME = 24 * 60 * 60;

    public final static String USER_CENTER = "USER_CENTER";

    /**
     * 资源属性列表
     */
    public final static String RESOURCE_PROPERTY_LIST = RedisUtil.buildRedisKey(USER_CENTER, "RESOURCE", "PROPERTY", "LIST");

    /**
     * 资源数据权限
     */
    public final static String RESOURCE_DATA_AUTHORIZATION = RedisUtil.buildRedisKey(USER_CENTER, "RESOURCE", "DATA", "AUTHORIZATION");


    /**
     * 用户资源数据权限 key (存储数据为 用户拥有哪些资源)
     *
     * @param userId
     * @return
     */
    public static String userResourceDataAuthorizationKey(Long userId) {
        return RedisUtil.buildRedisKey(RedisConstants.RESOURCE_DATA_AUTHORIZATION, userId.toString());
    }

    /**
     * 资源数据权限 key  (存储数据为 资源属于哪些用户)
     *
     * @param resourceType
     * @param resourceId
     * @return
     */
    public static String resourceDataAuthorizationKey(Integer resourceType, Long resourceId) {
        return RedisUtil.buildRedisKey(RedisConstants.RESOURCE_DATA_AUTHORIZATION, resourceType.toString(), resourceId.toString());
    }

    /**
     * 清理用户-资源类型下的权限数据权限
     *
     * @param userIdList
     */
    public static void removeUserDataAuthorization(List<Long> userIdList) {
        for (Long userId : userIdList) {
            //清理redis 权限
            String redisKey = RedisConstants.userResourceDataAuthorizationKey(userId);
            RedisUtil.remove(redisKey);
        }
    }
}
