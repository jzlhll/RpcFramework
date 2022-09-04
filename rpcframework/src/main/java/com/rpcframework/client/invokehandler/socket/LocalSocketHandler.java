package com.rpcframework.client.invokehandler.socket;

import com.rpcframework.client.BaseInvokeHandler;
import com.rpcframework.client.supply.IObjectInstanceSupply;

import java.lang.reflect.Method;

public class LocalSocketHandler extends BaseInvokeHandler {
    public LocalSocketHandler(IObjectInstanceSupply supply) {
        super(supply);
    }

    @Override
    protected Object sendCall(Class<?> methodDeclaringClass, Method method, Object[] args) {
        return null;
    }
}
