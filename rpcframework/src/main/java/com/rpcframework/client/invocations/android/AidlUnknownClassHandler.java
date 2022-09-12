package com.rpcframework.client.invocations.android;

import android.os.Bundle;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;

import com.rpcframework.aidl.IConnMgr;
import com.rpcframework.client.BaseRpcInvokeHandler;
import com.rpcframework.client.ConnAdapter;
import com.rpcframework.client.IClientMgr;
import com.rpcframework.pack.CallBundle;
import com.rpcframework.pack.ReturnSerial;
import com.rpcframework.pack.ServerReturnBundle;
import com.rpcframework.sdk.IRemoteService;

import java.lang.reflect.Method;

public final class AidlUnknownClassHandler extends BaseRpcInvokeHandler {
    private final ConnAdapter connMgr;
    public AidlUnknownClassHandler(Class<?> clientInterface, IClientMgr connMgr) {
        super(clientInterface);
        this.connMgr = (ConnAdapter) connMgr;
    }

    @Override
    protected ReturnSerial sendCall(Method method, Object[] args) { //直接重写
        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
            throw new RuntimeException("[BaseRpcInvokeHandler] cannot run in main thread!");
        }

        if (!connMgr.isAlive()) {
            return ReturnSerial.ERROR_SVR_NOT_ALIVE;
        }

        IConnMgr mgr = connMgr.getConnMgr();
        IRemoteService service = mgr.getSvr();

        CallBundle callBundle = new CallBundle();
        callBundle.addClassName(clientInterface.getName()).addMethodName(method.getName())
                .addParamTypes(method.getParameterTypes(), args);
        Bundle re;
        try {
            re = service.send(callBundle.get());
        } catch (RemoteException e) {
            String exception = Log.getStackTraceString(e);
            return new ReturnSerial(exception, -1);
        }

        if (re == null) {
            return ReturnSerial.ERROR_UNKNOWN_NULL;
        }

        return ServerReturnBundle.parse(re);
    }
}
