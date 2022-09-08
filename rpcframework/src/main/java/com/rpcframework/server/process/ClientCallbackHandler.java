package com.rpcframework.server.process;

import com.rpcframework.client.BaseInvokeHandler;
import com.rpcframework.util.GsonConvertor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ClientCallbackHandler extends BaseInvokeHandler {
    //todo 目前由于我的约定。暂时value肯定是只有1个。
    private final Object clientInstance;
    private final Class<?> clientClass;
    private final Method[] clientMethods;

    //由于是回调。这里其实传递的是
    public ClientCallbackHandler(Object clientInstance) {
        this.clientInstance = clientInstance;
        clientClass = clientInstance.getClass();
        clientMethods = clientClass.getMethods();
    }

    @Override
    protected Object sendCall(Method method, Object[] args) {
        Method target = null;
        for (Method clientMethod : clientMethods) {
            if (clientMethod.getName().equals(method.getName())) {
                target = clientMethod;
                break;
            }
        }

        if (target == null) {
            return null;//这说明了，服务端的回调代码增加了。但是我们客户端的接口没有更新。并不影响。服务端不执行。因为是回调接口。执行了也没有意义
        }

        try {
            //todo 转义args
            Class<?>[] paramTypes = target.getParameterTypes();
            Class<?>[] svrParamTypes = method.getParameterTypes();
            Object[] toClientArgs = new Object[args.length];
            for (int i = 0; i < paramTypes.length; i++) {
                if (paramTypes[i].equals(svrParamTypes[i])) {
                    toClientArgs[i] = args[i];
                } else {
                    //todo 直接进行了强制转换。没有进行annotation判别了。
                    toClientArgs[i] = GsonConvertor.convert(args[i], paramTypes[i]);
                }
            }
            return target.invoke(clientInstance, toClientArgs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }
}
