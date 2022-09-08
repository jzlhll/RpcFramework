package com.rpcframework.businessserver;

import android.util.Log;

import com.rpcframework.ICallback;

import java.util.HashSet;
import java.util.Set;

public class OrderHasCallbackImpl implements IOrderBis {
    private static final String TAG = "OrderHasCallbackImpl";
    public Set<ICallback> getCallbackSet() {
        return callbackSet;
    }
    private final Set<ICallback> callbackSet = new HashSet<>();

    @Override
    public ReturnBean markAOrder(InInfo info, String name) {
        ReturnBean bean = new ReturnBean();
        bean.r = true;
        bean.s = "xxxjfjffjdfjdf";
        Log.d(TAG, "mark A Order " + info.info + "," + info.info2 + ", " + name);

        new Thread(()->{
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (ICallback cb : callbackSet) {
                InInfo ininfo = new InInfo();
                ininfo.info = "delay10";
                ininfo.info2 = "s";
                ((IOrderCallback)cb).callback(ininfo, "adfdsf");
            }
        }).start();
        return bean;
    }

    @Override
    public boolean registerListener(ICallback callback) {
        return callbackSet.add(callback);
    }

    @Override
    public boolean unRegisterListener(ICallback callback) {
        return callbackSet.remove(callback);
    }
}
