package com.shsnc.base.user.config;

import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by houguangqiang on 2017/6/9.
 */
public class ServerConfig {

    private static Boolean onlyCheck;
    private static Integer sessionTime;
    private static Boolean devModel;

    static {
        try {
            Properties properties = PropertiesLoaderUtils.loadAllProperties("server.properties");
            String onlyCheck = properties.getProperty("LOGIN.ONLY_CHECK","false");
            String sessionTime = properties.getProperty("LOGIN.SESSION_TIME","1800");
            String devModel = properties.getProperty("LOGIN.DEV_MODEL","false");
            ServerConfig.onlyCheck = Boolean.valueOf(onlyCheck);
            ServerConfig.sessionTime = Integer.valueOf(sessionTime);
            ServerConfig.devModel = Boolean.valueOf(devModel);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ServerConfig(){
    }

    public static Boolean isOnlyCheck() {
        return onlyCheck;
    }

    public static Integer getSessionTime() {
        return sessionTime;
    }

    public static Boolean isDevModel(){
        return devModel;
    };
}
