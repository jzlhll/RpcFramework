package com.rpcframework.sdk.client.handler;

import com.rpcframework.sdk.ServerInterfaceClassName;
import com.rpcframework.sdk.client.Call;
import com.rpcframework.sdk.client.ClientSDK;
import com.rpcframework.sdk.client.supply.IObjectInstanceSupply;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class InnerProcessUnknownClassHandler extends InnerProcessHandler {

    public InnerProcessUnknownClassHandler(IObjectInstanceSupply supply) {
        super(supply);
    }

    @Override
    protected Object sendCallInner(Class<?> methodDeclaredClass, Method method, Object[] args) {
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

        //2. 本进程，interfaceClass并非服务端存的key，我们就要构造出服务端的key
        ServerInterfaceClassName ann = getInterfaceClass().getAnnotation(ServerInterfaceClassName.class);
        String clsName;
        if (ann != null) { //如果有注解，我们就使用注解
            clsName = ann.value();
        } else { //没有注解，我们则认为跟远端同接口。比较适用于不同app
            clsName = getInterfaceClass().getName();
        }

        object = supply.get(clsName);
        if (object != null) {
            try {
                //将接口转变为服务器的接口
                Method svrMethod =  object.getClass().getMethod(method.getName(), method.getParameterTypes());
                return svrMethod.invoke(object, args);
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
                return null;
            }
        }

        throw new RuntimeException("cannot find service impl of: " + methodDeclaredClass);
    }
}