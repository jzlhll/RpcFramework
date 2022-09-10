package com.rpcframework.study;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;

import com.rpcframework.log.ALog;

import java.util.Set;

public class Util {
    public static final Handler sMainHandler = new Handler(Looper.getMainLooper());
    public static final Handler sSubHandler;
    static {
        HandlerThread th = new HandlerThread("global-sub-thread-handler");
        th.start();
        sSubHandler = new Handler(th.getLooper());
    }

    public static void bundlePrint(String tag, Bundle bundle) {
        ALog.d(tag, "=====bundle======");
        Set<String> keySet = bundle.keySet();
        for(String key : keySet) {
            Object value = bundle.get(key);
            ALog.d(tag, "bundle key " + key + ": " + value);
        }

        ALog.d(tag, "=================");
    }
}
