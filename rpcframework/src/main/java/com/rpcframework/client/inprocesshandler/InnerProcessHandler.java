package com.rpcframework.client.inprocesshandler;

import com.rpcframework.client.ClientSDK;
import com.rpcframework.client.supply.IObjectInstanceSupply;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class InnerProcessHandler extends ClientSDK.RpcHandler {
    public InnerProcessHandler(IObjectInstanceSupply supply) {
        super(supply);
    }

    @Override
    protected Object sendCall(Class<?> methodDeclaringClass, Method method, Object[] args) {
        Object object = supply.get(methodDeclaringClass);
        if (object == null) {
            throw new RuntimeException("cannot find service impl of: " + methodDeclaringClass);
        }

        try {
            return method.invoke(object, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }
}