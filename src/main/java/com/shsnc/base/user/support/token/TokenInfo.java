package com.shsnc.base.user.support.token;

/**
 * Created by houguangqiang on 2017/6/12.
 */
public class TokenInfo {

    private String userId;
    private String uuid;

    public TokenInfo(String userId, String uuid) {
        this.userId = userId;
        this.uuid = uuid;
    }

    public String getUserId() {
        return userId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
