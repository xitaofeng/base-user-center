package com.shsnc.base.user.config;

/**
 * Created by houguangqiang on 2017/6/6.
 */
public abstract class UserConstant {

    /** 账户类型用户名 */
    public static final int ACCOUNT_TYPE_USERNAME = 1;
    /** 账户类型邮箱 */
    public static final int ACCOUNT_TYPE_EMAIL = 2;
    /** 账户类型手机号 */
    public static final int ACCOUNT_TYPE_MOBILE = 3;

    /** 正则表达式常量 */
    public static class Regex {
        /** 用户名 */
        public static final String USERNAME = "^[a-zA-Z]\\w{5,29}$";
        /** 手机号码 */
        public static final String MOBILE = "^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$";
        /** 邮箱 */
        public static final String EMAIL = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
        /** 属性名 */
        public static final String PROPERTY_NAME = "^.+$";
    }
    /** 用户启用状态 */
    public static final int USER_STATUS_ENABLED = 1;
    /** 用户禁用状态 */
    public static final int USER_STATUS_DISABLED = 2;
    /** 用户被锁住状态 */
    public static final int USER_STATUS_LOCKED = 3;
    /** 用户未删除 */
    public static final int USER_DELETEED_FALSE = 1;
    /** 用户已删除 */
    public static final int USER_DELETEED_TRUE = 2;
    /** 用户不是内部使用 */
    public static final int USER_INTERNAL_FALSE = 1;
    /** 用户是内部使用 */
    public static final int USER_INTERNAL_TRUE = 2;
    /** 用户组启用状态 */
    public static final int GROUP_STATUS_ENABLED = 1;
    /** 用户组禁用状态 */
    public static final int GROUP_STATUS_DISABLED = 2;

    public static final String REDIS_LOGIN_KEY = "USERCENTER_LOGIN_TOKEN";
    public static final String REDIS_USER_INFO_KEY = "USERCENTER_USER_INFO_TOKEN";

}
