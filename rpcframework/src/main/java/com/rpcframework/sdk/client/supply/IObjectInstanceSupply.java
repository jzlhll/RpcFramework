package com.rpcframework.sdk.client.supply;

/**
 * 定义提供者。通过接口class，找到服务端的（或者代理服务端的）对象。
 */
public interface IObjectInstanceSupply {
    Object get(Object interfaceClassOrClassNameStr);
}
