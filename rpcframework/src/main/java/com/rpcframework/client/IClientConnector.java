package com.rpcframework.client;

import com.rpcframework.Call;
import com.rpcframework.RpcReturnVal;

public interface IClientConnector {
    boolean isConnected();
    void connect();
    RpcReturnVal sendCall(Call call);
    void disconnect();
}
