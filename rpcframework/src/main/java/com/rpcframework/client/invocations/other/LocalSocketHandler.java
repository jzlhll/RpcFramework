package com.rpcframework.client.invocations.other;

import android.os.Looper;

import com.rpcframework.client.BaseRpcInvokeHandler;
import com.rpcframework.pack.CallSerial;
import com.rpcframework.pack.ReturnSerial;

import java.lang.reflect.Method;

public class LocalSocketHandler extends BaseRpcInvokeHandler {
    private LocalSocketConnector connector;

    public LocalSocketHandler(Class<?> clientInterface) {
        super(clientInterface);
    }

    protected IClientConnector getConnector() {
        if (connector == null) {
            connector = new LocalSocketConnector();
        }
        return connector;
    }

    protected ReturnSerial sendCall(Method method, Object[] args) {
        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
            throw new RuntimeException("[BaseRpcInvokeHandler] cannot run in main thread!");
        }
        // 封装请求信息
        CallSerial call = new CallSerial(clientInterface.getName(), method.getName(),
                method.getParameterTypes(), args);
        // 创建链接
        IClientConnector connector = getConnector();
        //todo 每次都是重新建立socket通信
        if (!connector.isConnected()) {
            connector.connect();
        }
        // 发送请求
        ReturnSerial r = connector.sendCall(call);
        // 获取封装远程方法调用结果的对象
        connector.disconnect();

        return r;
    }
}