package com.rpcframework.businessclient;

import com.rpcframework.annotation.RegisterCallback;
import com.rpcframework.annotation.ServerInterfaceClassName;
import com.rpcframework.server.IRegisterBis;

@ServerInterfaceClassName("com.rpcframework.businessserver.IOrderBis")
@RegisterCallback("com.rpcframework.businessserver.IOrderCallback")
public interface IOrderBis extends IRegisterBis {
    ReturnBean markAOrder(InInfo info, String name);
}