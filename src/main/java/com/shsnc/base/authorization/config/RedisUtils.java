package com.shsnc.base.authorization.config;

/**
 * redis 工具类
 * Created by Elena on 2017/6/12.
 */
public final class RedisUtils {

    public static final String REDIS_SEPARATE = ":";  //reids 分隔符号

    /**
     * 拼接redis
     * @param sourceKey 源key
     * @param addKeys 追加key
     * @return
     */
    public static String buildRedisKey(String sourceKey, String... addKeys) {
        StringBuffer keySb = new StringBuffer(sourceKey);
        for (int i = 0; i < addKeys.length; i++) {
            keySb.append(":");
            keySb.append(addKeys[i]);
        }
        return keySb.toString();
    }
}
