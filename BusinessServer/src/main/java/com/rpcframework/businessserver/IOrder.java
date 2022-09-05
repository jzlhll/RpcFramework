package com.rpcframework.businessserver;

public interface IOrder {
    void register(String name, String password, IOrderCallback callback);
    ReturnBean canCreate(InInfo info);
}
