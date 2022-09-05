package com.rpcframework.businessclient;

import com.rpcframework.annotation.ServerInterfaceClassName;

@ServerInterfaceClassName("com.rpcframework.businessserver.Info")
public interface IOrder {
    void register(String name, String password, IOrderCallback callback);
    ReturnBean canCreate(InInfo info);

    void register();
}
