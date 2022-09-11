package com.rpcframework.aidl;

import com.rpcframework.sdk.IRemoteService;

public interface IConnMgr {
    IRemoteService getSvr();
    void bind();
    void unBind();
    void destroy();
}
