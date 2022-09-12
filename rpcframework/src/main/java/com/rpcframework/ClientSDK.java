package com.rpcframework;

import android.content.Context;

import com.rpcframework.client.ClientConnMgrs;
import com.rpcframework.client.IClientMgr;
import com.rpcframework.client.invocations.process.IProcessObjectSupply;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class ClientSDK {
    private ClientSDK() {}

    /**
     * @param interfaceClass 客户端的接口
     * @param type 同进程的模式，只有 TYPE_INNER_PROCESS_NOT_SAME_CLASS 和 TYPE_INNER_PROCESS 可选
     *             不同进程，目前支持TYPE_LOCAL_SOCKET。
     */
    public static <T> T getProxyInProcess(Class<T> interfaceClass, String type) {
        InvocationHandler handler = null;
        if (TYPE_INNER_PROCESS.equals(type)
            || TYPE_INNER_PROCESS_NOT_SAME_CLASS.equals(type)) {
            IProcessObjectSupply supply = (interfaceOrClsName -> {
                if (interfaceOrClsName instanceof String) {
                    return ServerSDK.INSTANCE.getManager().get((String) interfaceOrClsName);
                } else {
                    return ServerSDK.INSTANCE.getManager().get((Class<?>) interfaceOrClsName);
                }
            });
            handler = InvokeHandlerFactory.createInProcess(interfaceClass, type, supply);
        }

        assert handler != null;
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass},
                handler);
    }

    //clinetMgr请传入new IClientMgr[1];将会给你一个mgr。
    //这个mgr是根据svrPkg，svrName进程唯一的。
    public static <T> T getProxyAidl(Context context, Class<T> interfaceClass,
                  String svrPkg, String serviceName, IClientMgr[] clientMgr) {
        AidlBuilder<T> b = new AidlBuilder<>();
        IClientMgr mgr = b.setSvrPkg(svrPkg).setSvrName(serviceName).createClientMgr(context);
        clientMgr[0] = mgr;
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass},
                InvokeHandlerFactory.createAidl(interfaceClass, mgr));
    }

    /**
     * 同一个进程；并且业务接口类，服务端和客户端都可直接访问
     */
    public static final String TYPE_INNER_PROCESS  = "innerProcess";
    /**
     * 同一个进程；但是业务接口类，服务端和客户端为不同类；通过注解来标识服务端接口
     */
    public static final String TYPE_INNER_PROCESS_NOT_SAME_CLASS  = "innerProcessNotSameClass";

    public static final String TYPE_SOCKET         = "socket";
    /**
     * 不同进程；采用localSocket作为通信基础
     */
    public static final String TYPE_LOCAL_SOCKET   = "localSocket";
    public static final String TYPE_AIDL           = "binder";
    public static final String TYPE_MESSENGER      = "messenger";

    private static final class AidlBuilder<T> {
        private String svrName, svrPkg;

        public AidlBuilder<T> setSvrName(String svrName) {
            this.svrName = svrName;
            return this;
        }

        public AidlBuilder<T> setSvrPkg(String svrPkg) {
            this.svrPkg = svrPkg;
            return this;
        }

        public IClientMgr createClientMgr(Context context) {
            return ClientConnMgrs.createConnection(context, svrPkg, svrName);
        }
    }
}


