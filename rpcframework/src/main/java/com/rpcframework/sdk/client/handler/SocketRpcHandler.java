package com.rpcframework.sdk.client.handler;

import com.rpcframework.sdk.client.ClientSDK;
import com.rpcframework.sdk.client.supply.IObjectInstanceSupply;

import java.lang.reflect.Method;

public class SocketRpcHandler extends ClientSDK.RpcHandler {
    public SocketRpcHandler(IObjectInstanceSupply supply) {
        super(supply);
    }

    @Override
    protected Object sendCall(Class<?> methodDeclaringClass, Method method, Object[] args) {
        return null;
    }
}
