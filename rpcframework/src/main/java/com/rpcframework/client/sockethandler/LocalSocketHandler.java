package com.rpcframework.client.sockethandler;

import com.rpcframework.client.ClientSDK;
import com.rpcframework.client.supply.IObjectInstanceSupply;

import java.lang.reflect.Method;

public class LocalSocketHandler extends ClientSDK.RpcHandler {
    public LocalSocketHandler(IObjectInstanceSupply supply) {
        super(supply);
    }

    @Override
    protected Object sendCall(Class<?> methodDeclaringClass, Method method, Object[] args) {
        return null;
    }
}
