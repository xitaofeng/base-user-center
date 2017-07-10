package com.shsnc.base.user.support.token;

import com.shsnc.base.util.crypto.AESUtil;

/**
 * Token解析
 * Created by houguangqiang on 2017/6/11.
 */
public class SimpleTokenProvider {

    private static final String TOKEN_KEY = "29cZ7jxSd5+JbSac39pg5A==";
    private static final String TOKEN_SEPARATOR = "::";

    private SimpleTokenProvider(){}

    public static String generateToken(String userId, String uuid) throws Exception {
        return AESUtil.encrypt(userId + TOKEN_SEPARATOR + uuid + TOKEN_SEPARATOR + TokenHelper.generateUUID(), TOKEN_KEY);
    }

    /**
     * 解析token
     * @param token token
     * @return 返回解析后的token，解析结果为一个长度为3的字符串数组，String[0]=userId，String[1]=uuid，String[2]=动态uuid
     * @throws IllegalArgumentException 解析异常
     */
    public static String[] resolveToken(String token) {
        try {
            String result = AESUtil.decrypt(token,TOKEN_KEY);
            return result.split(TOKEN_SEPARATOR);
        } catch (Exception e) {
            throw new IllegalArgumentException("token解析失败，不支持的token参数",e);
        }
    }
}
