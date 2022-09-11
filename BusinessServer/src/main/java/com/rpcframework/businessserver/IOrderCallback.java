package com.rpcframework.businessserver;

import com.rpcframework.server.IBusiness.ICallback;

public interface IOrderCallback extends ICallback {
    void callback(InInfo info, String aa);
    void callback2(InInfo info, boolean aa);
}
