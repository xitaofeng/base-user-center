package com.shsnc.base.authorization.config;

import com.shsnc.base.util.config.BizException;

/**
 * @author houguangqiang
 * @date 2017-08-08
 * @since 1.0
 */
public enum DataObject {

    /** 资源组 */
    RESOURCE_GROUP(1, "资源组"),
    /** 脚本 */
    SCRIPT(2, "脚本"),
    /** 编排 */
    ORCHESTRATION(3, "编排"),
    /** 巡检 */
    INSPECTION(4, "巡检"),
    /** 巡检项 */
    INSPECTION_ITEM(5, "巡检项"),
    /** 作业 */
    TASK(6, "作业"),
    /** 文件 */
    FILE(7, "文件"),

    ;

    private int type;
    private String description;

    DataObject(int type, String description){
        this.type = type;
        this.description = description;
    }

    public int getType(){
        return type;
    }

    public String getDescription() {
        return description;
    }

    public static DataObject of(String name) throws BizException {
        try {
            return DataObject.valueOf(name);
        } catch (IllegalArgumentException e) {
            throw new BizException("不支持的权限数据来源！");
        }
    }
}
