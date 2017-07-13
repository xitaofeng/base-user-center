package com.shsnc.base.module.config;

import java.util.ResourceBundle;

/**
 * Created by Elena on 2017/7/3.
 */
public class ModuleConstant {

    // #### //
    public static String BASE_USER_CENTER_MODULE = "";

    public static String BASE_DICTIONARY_MODULE = "";

    public static String BASE_RESOURCE_MODULE = "";

    static {
        ResourceBundle bundle = ResourceBundle.getBundle("module");

        BASE_USER_CENTER_MODULE = bundle.getString("BASE_USER_CENTER_MODULE");

        BASE_DICTIONARY_MODULE = bundle.getString("BASE_DICTIONARY_MODULE");

        BASE_RESOURCE_MODULE = bundle.getString("BASE_RESOURCE_MODULE");
    }
}
