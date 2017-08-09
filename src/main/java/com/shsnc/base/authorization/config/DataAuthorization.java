package com.shsnc.base.authorization.config;

/**
 * @author houguangqiang
 * @date 2017-08-08
 * @since 1.0
 */
public enum DataAuthorization {

    RESOURCE_GROUP(1, "资源组"),
    SCRIPT(2, "脚本"),
    ORCHESTRATION(3, "编排"),

    ;

    private int type;
    private String description;

    private DataAuthorization(int type, String description){
        this.type = type;
        this.description = description;
    }

    public int getType(){
        return type;
    }

    public String getDescription() {
        return description;
    }
}
