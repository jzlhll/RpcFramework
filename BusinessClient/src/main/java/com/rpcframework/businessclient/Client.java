package com.rpcframework.businessclient;

import com.rpcframework.client.ClientSDK;

public class Client {
    public void test() {
        IClientOrder order = ClientSDK.getProxy(
                IClientOrder.class,
                ClientSDK.TYPE_INNER_PROCESS_NOT_SAME_CLASS
        );
        MemberInfo i = new MemberInfo();
        i.accountName = "dsafte";
        i.id = "897";
        boolean orderId = order.createOrder(i);
    }
}
