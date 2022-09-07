package com.rpcframework.util;

public class ReflectUtil {
    public static Class<?> classForName(String clsName) {
        try {
            return Class.forName(clsName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
