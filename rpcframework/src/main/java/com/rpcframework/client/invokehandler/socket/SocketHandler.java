package com.rpcframework.client.invokehandler.socket;

import com.rpcframework.client.BaseInvokeHandler;
import com.rpcframework.client.supply.IObjectInstanceSupply;

import java.lang.reflect.Method;

public class SocketHandler extends BaseInvokeHandler {
    public SocketHandler(IObjectInstanceSupply supply) {
        super(supply);
    }

    @Override
    protected Object sendCall(Class<?> methodDeclaringClass, Method method, Object[] args) {
        return null;
    }
}
