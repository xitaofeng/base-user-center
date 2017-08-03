package com.shsnc.base.module.bean;

/**
 * @author houguangqiang
 * @date 2017-08-03
 * @since 1.0
 */
public class RightsInfo {

    private Long groupid;
    private int permission;

    public RightsInfo() {
    }

    public RightsInfo(Long groupid, int permission) {
        this.groupid = groupid;
        this.permission = permission;
    }

    public Long getGroupid() {
        return groupid;
    }

    public void setGroupid(Long groupid) {
        this.groupid = groupid;
    }

    public int getPermission() {
        return permission;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }
}
