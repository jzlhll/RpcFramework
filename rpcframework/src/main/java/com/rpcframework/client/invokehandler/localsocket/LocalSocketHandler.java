package com.rpcframework.client.invokehandler.localsocket;

import com.rpcframework.client.BaseRpcInvokeHandler;
import com.rpcframework.client.IClientConnector;

public class LocalSocketHandler extends BaseRpcInvokeHandler {
    private LocalSocketConnector connector;
    @Override
    protected IClientConnector getConnector() {
        if (connector == null) {
            connector = new LocalSocketConnector();
        }
        return connector;
    }
}