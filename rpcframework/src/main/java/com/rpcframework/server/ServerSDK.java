package com.rpcframework.server;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class ServerSDK {
    private ServerSDK() {}

    public static final ServerSDK INSTANCE = new ServerSDK();

    private ServerManager manager;
    public void setManager(ServerManager manager) {
        this.manager = manager;
    }

    public ServerManager getManager() {
        return manager;
    }
}