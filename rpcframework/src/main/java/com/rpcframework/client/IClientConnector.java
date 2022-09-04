package com.rpcframework.client;

import com.rpcframework.Call;

public interface IClientConnector {
    boolean isConnected();
    void connect();
    void sendCall(Call call);
    void disconnect();
}
