package com.rpcframework.client;

import com.rpcframework.Call;
import com.rpcframework.ReturnPack;

public interface IClientConnector {
    boolean isConnected();
    void connect();
    ReturnPack sendCall(Call call);
    void disconnect();
}
