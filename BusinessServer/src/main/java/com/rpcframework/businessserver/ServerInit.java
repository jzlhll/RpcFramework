package com.rpcframework.businessserver;

import android.util.Log;

import com.rpcframework.sdk.server.Server;

public class ServerInit {
    public static void init() {
        Log.d("allan", "init server");
        ServiceBusinessManager m = new ServiceBusinessManager();
        m.init();
        Server.INSTANCE.setManager(m);
    }
}
