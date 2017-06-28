package com.shsnc.base.module.config;

import com.shsnc.api.core.ThreadContext;
import com.shsnc.api.core.UserInfo;
import com.shsnc.base.module.base.dictionary.DictionaryInfo;
import com.shsnc.base.module.base.dictionary.DictionaryMapInfo;
import com.shsnc.base.util.api.ApiClient;
import com.shsnc.base.util.api.ApiResult;
import com.shsnc.base.util.api.BooleanResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Elena on 2017/6/26.
 */
public class DictionaryService {

    public static Logger LOG = LoggerFactory.getLogger(DictionaryService.class);

    /**
     * 根据字典id 和 mapkey 获取 对象
     * @param projectCode
     * @param dictCode
     * @param mapKey
     * @return
     */
    public static DictionaryMapInfo getDictionaryMap(String projectCode,String dictCode, String mapKey) {
        Map<String, String> params = new HashMap();
        params.put("projectCode", projectCode);
        params.put("dictCode", dictCode);
        params.put("mapKey", mapKey);

        try {
            ApiResult<DictionaryMapInfo> apiResult = ApiClient.request(BaseServer.dictionaryGetMapKeyServer(), params, DictionaryMapInfo.class);
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
