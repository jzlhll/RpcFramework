package com.rpcframework.businessserver;

import com.rpcframework.server.ServerManager;

public class ServiceBusinessManager extends ServerManager {

    public void init() {
        put(IOrderBis.class, new OrderHasCallbackImpl());
    }
}
