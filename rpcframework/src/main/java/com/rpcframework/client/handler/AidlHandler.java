package com.rpcframework.client.handler;

import com.rpcframework.client.ClientSDK;
import com.rpcframework.client.supply.IObjectInstanceSupply;

import java.lang.reflect.Method;

public class AidlHandler extends ClientSDK.RpcHandler {
    public AidlHandler(IObjectInstanceSupply supply) {
        super(supply);
    }

    @Override
    protected Object sendCall(Class<?> methodDeclaringClass, Method method, Object[] args) {
        return null;
    }
}
