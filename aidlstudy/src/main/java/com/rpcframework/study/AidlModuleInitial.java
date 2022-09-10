package com.rpcframework.study;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.startup.Initializer;

import com.rpcframework.aidlstudy.MyRemoteService;
import com.rpcframework.log.ALog;

import java.util.ArrayList;
import java.util.List;

public class AidlModuleInitial implements Initializer<String> {
    @NonNull
    @Override
    public String create(@NonNull Context context) {
        ALog.d( "AidlModuleInitial... pid: " + android.os.Process.myPid());
        context.startService(new Intent(context, MyRemoteService.class));
        return "init remote service";
    }

    @NonNull
    @Override
    public List<Class<? extends Initializer<?>>> dependencies() {
        return new ArrayList<>(0);
    }
}
