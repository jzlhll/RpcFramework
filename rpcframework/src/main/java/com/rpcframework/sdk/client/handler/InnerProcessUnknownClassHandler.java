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
    protected Object sendCall(Method method, Object[] args) {
        //避免hashCode，toString，equals出错
        Class<?> interfaceClass = method.getDeclaringClass();
        if (Object.class.equals(interfaceClass)) {
            try {
                return method.invoke(this, args);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                return null;
            }
        }

        //首先，先用同进程，同业务接口方式获取。
        Object object = super.sendCall(method, args);
        if (object != null) {
            return object;
        }

        //然后，获取不到了。
        ServerInterfaceClassName ann = getInterfaceClass().getDeclaredAnnotation(ServerInterfaceClassName.class);
        String clsName;
        if (ann != null) { //如果有注解，我们就使用注解
            clsName = ann.value();
        } else { //没有注解，我们认为跟远端同接口。todo 比较适用于不同app
            clsName = getInterfaceClass().getName();
        }

        object = supply.get(clsName);
        if (object != null) {
            try {
                //测试，同函数签名即可？
                return method.invoke(object, args);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                return null;
            }
        }

        //todo Call call = new Call(clsName, method.getName(), method.getParameterTypes(), args);
        throw new RuntimeException("cannot find service impl of: " + interfaceClass);
    }
}