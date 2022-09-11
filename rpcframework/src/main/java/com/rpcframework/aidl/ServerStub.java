package com.rpcframework.aidl;

import android.os.Bundle;
import android.os.RemoteCallbackList;
import android.os.RemoteException;

import com.rpcframework.sdk.IRemoteCallback;
import com.rpcframework.sdk.IRemoteService;

public abstract class ServerStub extends IRemoteService.Stub {
    //public abstract Bundle send(Bundle bundle);

    //内部自己实现了linkToDeath，会自动移除掉线的客户端callback
    private final RemoteCallbackList<IRemoteCallback> mList = new RemoteCallbackList<>();

    @Override
    public boolean registerCallback(IRemoteCallback callback) throws RemoteException {
        mList.register(callback);
        return true;
    }

    @Override
    public boolean unRegisterCallback(IRemoteCallback callback) throws RemoteException {
        mList.unregister(callback);
        return true;
    }

    public final void callback(Bundle bundle) throws RemoteException {
        int len = mList.beginBroadcast();
        for (int i = 0; i < len; i++) {
            //从listeners取出第i个ICarInfoCallBack
            IRemoteCallback cb = mList.getBroadcastItem(i);
            //回调方法
            cb.onCallback(bundle);
        }
        mList.finishBroadcast();
    }
}