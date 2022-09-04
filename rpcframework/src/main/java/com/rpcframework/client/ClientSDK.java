package com.rpcframework.client;

import com.rpcframework.client.supply.IObjectInstanceSupply;
import com.rpcframework.server.Server;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ClientSDK {
    private ClientSDK() {}

    /**
     * 从同进程，且业务接口是一个。
     * @param interfaceClass 客户端的接口
     * @param type 同进程的模式，只有TYPE_INNER_PROCESS_NO_CLASS和TYPE_INNER_PROCESS可选
     */
    public static <T> T getRemoteProxyInProcess(Class<T> interfaceClass, String type) {
        if (RpcHandlerFactory.TYPE_INNER_PROCESS.equals(type)
            || RpcHandlerFactory.TYPE_INNER_PROCESS_NO_CLASS.equals(type)) {
            IObjectInstanceSupply supply = (interfaceOrClsName -> {
                if (interfaceOrClsName instanceof String) {
                    return Server.INSTANCE.getManager().get((String) interfaceOrClsName);
                } else {
                    return Server.INSTANCE.getManager().get((Class<?>) interfaceOrClsName);
                }
            });
            RpcHandler handler = RpcHandlerFactory.create(type, supply);
            assert handler != null;
            return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class<?>[]{interfaceClass},
                    handler);
        }
        throw new RuntimeException("Error: call getRemoteProxyInProcess() with type: " + type);
    }

    public abstract static class RpcHandler implements InvocationHandler {
        public RpcHandler(IObjectInstanceSupply supply) {
            this.supply = supply;
        }
        //通过业务接口查找对象的提供者
        protected final IObjectInstanceSupply supply;

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) {
            if (supply == null) {
                throw new RuntimeException("no supply in InnerProcessHandler!");
            }

            //避免hashCode，toString，equals出错
            Class<?> methodDeclaringClass = method.getDeclaringClass();
            if (Object.class.equals(methodDeclaringClass)) {
                try {
                    return method.invoke(this, args);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                    return null;
                }
            }
            return sendCall(methodDeclaringClass, method, args);
        }

        /**
         * @param method InvocationHandler的invoke函数的参数。因此这个method是代理类Proxy的函数体
         * @param args 略
         * @return 略
         */
        protected abstract Object sendCall(Class<?> methodDeclaringClass, Method method, Object[] args);
    }
}
