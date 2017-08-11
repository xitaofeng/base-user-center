package com.shsnc.base.bean;

import java.util.List;

/**
 * @author houguangqiang
 * @date 2017-08-11
 * @since 1.0
 */
public class Condition {

    private boolean checkPermission;
    private List<Long> objectIds;

    public Condition() {
    }

    public Condition(boolean checkPermission, List<Long> objectIds) {
        this.checkPermission = checkPermission;
        this.objectIds = objectIds;
    }

    public boolean isCheckPermission() {
        return checkPermission;
    }

    public List<Long> getObjectIds() {
        return objectIds;
    }

    public void permission(boolean check, List<Long> objectIds){
        this.checkPermission = check;
        this.objectIds = objectIds;
    }


}
