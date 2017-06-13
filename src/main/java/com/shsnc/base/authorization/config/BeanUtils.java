package com.shsnc.base.authorization.config;

import com.shsnc.base.authorization.model.AuthorizationResourceAuthModel;

import java.util.*;

/**
 * Created by Elena on 2017/6/13.
 */
public final class BeanUtils {

    /**
     * 去重
     *
     * @param list
     * @return
     */
    public static List<AuthorizationResourceAuthModel> removeDuplicate(List<AuthorizationResourceAuthModel> list) {
        List<String> setKey = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            AuthorizationResourceAuthModel item = list.get(i);
            String key = item.getAuthType() + "-" + item.getAuthValue() + "-" + item.getResourceType() + "-" + item.getResourceId();
            if (setKey.contains(key)) {
                list.remove(i--);
            } else {
                setKey.add(key);
            }
        }

        return list;
    }
}
