package com.rpcframework.aidlstudy;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Process;

import androidx.annotation.Nullable;

import com.rpcframework.log.ALog;

public final class MyRemoteService extends Service {
    private static final String TAG = "MyRemoteService";
    private final ServerStub stub = new ServerStub();

    @Override
    public void onCreate() {
        super.onCreate();
        ALog.d(TAG, "on create ... pid: " + android.os.Process.myPid());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ALog.d(TAG, "on start command ...");
        if (intent.hasExtra("kill")) {
            ALog.d(TAG, "killed!");
            //intent.removeExtra("kill");
            //System.exit(0); //会自动重启。并且再带入这个intent.removeExtra都没有用
            Process.killProcess(Process.myPid()); //会自动重启。并且再带入这个intent.removeExtra都没有用
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return stub;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stub.mIsLive = false;
    }
}
