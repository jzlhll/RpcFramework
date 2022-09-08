package com.rpcframework.server;

import com.rpcframework.ICallback;

import java.util.HashSet;
import java.util.Set;

class RegisterBisDel implements IRegisterBis{
    private final Object callbackSetLock = new Object();

    public Set<ICallback> getCallbackSet() {
        return callbackSet;
    }

    private Set<ICallback> callbackSet;

    @Override
    public boolean registerListener(ICallback callback) {
        synchronized (callbackSetLock) {
            if (callbackSet == null) {
                callbackSet = new HashSet<>(1);
            }

            callbackSet.add(callback);
        }
        return true;
    }

    @Override
    public boolean unRegisterListener(ICallback callback) {
        synchronized (callbackSetLock) {
            callbackSet.remove(callback);
        }
        return true;
    }
}
