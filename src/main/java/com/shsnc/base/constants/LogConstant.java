package com.shsnc.base.constants;

/**
 * @author houguangqiang
 * @date 2017-11-02
 * @since 1.0
 */
public class LogConstant {

    private LogConstant() { }

    /** 模块 */
    public static class Module {
        private Module() { }
        /** 用户 */
        public static final int USER = 100;
    }

    /** 动作 */
    public static class Action {
        private Action() { }
        /** 登入 */
        public static final int LOGIN = 101;
        /** 登出 */
        public static final int LOGOUT = 102;
        /** 新增 */
        public static final int ADD = 201;
        /** 更新 */
        public static final int UPDATE = 202;
        /** 删除 */
        public static final int DELETE = 203;
        /** 导入 */
        public static final int IMPORT = 204;
        /** 授权 */
        public static final int AUTHORIZE = 301;
        /** 审核 */
        public static final int REVIEW = 305;
        /** 导出 */
        public static final int EXPORT = 401;
    }
}
