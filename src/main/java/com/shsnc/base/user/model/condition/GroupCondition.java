package com.shsnc.base.user.model.condition;

import com.shsnc.base.bean.Condition;

public class GroupCondition extends Condition {

    private String name;
    private String code;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
