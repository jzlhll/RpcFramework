package com.rpcframework.conn;

import com.rpcframework.aidlstudy.IRemoteService;

public interface IConnMgr {
    IRemoteService getAidl();
    void bind();
    void unBind();
    void destroy();
}
