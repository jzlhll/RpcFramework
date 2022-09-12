package com.rpcframework.client.invocations.other;

import com.rpcframework.client.BaseRpcInvokeHandler;
import com.rpcframework.exception.UnImplException;
import com.rpcframework.pack.ReturnSerial;

import java.lang.reflect.Method;

public class SocketHandler extends BaseRpcInvokeHandler {
    public SocketHandler(Class<?> clientInterface) {
        super(clientInterface);
    }

    @Override
    protected ReturnSerial sendCall(Method method, Object[] args) {
        throw new UnImplException();
    }
}
