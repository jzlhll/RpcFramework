package com.rpcframework.businessclient;

import com.rpcframework.annotation.ServerInterfaceClassName;
import com.rpcframework.server.IHasCallbackBis;

@ServerInterfaceClassName("com.rpcframework.businessserver.IOrderBis")
public interface IOrderBis extends IHasCallbackBis {
    ReturnBean markAOrder(InInfo info, String name);
}