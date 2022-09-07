package com.rpcframework.client.invocations.process;

import com.rpcframework.client.BaseInvokeHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class InnerProcessHandler extends BaseInvokeHandler {
    public InnerProcessHandler(IProcessObjectSupply supply) {
        this.supply = supply;
    }
    //通过业务接口查找对象的提供者
    protected final IProcessObjectSupply supply;

    @Override
    protected Object sendCall(Class<?> methodDeclaringClass, Method method, Object[] args) {
        if (supply == null) {
            throw new RuntimeException("no supply in InnerProcessHandler!");
        }

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