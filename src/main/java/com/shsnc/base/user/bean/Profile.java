package com.shsnc.base.user.bean;

import java.util.List;

/**
 * Created by houguangqiang on 2017/6/14.
 */
public class Profile {

    private String username;
    private String alias;
    private String mobile;
    private String email;
    private List<ExtendPropertyValue> extendPropertyValues;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<ExtendPropertyValue> getExtendPropertyValues() {
        return extendPropertyValues;
    }

    public void setExtendPropertyValues(List<ExtendPropertyValue> extendPropertyValues) {
        this.extendPropertyValues = extendPropertyValues;
    }
}
