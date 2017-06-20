package com.shsnc.base.authorization.config;

import com.shsnc.base.authorization.model.AuthorizationResourceAuthModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Elena on 2017/6/13.
 */
public final class BeanUtils {

    /**
     * 权限数据去重
     *
     * @param list
     * @return
     */
    public static List<AuthorizationResourceAuthModel> removeDuplicate(List<AuthorizationResourceAuthModel> list) {
        Map<String, AuthorizationResourceAuthModel> map = new HashMap<>();
        List<String> setKey = new ArrayList<>();
        for (AuthorizationResourceAuthModel item : list) {
            String key = item.getResourceType() + "-" + item.getResourceId() + "-" + item.getPropertyId() + "-" + item.getAuthType() + "-" + item.getAuthValue();
            map.put(key, item);
        }
        return (List<AuthorizationResourceAuthModel>) map.values();
    }
}
