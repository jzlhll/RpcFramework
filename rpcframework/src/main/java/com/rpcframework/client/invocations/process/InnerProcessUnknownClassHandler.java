package com.rpcframework.client.invocations.process;

import com.rpcframework.annotation.ServerInterfaceClassName;
import com.rpcframework.client.ICallback;
import com.rpcframework.util.GsonConvertor;
import com.rpcframework.util.ReflectUtil;
import com.rpcframework.util.RpcLog;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class InnerProcessUnknownClassHandler extends InnerProcessHandler {

    public InnerProcessUnknownClassHandler(IProcessObjectSupply supply) {
        super(supply);
    }

    @Override
    protected Object sendCall(Class<?> methodDeclaredClass, Method method, Object[] args) {
        if (supply == null) {
            throw new RuntimeException("no supply in InnerProcessHandler!");
        }

        //1. 本进程，而且interfaceClass也是直接服务端的key。
        Object object = supply.get(methodDeclaredClass);
        if (object != null) {
            //只要获取到了，不论啥情况都return。因为就是这个class
            try {
                return method.invoke(object, args);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            return null;
        }

        //2. 本进程，interfaceClass并非服务端存的key interfaceClass，我们就要构造出它
        ServerInterfaceClassName annotation = methodDeclaredClass.getAnnotation(ServerInterfaceClassName.class);
        String clsName;
        if (annotation != null) { //如果有注解，我们就使用注解
            clsName = annotation.value();
        } else { //没有注解，我们则认为跟远端同接口。比较适用于不同app
            clsName = methodDeclaredClass.getName();
        }

        object = supply.get(clsName);
        if (object == null) {
            RpcLog.e("cannot find service impl of: " + methodDeclaredClass);
            return null;
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
                if (paramType.isInterface() && paramType.isAssignableFrom(ICallback.class)) { //证明是一个回调接口
                    //todo 是回调接口类型，就不能如上转了，需要构建出服务端的callback并代理上客户端的代码。
                    //思考后：这里都需要将对象
                    // 同进程：直接将接口传递过去，虽然服务端无法直接获取，但可以在服务端可以基于这个对象getMethod列表；
                    // 跨进程：这个对象需要是aidl对象或者转变为list<函数名+参数类型+返回值类型>传递。后续基于exchanger交换。
                    // todo 跨进程实现的时候，转变为list<函数名+参数类型+返回值类型>
                    svrArgs[i] = args[i];
                    svrParamTypes[i] = paramType;
                } else {
                    //证明是参数
                    //如果有标注注解；就是服务端的自定义对象。我们就要把参数转变过去
                    ServerInterfaceClassName an = paramType.getAnnotation(ServerInterfaceClassName.class);
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
                        svrArgs[i] = args[i];
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
