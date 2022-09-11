package com.rpcframework.util;

import android.os.Bundle;

import java.util.Set;

public final class Util {
    private Util(){}

    public static void bundlePrint(String tag, Bundle bundle) {
        RpcLog.d(tag, "=====bundle======");
        Set<String> keySet = bundle.keySet();
        for(String key : keySet) {
            Object value = bundle.get(key);
            RpcLog.d(tag, "bundle key " + key + ": " + value);
        }

        RpcLog.d(tag, "=================");
    }
}
