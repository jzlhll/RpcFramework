package com.rpcframework.client.invocations.other;

import com.rpcframework.client.BaseInvokeHandler;

import java.lang.reflect.Method;

public class AidlHandler extends BaseInvokeHandler {

    @Override
    protected Object sendCall(Class<?> methodDeclaringClass, Method method, Object[] args) {
        return null;
    }
}
