package com.rpcframework.aidlstudy;

import android.os.Bundle;
import android.os.RemoteCallbackList;
import android.os.RemoteException;

import com.rpcframework.bean.Info;
import com.rpcframework.log.ALog;
import com.rpcframework.study.Util;

class ServerStub extends IRemoteService.Stub {
    public ServerStub() {
        new Thread(()->{
            while (mIsLive) {
                try {
                    Thread.sleep(8 * 1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Bundle bundle = new Bundle();
                bundle.putString("hearthBeat", "" + Math.random());
                try {
                    callback(bundle);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    boolean mIsLive = true;

    private static final String TAG = "MyRemoteStub";

    //内部自己实现了linkToDeath，会自动移除掉线的客户端callback
    private final RemoteCallbackList<IRemoteCallback> mList = new RemoteCallbackList<>();

    @Override
    public Bundle send(Bundle bundle) {
        bundle.setClassLoader(getClass().getClassLoader());
        ALog.d(TAG, "send get coming data: " + bundle);
        Util.bundlePrint(TAG, bundle);

        GeneralBundle.Builder b = GeneralBundle.Builder.create();
        Info info = new Info();
        info.setName("allan");
        info.setValue("12453");
        info.setOther(89);
        b.addReturnType(Info.class.getName());
        b.addHash(info.hashCode());
        Bundle ret = b.build().toBundle();

        ret.putParcelable(GeneralBundle.RETURN_VALUE, info);
        return ret;
    }

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

    void callback(Bundle bundle) throws RemoteException {
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
