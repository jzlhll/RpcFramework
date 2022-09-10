package com.rpcframework.log;

import android.util.Log;

public final class ALog {
    private static final String TAG = "allan-";
    public static void d(String log) {
        Log.d(TAG, log);
    }
    public static void d(String tag, String log) {
        Log.d(TAG + tag, log);
    }
    public static void e(String log) {
        Log.d(TAG, log);
    }
    public static void e(String tag, String log) {
        Log.d(TAG + tag, log);
    }
}
