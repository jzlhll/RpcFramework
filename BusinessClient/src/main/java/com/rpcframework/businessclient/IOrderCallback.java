package com.rpcframework.businessclient;

import com.rpcframework.ICallback;
import com.rpcframework.annotation.ServerInterfaceClassName;

@ServerInterfaceClassName("com.rpcframework.businessserver.IOrderCallback")
public interface IOrderCallback extends ICallback {
    void callback(InInfo info, String aa);
    void callback2(InInfo info, boolean aa);
}
