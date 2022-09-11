package com.rpcframework.client;

import com.rpcframework.pack.CallSerial;
import com.rpcframework.pack.ReturnSerial;

public interface IClientConnector {
    boolean isConnected();
    void connect();
    ReturnSerial sendCall(CallSerial call);
    void disconnect();
}
