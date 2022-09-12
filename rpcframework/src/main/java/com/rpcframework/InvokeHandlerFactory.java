package com.rpcframework;

import static com.rpcframework.ClientSDK.*;

import com.rpcframework.aidl.IConnMgr;
import com.rpcframework.client.IClientMgr;
import com.rpcframework.client.invocations.android.AidlUnknownClassHandler;
import com.rpcframework.client.invocations.process.IProcessObjectSupply;
import com.rpcframework.client.invocations.process.InnerProcessUnknownClassHandler;
import com.rpcframework.client.invocations.process.InnerProcessHandler;
import com.rpcframework.client.invocations.other.LocalSocketHandler;
import com.rpcframework.client.invocations.other.SocketHandler;

import java.lang.reflect.InvocationHandler;

final class InvokeHandlerFactory {
    public static InvocationHandler createOutProcess(Class<?> clientInterface, String type) {
        switch (type) {
            case TYPE_SOCKET:
                return new SocketHandler(clientInterface);
            case TYPE_LOCAL_SOCKET:
                return new LocalSocketHandler(clientInterface);
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

    public static InvocationHandler createAidl(Class<?> clientInterface, IClientMgr mgr) {
        return new AidlUnknownClassHandler(clientInterface, mgr);
    }
}
