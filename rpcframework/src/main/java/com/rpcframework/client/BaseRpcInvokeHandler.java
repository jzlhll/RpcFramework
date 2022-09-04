package com.rpcframework.client;

import android.os.Looper;

import com.rpcframework.Call;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class BaseRpcInvokeHandler implements InvocationHandler {
    protected abstract IClientConnector getConnector();

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

    private Object sendCall(Class<?> methodDeclaringClass, Method method, Object[] args) {
        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
            throw new RuntimeException("[BaseRpcInvokeHandler] cannot run in main thread!");
        }
        // 封装请求信息
        Call call = new Call(methodDeclaringClass.getName(), method.getName(),
                method.getParameterTypes(), args);
        // 创建链接
        IClientConnector connector = getConnector();
        if (!connector.isConnected()) {
            connector.connect();
        }
        // 发送请求
        connector.sendCall(call);
        // 获取封装远程方法调用结果的对象
        connector.disconnect();

        return call.getResult();
    }
}
