package com.shsnc.base.authorization.config;

import com.shsnc.base.util.RedisUtil;

import java.util.Set;

/**
 * Created by Elena on 2017/7/14.
 */
public class AuthorizationUtil {

    /**
     * 清理用户-资源类型下的权限数据权限
     *
     * @param userIdList
     */
    public static void removeUserDataAuthorization(Set<Long> userIdList) {
        for (Long userId : userIdList) {
            //清理redis 权限
            String redisKey = RedisConstants.userResourceDataAuthorizationKey(userId);
            RedisUtil.remove(redisKey);
        }
    }
}
