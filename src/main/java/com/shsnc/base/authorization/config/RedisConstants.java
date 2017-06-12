package com.shsnc.base.authorization.config;

/**
 * Created by Elena on 2017/6/12.
 */
public class RedisConstants {

    public static final Integer REDIS_TIME = 24 * 60 * 60;

    public final static String ATM = "USERCENTER";

    /** 资源属性 */
    public final static String RESOURCE_RESOURCE_PROPERTY_LIST = RedisUtils.buildRedisKey(ATM,"RESOURCE","PROPERTY","LIST");

    /** atm amp 接口数据 */
    public final static String ATM_AMP_API = RedisUtils.buildRedisKey(ATM,"AMP","API");

    /**
     *  atm获取AMP告警接口，主机KEY
     */
    public final static String ATM_AMP_API_ALARM_HOST = RedisUtils.buildRedisKey(ATM,"AMP","API","ALARM","HOST");

    /**
     * atm获取的主机告警，存放使用的MAP
     */
    public final static String ATM_AMP_API_ALARM_HOSTMAP = RedisUtils.buildRedisKey(ATM,"AMP","API","ALARM","HOSTMAP");

    /**
     *  atm获取AMP指标接口，主机KEY
     */
    public final static String ATM_AMP_API_ALARM_INDEX = RedisUtils.buildRedisKey(ATM,"AMP","API","INDEX","HOST");

    /**
     * atm获取的主机指标，存放使用的MAP
     */
    public final static String ATM_AMP_API_INDEX_HOSTMAP = RedisUtils.buildRedisKey(ATM,"AMP","API","INDEX","HOSTMAP");
}
