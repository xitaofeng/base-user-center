package com.shsnc.base.module.base.dictionary;

/**
 * Created by Elena on 2017/6/22.
 */
public class DictionaryMapInfo {

    /**
     * 字典编码
     */
    private String dictId;
    /**
     * 字典key
     */
    private String mapKey;
    /**
     * 字典Value
     */
    private String mapDesc;
    /**
     * 描述
     */
    private String mapValue;

    public String getDictId() {
        return dictId;
    }

    public void setDictId(String dictId) {
        this.dictId = dictId;
    }

    public String getMapKey() {
        return mapKey;
    }

    public void setMapKey(String mapKey) {
        this.mapKey = mapKey;
    }

    public String getMapDesc() {
        return mapDesc;
    }

    public void setMapDesc(String mapDesc) {
        this.mapDesc = mapDesc;
    }

    public String getMapValue() {
        return mapValue;
    }

    public void setMapValue(String mapValue) {
        this.mapValue = mapValue;
    }
}
