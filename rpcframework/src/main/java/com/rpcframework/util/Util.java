package com.rpcframework.util;

public final class Util {
    private Util(){}
    public static Class<?> classForName(String clsName) {
        try {
            return Class.forName(clsName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
