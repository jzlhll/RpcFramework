package com.rpcframework.client;

import com.rpcframework.client.supply.IObjectInstanceSupply;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class BaseInvokeHandler implements InvocationHandler {
    public BaseInvokeHandler(IObjectInstanceSupply supply) {
        this.supply = supply;
    }
    //通过业务接口查找对象的提供者
    protected final IObjectInstanceSupply supply;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        if (supply == null) {
            throw new RuntimeException("no supply in InnerProcessHandler!");
        }

        //避免hashCode，toString，equals出错
        Class<?> methodDeclaringClass = method.getDeclaringClass();
        if (Object.class.equals(methodDeclaringClass)) {
            try {
                return method.invoke(this, args);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                return null;
            }
        }
        return sendCall(methodDeclaringClass, method, args);
    }

    /**
     * @param method InvocationHandler的invoke函数的参数。因此这个method是代理类Proxy的函数体
     * @param args 略
     * @return 略
     */
    protected abstract Object sendCall(Class<?> methodDeclaringClass, Method method, Object[] args);
}
