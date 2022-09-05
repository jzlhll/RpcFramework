package com.rpcframework.businessclient;


import android.util.Log;

import com.rpcframework.client.ClientSDK;

public class ClientTest {
    public void test() {
        IOrder order = ClientSDK.getProxyInProcess(IOrder.class, ClientSDK.TYPE_INNER_PROCESS_NOT_SAME_CLASS);
        order.register("allan", "12345", new IOrderCallback() {
            @Override
            public void onOrder(ReturnBean bean, String name) {
                Log.w("allan", name + " :bean " + bean.r + ", " + bean.s);
            }
        });
    }
}
