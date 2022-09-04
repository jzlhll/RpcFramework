package com.rpcframework.businessclient;

import com.rpcframework.annotation.ServerInterfaceClassName;

@ServerInterfaceClassName("com.rpcframework.businessserver.business.IOrder")
public interface IClientOrder {
    /////使用手册：
    //返回值的class上不用加注解；
    //入参如果是自定义类型，必须加注解。
    OrderBean createOrder(MemberInfo memberInfo);
}
