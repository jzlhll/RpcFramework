package com.rpcframework.client;

import com.rpcframework.aidl.IConnMgrServiceChanged;
import com.rpcframework.pack.ReturnSerial;

/**
 * 屏蔽掉Aidl的 IConnMgr 对于sdk中的IRemoteService的使用细节。
 */
public interface IClientMgr {
    boolean isAlive();

    /**
     * 只要在onCreate等生命周期开始以后，不用再关注了，内部将会自动重连服务端。
     */
    void connect();
    /**
     * 彻底断开服务端。todo 目前设计类似为进程单例。不得随意调用。
     */
    void disConnect();

    /**
     * 会使用weakRef帮你保存。因此不太需要反注册。
     */
    void addMgrServiceChanged(IConnMgrServiceChanged changed);
    void removeMgrServiceChanged(IConnMgrServiceChanged changed);
}
