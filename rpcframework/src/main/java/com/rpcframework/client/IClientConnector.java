package com.rpcframework.client;

import com.rpcframework.Call;
import com.rpcframework.Response;

public interface IClientConnector {
    boolean isConnected();
    void connect();
    Response sendCall(Call call);
    void disconnect();
}
