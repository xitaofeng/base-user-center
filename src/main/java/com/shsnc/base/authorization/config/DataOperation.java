package com.shsnc.base.authorization.config;

/**
 * @author houguangqiang
 * @date 2017-08-08
 * @since 1.0
 */
public enum DataPermission {

    GET(1),
    ADD(2),
    EDIT(4),
    DELETE(8),
    EXECUTE(16),

    ;
    private int value;

    DataPermission(int value){
        this.value = value;
    }

    public int getValue(){
        return value;
    }

    public static int calculate(DataPermission... permissions){
        int permission = 0;
        for (DataPermission dataPermission : permissions) {
            permission = permission | dataPermission.getValue();
        }
        return permission;
    }

    public static void main(String[] args) {
        System.out.println(DataPermission.calculate(DataPermission.values()));
    }
}
