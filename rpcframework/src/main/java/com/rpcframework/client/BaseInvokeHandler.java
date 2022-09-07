package com.rpcframework.client;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class BaseInvokeHandler implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
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
