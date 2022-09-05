package com.rpcframework.server;

import com.rpcframework.common.IInProcessClientCallback;

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

    /**
     * 构建出代理客户端对象callback代理的对象
     */
    public static <T extends IInProcessClientCallback> T
            createProxyForClient(Class<T> svrInterface, Object clientObj) {
        InvocationHandler handler = null;

        assert handler != null;
        return (T) Proxy.newProxyInstance(svrInterface.getClassLoader(),
                new Class<?>[]{svrInterface},
                (proxy, method, args) -> {
                });
    }
}