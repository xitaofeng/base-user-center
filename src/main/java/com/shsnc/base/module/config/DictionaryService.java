package com.shsnc.base.module.config;

import com.shsnc.base.module.base.dictionary.DictionaryMapInfo;
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
public class DictionaryService {

    public static Logger LOG = LoggerFactory.getLogger(DictionaryService.class);

    /**
     * 根据字典id 和 mapkey 获取 对象
     *
     * @param moduleCode
     * @param dictCode
     * @param mapKey
     * @return
     */
    public static DictionaryMapInfo getDictionaryMap(String moduleCode, String dictCode, String mapKey) throws BizException {
        Map<String, Object> params = new HashMap();

        Map<String, String> entity = new HashMap<>();

        entity.put("moduleCode", moduleCode);
        entity.put("dictCode", dictCode);
        entity.put("mapKey", mapKey);
        params.put("entity", entity);
        try {
            String url = BaseServer.dictionaryGetMapKeyServer();
            ApiResult<DictionaryMapInfo> apiResult = ApiClient.request(url, params, DictionaryMapInfo.class);
            LOG.debug("请求数据字典服务：地址【{}】，参数【{}】，结果【{}】", url, JsonUtil.toJsonString(params), JsonUtil.toJsonString(apiResult));
            if (apiResult.getMessageCode() == 200) {
                return apiResult.getData();
            } else {
                throw new BizException("请求数据字典服务出现异常");
            }
        } catch (IOException e) {
            LOG.error("获取是否为管理员出现异常", e);
            e.printStackTrace();
        }
        return null;
    }
}
