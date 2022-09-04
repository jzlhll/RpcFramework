package com.rpcframework.client;

import static com.rpcframework.client.ClientSDK.*;

import com.rpcframework.client.invokehandler.inprocess.InnerProcessUnknownClassHandler;
import com.rpcframework.client.invokehandler.inprocess.InnerProcessHandler;
import com.rpcframework.client.invokehandler.localsocket.LocalSocketHandler;
import com.rpcframework.client.invokehandler.other.SocketHandler;
import com.rpcframework.client.supply.IObjectInstanceSupply;

import java.lang.reflect.InvocationHandler;

final class InvokeHandlerFactory {
    public static InvocationHandler createOutProcess(String type) {
        switch (type) {
            case TYPE_SOCKET:
                return new SocketHandler();
            case TYPE_LOCAL_SOCKET:
                return new LocalSocketHandler();
            //todo Broadcast:
            //todo Messenger
            //todo aidl
        }

        return null;
    }

    public static InvocationHandler createInProcess(String type, IObjectInstanceSupply supply) {
        switch (type) {
            case TYPE_INNER_PROCESS:
                return new InnerProcessHandler(supply);
            case TYPE_INNER_PROCESS_NOT_SAME_CLASS:
                return new InnerProcessUnknownClassHandler(supply);
        }

        return null;
    }
}
