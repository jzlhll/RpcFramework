package com.rpcframework.server;

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