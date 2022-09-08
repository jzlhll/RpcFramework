package com.rpcframework.client.invocations.other;

import com.rpcframework.client.BaseInvokeHandler;

import java.lang.reflect.Method;

public class BroadcastHandler extends BaseInvokeHandler {

    public BroadcastHandler(Class<?> clientInterface) {
    }

    @Override
    protected Object sendCall(Method method, Object[] args) {
        return null;
    }
}
