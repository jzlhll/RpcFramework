package com.rpcframework.client.invocations.process;

/**
 * 同进程下：定义提供者。通过接口class，找到服务端的（或者代理服务端的）对象。
 */
public interface IProcessObjectSupply {
    Object get(Object interfaceClassOrClassNameStr);
}
