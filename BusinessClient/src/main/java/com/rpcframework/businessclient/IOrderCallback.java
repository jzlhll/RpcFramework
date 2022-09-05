package com.rpcframework.businessclient;

import com.rpcframework.annotation.ServerInterfaceClassName;

@ServerInterfaceClassName("com.rpcframework.businessserver.Info")
public interface IOrderCallback {
    void onOrder(ReturnBean bean, String name);
}
