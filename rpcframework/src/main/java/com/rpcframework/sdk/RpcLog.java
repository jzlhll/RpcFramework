package com.rpcframework.sdk;

public final class RpcLog {
    private static final String LOG_TAG = "rpc_sdk";
    private RpcLog(){}

    public static void d(String s) {
        android.util.Log.d(LOG_TAG, s);
    }

    public static void e(String s) {
        android.util.Log.e(LOG_TAG, s);
    }
}
