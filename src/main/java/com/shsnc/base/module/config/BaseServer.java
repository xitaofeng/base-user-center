package com.shsnc.base.module.config;

/**
 * Created by Elena on 2017/6/22.
 */
public class BaseServer {


    private static String dictionaryBaseUrl = "http://192.168.11.60:10030/";

    private static String DICTIONARY_MAP_KEY = "/dictionary/map/getByKey";

    private static String DICTIONARY_GET_ONE = "/dictionary/dict/get";

    /**
     * 获取数据字典单个对象服务
     *
     * @return
     */
    public static String dictionaryGetMapKeyServer() {
        return dictionaryBaseUrl + DICTIONARY_MAP_KEY;
    }

    /**
     * 获取数据字典单个对象服务
     *
     * @return
     */
    public static String dictionaryiOneServer() {
        return dictionaryBaseUrl + DICTIONARY_GET_ONE;
    }


}
