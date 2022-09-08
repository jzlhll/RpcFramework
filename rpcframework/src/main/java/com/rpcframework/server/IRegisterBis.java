package com.rpcframework.server;

import com.rpcframework.ICallback;

/**
 * 本接口，是给Bis业务接口去继承。
 * 并不一定都需要继承这个。
 * 如果服务端某个业务类支持注册监听回调的模式。就必须实现。
 *
 * 注意：客户端copy以后，必须注解上
 */
public interface IRegisterBis {
    boolean registerListener(ICallback callback);
    boolean unRegisterListener(ICallback callback);
}
