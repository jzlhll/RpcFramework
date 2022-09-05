package com.rpcframework.client;

import android.os.Looper;

import com.rpcframework.Call;
import com.rpcframework.Response;
import com.rpcframework.util.RpcLog;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class BaseRpcInvokeHandler implements InvocationHandler {
    protected abstract IClientConnector getConnector();

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        //避免hashCode，toString，equals出错
        Class<?> methodDeclaringClass = method.getDeclaringClass();
        if (!methodDeclaringClass.isInterface()) {
            throw new RuntimeException("client call not a interface!");
        }

        if (Object.class.equals(methodDeclaringClass)) {
            try {
                return method.invoke(this, args);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                return null;
            }
        }
        Response response = sendCall(methodDeclaringClass, method, args);

        if (response.getErrorCode() != 0) {
            RpcLog.e("BaseRpcInvokeHandler invoke result error: " + response.getException());
        }
        return response.getResult();
    }

    private Response sendCall(Class<?> methodDeclaringClass, Method method, Object[] args) {
        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
            throw new RuntimeException("[BaseRpcInvokeHandler] cannot run in main thread!");
        }
        // 封装请求信息
        Call call = new Call(methodDeclaringClass.getName(), method.getName(),
                method.getParameterTypes(), args);
        // 创建链接
        IClientConnector connector = getConnector();
        //todo 每次都是重新建立socket通信
        if (!connector.isConnected()) {
            connector.connect();
        }
        // 发送请求
        Response r = connector.sendCall(call);
        // 获取封装远程方法调用结果的对象
        connector.disconnect();

        return r;
    }
}
