package com.rpcframework.sdk.client.handler;

import com.rpcframework.sdk.client.ClientSDK;
import com.rpcframework.sdk.client.supply.IObjectInstanceSupply;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class InnerProcessHandler extends ClientSDK.RpcHandler {

    public InnerProcessHandler(IObjectInstanceSupply supply) {
        super(supply);
    }

    @Override
    protected Object sendCall(Method method, Object[] args) {
        if (supply == null) {
            throw new RuntimeException("no supply in InnerProcessHandler!");
        }

        //避免hashCode，toString，equals出错
        Class<?> interfaceClass = method.getDeclaringClass();
        if (Object.class.equals(interfaceClass)) {
            try {
                return method.invoke(this, args);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                return null;
            }
        }

        Object object = supply.get(interfaceClass);
        if (object == null) {
            throw new RuntimeException("cannot find service impl of: " + interfaceClass);
        }
        try {
            return method.invoke(object, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }
}