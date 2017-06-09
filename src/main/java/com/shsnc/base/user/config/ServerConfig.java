package com.shsnc.base.user.config;

import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by houguangqiang on 2017/6/9.
 */
public class ServerConfig {

    private static Boolean isSingleLogin;
    private static Integer session;

    static {
        try {
            Properties properties = PropertiesLoaderUtils.loadAllProperties("server.properties");
            String isSingleLogin = properties.getProperty("isSingleLogin","false");
            String session = properties.getProperty("session","60");
            ServerConfig.isSingleLogin = Boolean.valueOf(isSingleLogin);
            ServerConfig.session = Integer.valueOf(session);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ServerConfig(){
    }

    public static Boolean getIsSingleLogin() {
        return isSingleLogin;
    }

    public static Integer getSession() {
        return session;
    }
}
