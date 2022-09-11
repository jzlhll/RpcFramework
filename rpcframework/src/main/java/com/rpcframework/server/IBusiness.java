package com.rpcframework.server;

/**
 * 本接口，是给Business业务接口去继承。
 * 如果服务端某个业务类支持注册监听回调的模式。就必须实现来自本接口。
 * 如果没有注册回调的需要，则不用实现。
 */
public interface IBusiness {
    boolean addListener(ICallback callback);
    boolean removeListener(ICallback callback);

    /**
     * 请返回你给客户端提供的callback子类接口类型。
     */
    Class<? extends ICallback> getCallbackClass();


    /**
     * 一个空接口。凡是需要注册到服务端的回调接口必须实现它。
     * 暂时给出几个规矩：
     * 1. 函数不得重名
     */
    interface ICallback {
    }
}