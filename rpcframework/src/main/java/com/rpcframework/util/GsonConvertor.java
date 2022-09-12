package com.rpcframework.util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

/**
 * 将对象结果进行转换
 */
public final class GsonConvertor {
    private GsonConvertor(){}
    public static final Gson sGson = new Gson();

    /**
     * 将原来的数据序列化以后，再行反序列化回到clazz对象中去。
     */
    public static <T> T convert(Object origReturnVal, Class<T> clazz) {
        String s = sGson.toJson(origReturnVal, origReturnVal.getClass());
        return sGson.fromJson(s, clazz);
    }

    public static <T> T convert2(Object origReturnVal, Class<T> clazz) {
        JsonElement j = sGson.toJsonTree(origReturnVal);
        return sGson.fromJson(j, clazz);
    }
}
