package com.rpcframework.sdk.client;

import com.rpcframework.sdk.client.handler.InnerProcessUnknownClassHandler;
import com.rpcframework.sdk.client.handler.InnerProcessHandler;
import com.rpcframework.sdk.client.handler.SocketRpcHandler;
import com.rpcframework.sdk.client.supply.IObjectInstanceSupply;

public final class RpcHandlerFactory {
    public static final String TYPE_INNER_PROCESS  = "innerProcess";
    public static final String TYPE_INNER_PROCESS_NO_CLASS  = "innerProcessNotKnownClass";
    public static final String TYPE_SOCKET         = "socket";
    public static final String TYPE_LOCAL_SOCKET   = "localSocket";
    public static final String TYPE_AIDL           = "binder";
    public static final String TYPE_MESSENGER      = "messenger";

    public static ClientSDK.RpcHandler create(String type, IObjectInstanceSupply supply) {
        switch (type) {
            case TYPE_INNER_PROCESS:
                return new InnerProcessHandler(supply);
            case TYPE_INNER_PROCESS_NO_CLASS:
                return new InnerProcessUnknownClassHandler(supply);
            case TYPE_SOCKET:
                return new SocketRpcHandler(supply);
            //todo Broadcast:
            //todo Messenger
            //todo aidl
        }

        return null;
    }
}
