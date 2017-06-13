package com.shsnc.base.user.support.token;

import com.shsnc.base.util.crypto.AESUtil;

/**
 * Created by houguangqiang on 2017/6/11.
 */
public class SimpleTokenProvider {

    private static final String TOKEN_KEY = "29cZ7jxSd5+JbSac39pg5A==";
    private static final String TOKEN_SEPARATOR = "::";


    public static String generateToken(String userId, String uuid) throws Exception {
        return AESUtil.encrypt(userId + TOKEN_SEPARATOR + uuid + TOKEN_SEPARATOR + TokenHelper.generateUUID(), TOKEN_KEY);
    }

    public static String[] resolveToken(String token) throws Exception {
        String result = AESUtil.decrypt(token,TOKEN_KEY);
        return result.split(TOKEN_SEPARATOR);
    }
}
