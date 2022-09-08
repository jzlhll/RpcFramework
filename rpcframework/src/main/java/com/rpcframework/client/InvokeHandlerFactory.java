package com.rpcframework.client;

import static com.rpcframework.client.ClientSDK.*;

import com.rpcframework.client.invocations.process.IProcessObjectSupply;
import com.rpcframework.client.invocations.process.InnerProcessUnknownClassHandler;
import com.rpcframework.client.invocations.process.InnerProcessHandler;
import com.rpcframework.client.invocations.socket.LocalSocketHandler;
import com.rpcframework.client.invocations.other.SocketHandler;

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

    public static InvocationHandler createInProcess(Class<?> clientInterface, String type, IProcessObjectSupply supply) {
        switch (type) {
            case TYPE_INNER_PROCESS:
                return new InnerProcessHandler(clientInterface, supply);
            case TYPE_INNER_PROCESS_NOT_SAME_CLASS:
                return new InnerProcessUnknownClassHandler(clientInterface, supply);
        }

        return null;
    }
}
