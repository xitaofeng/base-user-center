package com.shsnc.base.module.config;

import com.shsnc.api.core.ThreadContext;
import com.shsnc.base.module.bean.RightsInfo;
import com.shsnc.base.util.api.ApiClient;
import com.shsnc.base.util.api.ApiResult;
import com.shsnc.base.util.api.BooleanResult;
import com.shsnc.base.util.config.BizException;
import com.shsnc.base.util.config.MessageCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author houguangqiang
 * @date 2017-08-03
 * @since 1.0
 */
public class AmpRemoteService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AmpRemoteService.class);

    private static final String ERROR_MESSAGE = "请求AMP服务异常!";

    private static final String UPDATE_RIGHTS ="/api/synchronize/updateRights";

    public static boolean updateHostsGroups(Long groupId, List<RightsInfo> usergrpRights) throws BizException {
        Map<String,Object> params = new HashMap<>();
        params.put("id", groupId);
        params.put("usergrpPermission", usergrpRights);
        ApiResult<BooleanResult> result = null;
        try {
            result = ApiClient.request(ModuleConstant.BASE_AMP_MODULE + UPDATE_RIGHTS, params, BooleanResult.class, ThreadContext.getClientInfo().getToken());
        } catch (IOException e) {
            LOGGER.error(ERROR_MESSAGE, e);
            throw new BizException(ERROR_MESSAGE);
        }
        return getBooleanResult(result);
    }

    private static boolean getBooleanResult(ApiResult<BooleanResult> result) throws BizException {
        if (result == null) {
            LOGGER.error("%s 没有任何响应！", ERROR_MESSAGE);
            throw new BizException(ERROR_MESSAGE);
        } else if (result.getMessageCode() == MessageCode.RESP_OK.getMsgCode()) {
            return result.getData().isResult();
        } else {
            throw new BizException(result.getMessage());
        }
    }
}
