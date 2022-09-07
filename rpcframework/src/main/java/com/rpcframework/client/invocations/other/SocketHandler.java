package com.rpcframework.client.invocations.other;

import com.rpcframework.client.BaseRpcInvokeHandler;
import com.rpcframework.client.IClientConnector;

public class SocketHandler extends BaseRpcInvokeHandler {
    @Override
    protected IClientConnector getConnector() {
        return null;
    }
}
