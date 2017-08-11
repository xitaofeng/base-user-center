package com.shsnc.base.authorization.config;

import java.util.ArrayList;
import java.util.List;

/**
 * @author houguangqiang
 * @date 2017-08-08
 * @since 1.0
 */
public enum DataOperation {

    /** 没有任何权限 */
    DENY(0),
    /** 所有 */
    ALL(-1),
    /** 查询 */
    GET(1),
    /** 新增 */
    ADD(2),
    /** 编辑 */
    EDIT(4),
    /** 删除 */
    DELETE(8),
    /** 执行 */
    EXECUTE(16),

    ;
    private int value;

    DataOperation(int value){
        this.value = value;
    }

    public int getValue(){
        return value;
    }

    /**
     * 传入操作计算权限值
     * @param dataOperations 操作集合
     * @return 返回权限值
     */
    public static int calculate(DataOperation... dataOperations){
        int permission = 0;
        for (DataOperation dataOperation : dataOperations) {
            permission = permission | dataOperation.getValue();
        }
        return permission;
    }

    /**
     * 根据权限值解析成拥有的操作集合
     * @param permission 权限值
     * @return 返回拥有的操作的数组
     */
    public static DataOperation[] resolve(int permission){
        List<DataOperation> dataOperations = new ArrayList<>(DataOperation.values().length);
        for (DataOperation dataOperation : DataOperation.values()) {
            if ((permission & dataOperation.getValue()) == dataOperation.getValue()) {
                dataOperations.add(dataOperation);
            }
        }
        return dataOperations.toArray(new DataOperation[dataOperations.size()]);
    }

    /**
     * 判断给出的权限值是否有某项操作的权限
     * @param permission 权限值
     * @param dataOperation 操作
     * @return 为ture表示包含，否则为不包含
     */
    public static boolean hasOperation(int permission, DataOperation dataOperation){
        return (permission & dataOperation.getValue()) == dataOperation.getValue();
    }


}
