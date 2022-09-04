package com.rpcframework.businessclient;

import com.rpcframework.annotation.ServerInterfaceClassName;

@ServerInterfaceClassName("com.rpcframework.businessserver.business.IOrder")
public interface IClientOrder {
    boolean createOrder(MemberInfo memberInfo);
}
