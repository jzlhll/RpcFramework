package com.rpcframework.client.invocations.process;

import com.rpcframework.client.BaseInvokeHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class InnerProcessHandler extends BaseInvokeHandler {
    protected final Class<?> clientInterface;

    public InnerProcessHandler(Class<?> clientInterface, IProcessObjectSupply supply) {
        this.clientInterface = (clientInterface);
        this.supply = supply;
    }
    //通过业务接口查找对象的提供者
    protected final IProcessObjectSupply supply;

    @Override
    protected Object sendCall(Method method, Object[] args) {
        if (supply == null) {
            throw new RuntimeException("no supply in InnerProcessHandler!");
        }

        Object object = supply.get(clientInterface);
        if (object == null) {
            throw new RuntimeException("cannot find service impl of: " + clientInterface);
        }

        try {
            return method.invoke(object, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }
}