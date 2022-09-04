package com.rpcframework.businessclient;

import android.util.Log;

import com.rpcframework.client.ClientSDK;

public class Client {
    public void test() {
        IClientOrder order = ClientSDK.getRemoteProxyInProcess(
                IClientOrder.class,
                ClientSDK.TYPE_INNER_PROCESS_NOT_SAME_CLASS
        );
        //boolean orderId = order.createOrder();
        //Log.w("allan", "orderId: " + orderId);

        IMyMember m = ClientSDK.getRemoteProxyInProcess(IMyMember.class,
                ClientSDK.TYPE_INNER_PROCESS_NOT_SAME_CLASS);
        //ERROR: 因为服务端的是MemberBean；所以这里类型是无法强转的
        MemberInfo info = m.createAccount("allan", "123456");
        Log.w("allan", "memberInfo: " + info);
    }
}
