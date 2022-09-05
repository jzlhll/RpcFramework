package com.rpcframework.business;

public interface IHasCbBusiness {
    void register(ICallback callback);
    void unRegister(ICallback callback);
}