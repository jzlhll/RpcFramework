package com.rpcframework.client.invocations.android;

import com.rpcframework.client.BaseInvokeHandler;
import com.rpcframework.exception.UnImplException;

import java.lang.reflect.Method;

public class BroadcastHandler extends BaseInvokeHandler {

    @Override
    protected Object sendCall(Method method, Object[] args) {
        throw new UnImplException();
    }
}
