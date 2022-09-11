package com.rpcframework.aidlstudy;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.rpcframework.conn.IConnMgr;
import com.rpcframework.log.ALog;

public class ClientConnMgr implements IConnMgr, IBinder.DeathRecipient{
    static final String TAG = "ClientConnMgr";
    //强绑定activity或者service
    private final Context mContext;

    private final ClientConnMgrRetryHandler mRetry;
    private ServiceConnectionImpl serviceConnection;
    public IRemoteService getAidl() {
        ServiceConnectionImpl sc = serviceConnection;
        if (sc != null && sc.remoteService != null && sc.mBinder.isBinderAlive()) {
            return sc.remoteService;
        }
        return null;
    }

    private String mServicePackage, mServiceName;
    public ClientConnMgr setServicePackage(String servicePackage) {
        mServicePackage = servicePackage;
        return this;
    }
    public ClientConnMgr setServiceName(String serviceName) {
        mServiceName = serviceName;
        return this;
    }

    public ClientConnMgr(Activity activity) {
        this(activity, false);
    }

    public ClientConnMgr(Service service) {
        this(service, false);
    }

    private ClientConnMgr(Context context, boolean inner) {
        mContext = context;
        mRetry = new ClientConnMgrRetryHandler(
            () -> { //bind
                try {
                    ServiceConnectionImpl sc = new ServiceConnectionImpl();
                    ALog.d("REAL: bind service1: ");
                    boolean isCan = mContext.bindService(getServiceIntent(), sc, Context.BIND_AUTO_CREATE);
                    serviceConnection = sc;
                    ALog.d("REAL: bind service2: " + isCan); //我们不关注，因为我们在不断的重试。
                } catch (Exception e) {
                    e.printStackTrace();
                }
            },
            () -> { //unbind
                ServiceConnectionImpl sc = serviceConnection;
                if (sc != null && sc.mBinder != null && sc.mBinder.isBinderAlive()) {
                    ALog.d(TAG, "unbind: unlink to death");
                    sc.mBinder.unlinkToDeath(this, 0);
                }
                ALog.d("REAL: unbind");
                if (sc != null) mContext.unbindService(sc);
                serviceConnection = null;
            }
        );

        mRetry.setStopper(bindState -> {
            ALog.d("REAL: after stopper bindState: " + bindState);
        });

        mRetry.setTimeout(bindState -> {
            ALog.d("REAL: timeout: " + bindState);
        });
    }

    @Override
    public void binderDied() {
        ServiceConnectionImpl sc = serviceConnection;
        if (sc != null && sc.mBinder != null && sc.mBinder.isBinderAlive()) {
            ALog.d(TAG, "unlink to death");
            sc.mBinder.unlinkToDeath(this, 0);
        }
        whenErrorRetry("unlink death and binder Died!");
    }

    private final class ServiceConnectionImpl implements ServiceConnection {
        private IBinder mBinder;
        private IRemoteService remoteService;

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ALog.d(TAG, "onnServiceConnected( " + name);
            this.mBinder = service;
            remoteService = IRemoteService.Stub.asInterface(service);
            mRetry.stopRetry(true);
            try {
                service.linkToDeath(ClientConnMgr.this, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            whenErrorRetry("onnServiceDisconnected!");
        }

        @Override
        public void onBindingDied(ComponentName name) {
            whenErrorRetry("onnBindingDied!" + name);
        }
    }

    private Intent getServiceIntent() {
        Intent intent = new Intent(mServicePackage);
        intent.setComponent(new ComponentName(mServicePackage, mServiceName));
        return intent;
    }

    public void bind() {
        mRetry.startRetry();
    }

    public void unBind() {
        mRetry.unbind();
    }

    private void whenErrorRetry(String reason) {
        ALog.d(TAG, "whenErorRetry reason: " + reason);
        mRetry.startRetryWithDelay();
    }

    public void destroy() {
        mRetry.destroy();
    }
}
