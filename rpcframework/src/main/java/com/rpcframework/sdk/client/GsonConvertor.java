package com.rpcframework.sdk.client;

import com.google.gson.Gson;

/**
 * 将对象结果进行转换
 */
public final class GsonConvertor {
    private GsonConvertor(){}
    private static final Gson sGson = new Gson();

    public static <T> T convertReturnVal(Object origReturnVal, Class<T> clazz) {
        String s = sGson.toJson(origReturnVal, origReturnVal.getClass());
        return sGson.fromJson(s, clazz);
    }
}
