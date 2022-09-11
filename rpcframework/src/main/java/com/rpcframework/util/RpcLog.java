package com.rpcframework.util;

import android.util.Log;

public final class RpcLog {
    private static final String LOG_TAG = "rpc_sdk";
    private RpcLog(){}

    public static void d(String s) {
        android.util.Log.d(LOG_TAG, s);
    }

    public static void d(String tag, String s) {
        android.util.Log.d(LOG_TAG + tag, s);
    }

    public static void e(String s) {
        android.util.Log.e(LOG_TAG, s);
    }

    public static void e(String tag, String s) {
        android.util.Log.e(LOG_TAG + tag, s);
    }

    public static String ex(Throwable th) {
        return Log.getStackTraceString(th);
    }
}
