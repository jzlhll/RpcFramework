package com.rpcframework.aidl;

import com.rpcframework.sdk.IRemoteService;

public interface IConnMgr {
    IRemoteService getSvr();
    boolean isAlive();

    void setServicePackage(String servicePackage);
    void setServiceName(String serviceName);

    void bind();
    void unBind();
    void destroy();

    void setMgrServiceChanged(IConnMgrServiceChanged changed);
    void delMgrServiceChanged();
}
