package com.shsnc.base.user.support.token;

import com.shsnc.base.util.crypto.MD5Util;

import java.util.UUID;

/**
 * Created by houguangqiang on 2017/6/12.
 */
public final class TokenHelper {

    public static String encodeUID(String userId) {
        return MD5Util.encodeWithoutSalt(userId);
    }

    public static String generateUUID() {
        return UUID.randomUUID().toString().replace("-","").toUpperCase();
    }
}
