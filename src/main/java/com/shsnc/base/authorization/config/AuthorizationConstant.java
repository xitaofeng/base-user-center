package com.shsnc.base.authorization.config;

/**
 * Created by Elena on 2017/6/5.
 */
public final class AuthorizationConstant {

    /**
     * 基本操作
     */
    public enum BasicOperation {
        //添加
        ADD(1),
        //编辑
        DELETE(2),
        //
        EDIT(3),
        //查看
        GET(4);

        private Integer status;

        BasicOperation(Integer status) {
            this.status = status;
        }

        public Integer getStatus() {
            return status;
        }
    }
}
