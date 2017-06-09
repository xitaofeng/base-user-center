package com.shsnc.base.user.bean;

/**
 * Created by houguangqiang on 2017/6/9.
 */
public class LoginResult {

    private UserInfo userInfo;
    private Certification certification;

    public LoginResult() {
    }

    public LoginResult(UserInfo userInfo, Certification certification) {
        this.userInfo = userInfo;
        this.certification = certification;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public Certification getCertification() {
        return certification;
    }

    public void setCertification(Certification certification) {
        this.certification = certification;
    }
}
