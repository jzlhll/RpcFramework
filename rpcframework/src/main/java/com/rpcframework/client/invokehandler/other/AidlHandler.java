package com.rpcframework.client.invokehandler.other;

import com.rpcframework.client.BaseInvokeHandler;
import com.rpcframework.client.supply.IObjectInstanceSupply;

import java.lang.reflect.Method;

public class AidlHandler extends BaseInvokeHandler {
    public AidlHandler(IObjectInstanceSupply supply) {
        super(supply);
    }

    @Override
    protected Object sendCall(Class<?> methodDeclaringClass, Method method, Object[] args) {
        return null;
    }
}
