package com.shsnc.base.module.config;

import com.shsnc.base.util.BizAssert;
import com.shsnc.base.util.config.BizException;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Created by Elena on 2017/7/3.
 */
public class ModuleConstant {

    private static final String MODULE_DEFAULT_URL;
    public static final String MODULE_ATM_URL;
    public static final String MODULE_USER_CENTER_URL;
    public static final String MODULE_DICTIONARY_URL;
    public static final String MODULE_RESOURCE_URL;
    public static final String MODULE_AMP_URL;

    static {
        try {
            Properties properties = PropertiesLoaderUtils.loadAllProperties("server.properties");

            MODULE_DEFAULT_URL = properties.getProperty("module.default_url");
            MODULE_ATM_URL = properties.getProperty("module.atm_url", MODULE_DEFAULT_URL);
            MODULE_USER_CENTER_URL = properties.getProperty("module.user_center_url", MODULE_DEFAULT_URL);
            MODULE_DICTIONARY_URL = properties.getProperty("module.dictionary_url", MODULE_DEFAULT_URL);
            MODULE_RESOURCE_URL = properties.getProperty("module.resource_url", MODULE_DEFAULT_URL);
            MODULE_AMP_URL = properties.getProperty("module.amp_url", MODULE_DEFAULT_URL);

            BizAssert.notNull(MODULE_ATM_URL, "配置文件server.properties[module.atm_url]属性不能为空！");
            BizAssert.notNull(MODULE_USER_CENTER_URL, "配置文件server.properties[module.user_center_url]属性不能为空！");
            BizAssert.notNull(MODULE_DICTIONARY_URL, "配置文件server.properties[module.dictionary_url]属性不能为空！");
            BizAssert.notNull(MODULE_RESOURCE_URL, "配置文件server.properties[module.resource_url]属性不能为空！");
            BizAssert.notNull(MODULE_AMP_URL, "配置文件server.properties[module.amp_url]属性不能为空！");
        } catch (IOException e) {
            throw new RuntimeException("配置文件server.properties加载失败！", e);
        } catch (BizException e2) {
            throw new RuntimeException(e2.getErrorMessage());
        }
    }
}
