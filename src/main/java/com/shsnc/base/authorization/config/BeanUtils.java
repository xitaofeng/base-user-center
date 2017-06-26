package com.shsnc.base.authorization.config;

import com.shsnc.base.authorization.model.AuthorizationResourceAuthModel;

import java.util.*;

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
        Collection<AuthorizationResourceAuthModel> collection = map.values();
        return new ArrayList<>(collection);
    }
}
