package com.rpcframework.client.invocations.process;

import android.util.Log;

import com.rpcframework.annotation.MappingSameClassAnnotation;
import com.rpcframework.ICallback;
import com.rpcframework.server.IHasCallbackBis;
import com.rpcframework.server.process.ClientCallbackHandler;
import com.rpcframework.util.GsonConvertor;
import com.rpcframework.util.ReflectUtil;
import com.rpcframework.util.RpcLog;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public final class InnerProcessUnknownClassHandler extends InnerProcessHandler {
    public InnerProcessUnknownClassHandler(Class<?> clientInterface, IProcessObjectSupply supply) {
        super(clientInterface, supply);
    }

    private Class<?> svrInterfaceClass;

    private Class<?> cvtInterfaceClass() {
        if (svrInterfaceClass == null) {
            svrInterfaceClass = ReflectUtil.clientClassToSvrClass(clientInterface);
        }
        return svrInterfaceClass;
    }

    //如果是注册函数则处理一下。
    private boolean doIRegisterBisMethods(Method method, Object[] args, Object svrInstance) {
        String name = method.getName();
        Boolean b = null;
        if ("registerListener".equals(name)) {
            b = Boolean.TRUE;
        } else if ("unRegisterListener".equals(name)) {
            b = Boolean.FALSE;
        }

        if (b == null) {
            return false;
        }

        if (!(svrInstance instanceof IHasCallbackBis)) {
            RpcLog.d("doIRegisterBisMethods svrInstance is not an IRegisterBis");
            return false;
        }

        Class<?>[] paramTypes = method.getParameterTypes();
        if (paramTypes.length != 1) {
            throw new RuntimeException("Error01 param of " + method.getDeclaringClass() + ", " + name);
        }
        //todo 改成泛型，减少一个注解
        Class<?> paramType = ((IHasCallbackBis) svrInstance).getCallbackClass();
        IHasCallbackBis svrInstanceBis = (IHasCallbackBis) svrInstance;

        //证明是一个回调接口
        //是回调接口类型，就不能如上直接转了，需要构建出服务端的callback并代理上客户端的代码。
        //思考后：这里都需要将对象
        // 同进程：直接将接口传递过去，虽然服务端无法直接获取，但可以在服务端可以基于这个对象getMethod列表；
        // 跨进程 todo：这个对象需要是aidl对象或者转变为list<函数名+参数类型+返回值类型>传递。后续基于exchanger交换。
        // 跨进程实现的时候，转变为list<函数名+参数类型+返回值类型>
        //todo 这里就要求客户端的回调ICallback接口设计，不能有非bean，非基础类型的参数。

        //得到服务端接口类 todo 加速
        Class<?> svrCallbackClass = ReflectUtil.clientClassToSvrClass(paramType);
        //再创建代理
        ICallback o = (ICallback) Proxy.newProxyInstance(ICallback.class.getClassLoader(),
                new Class[] {svrCallbackClass},
                new ClientCallbackHandler(args[0]));
        boolean r = svrInstanceBis.registerListener(o);
        Log.d("allan", "r " + r);
        return true;
    }

    @Override
    protected Object sendCall(Method method, Object[] args) {
        if (supply == null) {
            throw new RuntimeException("no supply in InnerProcessHandler!");
        }

        //1. 本进程，而且interfaceClass也是直接服务端的key。
        Object object = supply.get(clientInterface);
        if (object != null) {
            //todo 可以实现，让服务端动态创建实现类。而不是一开始要求服务端已经初始化好了所有bis类。
            //只要获取到了，不论啥情况都return。因为就是这个class
            try {
                return method.invoke(object, args);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            return null;
        }

        //2. 本进程，interfaceClass并非服务端存的key interfaceClass，我们就要构造出它
        String clsName = ReflectUtil.clientClassToSvrClassName(cvtInterfaceClass());
        object = supply.get(clsName);
        if (object == null) {
            RpcLog.e("cannot find service has bis: " + cvtInterfaceClass());
            return null;
        }

        if (doIRegisterBisMethods(method, args, object)) {
            return true;
        }

        //todo 如下代码还可以做全局缓存加速
        try {
            //将接口转变为服务器的接口
            //将参数类型和参数对象，逐一转变为服务端类型和对象
            Class<?>[] paramTypes = method.getParameterTypes();
            Class<?>[] svrParamTypes = new Class[paramTypes.length];
            Object[] svrArgs = new Object[args.length];
            int i = 0;
            for (Class<?> paramType : paramTypes) {
                {
                    //证明是参数
                    //如果有标注注解；就是服务端的自定义对象。我们就要把参数转变过去
                    MappingSameClassAnnotation an = paramType.getAnnotation(MappingSameClassAnnotation.class);
                    if (an != null && an.value() != null) {
                        Class<?> svrParamType = ReflectUtil.classForName(an.value());
                        svrParamTypes[i] = svrParamType;
                        if (svrParamType != null && !svrParamType.isInterface()) {
                            svrArgs[i] = GsonConvertor.convert(args[i], svrParamType);
                        } else {
                            throw new RuntimeException("Error01 when parse svrParamType");
                        }
                    } else {
                        svrParamTypes[i] = paramType;
                        svrArgs[i] = args[i]; //todo 如果这里是跨进程还是需要转换。
                    }
                }
                i++;
            }

            Method svrMethod = object.getClass().getMethod(method.getName(), svrParamTypes);
            Object result = svrMethod.invoke(object, svrArgs);
            Class<?> clientReturnType = method.getReturnType();
            //注意：虽然我们经过转换拿到了svrMethod，但是这里得到的result，仍然是服务端的return type。
            //由于返回type可能是原生的，也可能是自定义类。如果能同时拿到就还好。
            // 拿不到，就得转换。所以需要转换为客户端我们的return type。
            if (void.class.equals(clientReturnType) || Void.class.equals(clientReturnType)) {
                return null;
            } else {
                Class<?> svrReturnType = svrMethod.getReturnType();
                if (svrReturnType.equals(clientReturnType)) {
                    return result;
                } else {
                    return GsonConvertor.convert(result, clientReturnType);
                }
            }
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
}
