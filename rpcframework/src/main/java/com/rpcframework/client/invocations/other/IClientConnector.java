package com.rpcframework.client.invocations.other;

import com.rpcframework.pack.CallSerial;
import com.rpcframework.pack.ReturnSerial;

interface IClientConnector {
    boolean isConnected();
    void connect();
    ReturnSerial sendCall(CallSerial call);
    void disconnect();
}
