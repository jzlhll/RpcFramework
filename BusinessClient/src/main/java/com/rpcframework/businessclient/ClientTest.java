package com.rpcframework.businessclient;

import android.util.Log;

import com.rpcframework.client.ClientSDK;

public class ClientTest {
    private static final String TAG = "ClientTest";

    public void test() {
        IOrderBis order = ClientSDK.getProxyInProcess(IOrderBis.class, ClientSDK.TYPE_INNER_PROCESS_NOT_SAME_CLASS);
        order.registerListener(new IOrderCallback() {
            @Override
            public void callback(InInfo info, String aa) {
                Log.d(TAG, "callback info: " + info.info + ", " + info.info2 + ", aa " + aa);
            }

            @Override
            public void callback2(InInfo info, boolean aa) {
                Log.d(TAG, "callback2 info: " + info.info + ", " + info.info2 + ", aa " + aa);
            }
        });

        InInfo info = new InInfo();
        info.info = "clientTest";
        ReturnBean returnBean = order.markAOrder(info, "client007");
        Log.d(TAG, "return bean " + returnBean.r + ", " + returnBean.s);
    }
}
