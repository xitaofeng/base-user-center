package com.shsnc.base.module.config;

import com.shsnc.api.core.ThreadContext;
import com.shsnc.api.core.config.HttpServerConfig;
import com.shsnc.base.api.constant.BaseResourceConstant;
import com.shsnc.base.module.base.resource.ResourceInfo;
import com.shsnc.base.module.bean.ResourceGroupInfo;
import com.shsnc.base.util.JsonUtil;
import com.shsnc.base.util.api.ApiClient;
import com.shsnc.base.util.api.ApiResult;
import com.shsnc.base.util.config.BizException;
import com.shsnc.base.util.config.MessageCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

/**
 * Created by Elena on 2017/6/26.
 */
public class BaseResourceService {

    public static Logger LOG = LoggerFactory.getLogger(BaseResourceService.class);

    private static final String ERROR_MESSAGE = "请求资源服务异常!";

    private static final String GET_RESOURCE_GROUPS_BY_RESOURCE_GROUPIDS ="/resource/internal/resource/group/list/resourceGroupIds";
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

    public static List<ResourceGroupInfo> getResourceGroupsByResourceGroupIds(List<Long> resourceGroupIds) throws BizException {
        Map<String,Object> params = new HashMap<>();
        params.put("resourceGroupIds", resourceGroupIds);
        ApiResult<List> result;
        try {
            result = ApiClient.requestInternal(ModuleConstant.BASE_RESOURCE_MODULE + GET_RESOURCE_GROUPS_BY_RESOURCE_GROUPIDS, params, List.class, HttpServerConfig.getSignatureKey(),ThreadContext.getClientInfo().getToken());
        } catch (IOException e) {
            LOG.error(ERROR_MESSAGE, e);
            throw new BizException(ERROR_MESSAGE);
        }
        if (result == null) {
            if (LOG.isErrorEnabled()) {
                LOG.error(String.format("%s 没有任何响应！", ERROR_MESSAGE));
            }
            throw new BizException(ERROR_MESSAGE);
        } else if (result.getMessageCode() == MessageCode.RESP_OK.getMsgCode()) {
            if (result.getData() != null) {
                return JsonUtil.convert(result.getData(), List.class, ResourceGroupInfo.class);
            }
        } else {
            throw new BizException(result.getMessage());
        }
        return new ArrayList<>();
    }

}
