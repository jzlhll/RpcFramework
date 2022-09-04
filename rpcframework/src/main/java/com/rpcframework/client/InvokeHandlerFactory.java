package com.rpcframework.client;

import static com.rpcframework.client.ClientSDK.*;

import com.rpcframework.client.invokehandler.inprocess.InnerProcessUnknownClassHandler;
import com.rpcframework.client.invokehandler.inprocess.InnerProcessHandler;
import com.rpcframework.client.invokehandler.socket.LocalSocketHandler;
import com.rpcframework.client.invokehandler.socket.SocketHandler;
import com.rpcframework.client.supply.IObjectInstanceSupply;

final class InvokeHandlerFactory {
    public static BaseInvokeHandler create(String type, IObjectInstanceSupply supply) {
        switch (type) {
            case TYPE_INNER_PROCESS:
                return new InnerProcessHandler(supply);
            case TYPE_INNER_PROCESS_NOT_SAME_CLASS:
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
