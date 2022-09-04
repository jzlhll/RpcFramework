package com.rpcframework.sdk.server;

public class Server {
    private Server() {}

    public static final Server INSTANCE = new Server();

    private ServerManager manager;
    public void setManager(ServerManager manager) {
        this.manager = manager;
    }

    public ServerManager getManager() {
        return manager;
    }
}
