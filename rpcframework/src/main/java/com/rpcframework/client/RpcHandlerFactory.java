package com.rpcframework.client;

import com.rpcframework.client.inprocesshandler.InnerProcessUnknownClassHandler;
import com.rpcframework.client.inprocesshandler.InnerProcessHandler;
import com.rpcframework.client.sockethandler.LocalSocketHandler;
import com.rpcframework.client.sockethandler.SocketHandler;
import com.rpcframework.client.supply.IObjectInstanceSupply;

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
                return new SocketHandler(supply);
            case TYPE_LOCAL_SOCKET:
                return new LocalSocketHandler(supply);
            //todo Broadcast:
            //todo Messenger
            //todo aidl
        }

        return null;
    }
}
