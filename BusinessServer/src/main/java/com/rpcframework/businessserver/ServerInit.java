package com.rpcframework.businessserver;

import android.util.Log;

import com.rpcframework.ServerSDK;

public class ServerInit {
    public static void init() {
        Log.d("allan", "init server");
        ServiceBusinessManager m = new ServiceBusinessManager();
        m.init();
        ServerSDK.INSTANCE.setManager(m);
    }
}
