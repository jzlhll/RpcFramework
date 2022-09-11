package com.rpcframework.server;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;

import androidx.annotation.Nullable;

import com.rpcframework.aidl.ServerStub;
import com.rpcframework.util.RpcLog;

public class RpcService extends Service {
    private static final String TAG = "RpcService";
    private ServerStubImpl stub;

    @Override
    public void onCreate() {
        super.onCreate();
        RpcLog.d(TAG, "on create...");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        RpcLog.d(TAG, "onBind...");
        if (stub == null) {
            stub = new ServerStubImpl();
        }
        return stub;
    }

    private final class ServerStubImpl extends ServerStub {

        @Override
        public Bundle send(Bundle bundle) throws RemoteException {
            return null;
        }
    }
}
