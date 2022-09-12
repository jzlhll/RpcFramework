package com.rpcframework.client;

import android.content.Context;

import androidx.collection.SimpleArrayMap;

import com.rpcframework.aidl.ConnMgr;
import com.rpcframework.aidl.IConnMgr;

/**
 * 同一个进程里面，对于同一个远程服务应该使用一个conMgr。
 */
public final class ClientConnMgrs {
    private static final SimpleArrayMap<Integer, IClientMgr> conns = new SimpleArrayMap<>(2);

    public static IClientMgr createConnection(Context context, String pkg, String serviceName) {
        int key = (pkg + serviceName).hashCode();
        if (conns.containsKey(pkg + serviceName)) {
            return conns.get(key);
        }
        IConnMgr mgr = new ConnMgr(context, true);
        IClientMgr adapter = new ConnAdapter(mgr);
        conns.put(key, adapter);
        return adapter;
    }
}
