package com.shsnc.base.module.config;

import com.shsnc.api.core.ThreadContext;
import com.shsnc.base.api.constant.BaseDictionaryConstant;
import com.shsnc.base.api.constant.BaseResourceConstant;
import com.shsnc.base.module.base.dictionary.DictionaryMapInfo;
import com.shsnc.base.module.base.resource.ResourceInfo;
import com.shsnc.base.util.JsonUtil;
import com.shsnc.base.util.api.ApiClient;
import com.shsnc.base.util.api.ApiResult;
import com.shsnc.base.util.config.BizException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Elena on 2017/6/26.
 */
public class BaseResourceService {

    public static Logger LOG = LoggerFactory.getLogger(BaseResourceService.class);

    /**
     * 根据resourceCode 获取对象
     *
     * @param resourceCode
     * @return
     */
    public static ResourceInfo getResourceInfoByResourceCode(String resourceCode) {
        Map<String, String> params = new HashMap<>();

        params.put("resourceCode", resourceCode);
        try {
            String url =  ModuleConstant.BASE_RESOURCE_MODULE + BaseResourceConstant.RESOURCE_INSTANCE_GETINSTANCEBYID;
            ApiResult<ResourceInfo> apiResult = ApiClient.request(url, params, ResourceInfo.class);
            LOG.debug("请求数据字典服务：地址【{}】，参数【{}】，结果【{}】", url, JsonUtil.toJsonString(params), JsonUtil.toJsonString(apiResult));
            if (apiResult.getMessageCode() == 200) {
                return apiResult.getData();
            }
        } catch (IOException e) {
            LOG.error("获取是否为管理员出现异常", e);
            e.printStackTrace();
        }
        return null;
    }

}
