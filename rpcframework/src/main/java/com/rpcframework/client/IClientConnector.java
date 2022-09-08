package com.rpcframework.client;

import com.rpcframework.Call;
import com.rpcframework.ReturnParcel;

public interface IClientConnector {
    boolean isConnected();
    void connect();
    ReturnParcel sendCall(Call call);
    void disconnect();
}
