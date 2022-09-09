package com.rpcframework.client;

import com.rpcframework.pack.Call;
import com.rpcframework.pack.ReturnParcel;

public interface IClientConnector {
    boolean isConnected();
    void connect();
    ReturnParcel sendCall(Call call);
    void disconnect();
}
