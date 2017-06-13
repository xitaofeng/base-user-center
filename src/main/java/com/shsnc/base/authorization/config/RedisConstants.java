package com.shsnc.base.authorization.config;

/**
 * Created by Elena on 2017/6/12.
 */
public class RedisConstants {

    public static final Integer REDIS_TIME = 24 * 60 * 60;

    public final static String USER_CENTER = "USER_CENTER";

    /** 资源属性列表 */
    public final static String RESOURCE_PROPERTY_LIST = RedisUtils.buildRedisKey(USER_CENTER,"RESOURCE","PROPERTY","LIST");

    /** 资源数据权限 */
    public final static String RESOURCE_DATA_AUTHORIZATION = RedisUtils.buildRedisKey(USER_CENTER,"RESOURCE","DATA","AUTHORIZATION");


}
