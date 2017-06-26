package com.shsnc.base.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据权限 工具类
 * Created by Elena on 2017/6/26.
 */
public class DataAuthorizationUtils {


    /**
     * 资源类型
     */
    public enum EnumResourceType {
        HOST(1),

        SCRIPT(2);

        private Integer value;

        public Integer getValue() {
            return value;
        }

        EnumResourceType(Integer value) {
            this.value = value;
        }
    }

    /**
     * 权限值
     */
    public enum EnumDataAuthorization {
        ADD(1),

        DELETE(2),

        EDIT(4),

        GET(8),

        RUN(16),

        AUTHORIZATION(32);

        private Integer value;

        public Integer getValue() {
            return value;
        }

        EnumDataAuthorization(Integer value) {
            this.value = value;
        }
    }

    /**
     * 解析权限值
     *
     * @param authorizationValue
     * @return
     */
    public static List<EnumDataAuthorization> analysisAuthorizationValue(Integer authorizationValue) {
        List<EnumDataAuthorization> list = new ArrayList<>();
        //TODO 解析
        if (authorizationValue > 0) {
            list.add(EnumDataAuthorization.GET);
        }
        return list;
    }
}
