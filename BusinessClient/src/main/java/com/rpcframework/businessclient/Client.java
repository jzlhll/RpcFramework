package com.rpcframework.businessclient;

import android.util.Log;

import com.rpcframework.client.ClientSDK;
import com.rpcframework.client.RpcHandlerFactory;

public class Client {
    public void test() {
        IClientOrder order = ClientSDK.getRemoteProxyInProcess(
                IClientOrder.class,
                RpcHandlerFactory.TYPE_INNER_PROCESS_NO_CLASS
        );
        //boolean orderId = order.createOrder();
        //Log.w("allan", "orderId: " + orderId);

        IMyMember m = ClientSDK.getRemoteProxyInProcess(IMyMember.class, RpcHandlerFactory.TYPE_INNER_PROCESS_NO_CLASS);
        //ERROR: 因为服务端的是MemberBean；所以这里类型是无法强转的
        MemberInfo info = m.createAccount("allan", "123456");
        Log.w("allan", "memberInfo: " + info);
    }
}
