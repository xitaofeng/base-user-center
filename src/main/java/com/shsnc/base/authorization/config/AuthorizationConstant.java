package com.shsnc.base.authorization.config;

/**
 * Created by Elena on 2017/6/5.
 */
public final class AuthorizationConstant {

    public static String ROLE_CODE_ADMIN = "admin";

    /**
     * 基本操作
     */
    public enum EnumBasicOperation {
        //添加
        ADD(1),
        //编辑
        DELETE(2),
        //
        EDIT(3),
        //查看
        GET(4);

        private Integer status;

        EnumBasicOperation(Integer status) {
            this.status = status;
        }

        public Integer getStatus() {
            return status;
        }
    }
}
