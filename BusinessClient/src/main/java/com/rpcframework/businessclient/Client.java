package com.rpcframework.businessclient;

import android.util.Log;

import com.rpcframework.sdk.client.ClientSDK;
import com.rpcframework.sdk.client.RpcHandlerFactory;

public class Client {
    public void test() {
        IClientOrder order = ClientSDK.getRemoteProxyInProcess(
                IClientOrder.class,
                RpcHandlerFactory.TYPE_INNER_PROCESS_NO_CLASS
        );
        String orderId = order.createOrder();
        Log.d("allan", "orderId: " + orderId);
    }
}
