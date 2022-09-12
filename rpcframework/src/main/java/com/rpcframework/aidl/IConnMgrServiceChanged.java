package com.rpcframework.aidl;

public interface IConnMgrServiceChanged {
    /**
     * @param bindState 如果true，你应该需要调用一下IConnMgr.getSvr获取一下。
     *                  如果false，则应该不能操作svr
     */
    void onServiceChanged(boolean bindState);
}
