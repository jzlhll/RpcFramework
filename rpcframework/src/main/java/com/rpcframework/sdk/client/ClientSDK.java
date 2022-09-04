package com.rpcframework.sdk.client;

import com.rpcframework.sdk.client.supply.IObjectInstanceSupply;
import com.rpcframework.sdk.server.Server;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ClientSDK {
    private ClientSDK() {}

    /**
     * 从同进程，且业务接口是一个。
     */
    public static <T> T getRemoteProxyInProcess(Class<T> interfaceClass, String type) {
        IObjectInstanceSupply supply = (interfaceOrClsName -> {
            if (interfaceOrClsName instanceof String) {
                return Server.INSTANCE.getManager().get((String) interfaceOrClsName);
            } else {
                return Server.INSTANCE.getManager().get((Class<?>) interfaceOrClsName);
            }
        });
        RpcHandler handler = RpcHandlerFactory.create(type, supply);
        assert handler != null;
        handler.setInterfaceClass(interfaceClass);
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class<?>[]{interfaceClass},
                handler);
    }

    public abstract static class RpcHandler implements InvocationHandler {
        public RpcHandler(IObjectInstanceSupply supply) {
            this.supply = supply;
        }
        //业务的接口
        private Class<?> interfaceClass;
        //通过业务接口查找对象的提供者
        protected final IObjectInstanceSupply supply;

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) {
            return sendCall(method, args);
        }

        /**
         * @param method InvocationHandler的invoke函数的参数。因此这个method是代理类Proxy的函数体
         * @param args 略
         * @return 略
         */
        protected abstract Object sendCall(Method method, Object[] args);

        public final Class<?> getInterfaceClass() {
            return interfaceClass;
        }

        public final void setInterfaceClass(Class<?> interfaceClass) {
            this.interfaceClass = interfaceClass;
        }
    }
}
