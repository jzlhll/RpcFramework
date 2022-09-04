package com.rpcframework.businessclient;

import com.rpcframework.sdk.ServerInterfaceClassName;

@ServerInterfaceClassName("com.rpcframework.businessserver.business.IOrder")
public interface IClientOrder {
    boolean createOrder();
}
