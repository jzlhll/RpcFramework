package com.rpcframework.client;

import com.rpcframework.pack.ReturnSerial;
import com.rpcframework.util.RpcLog;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class BaseRpcInvokeHandler implements InvocationHandler {
    protected final Class<?> clientInterface;

    public BaseRpcInvokeHandler(Class<?> clientInterface) {
        this.clientInterface = (clientInterface);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
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
        ReturnSerial response = sendCall(method, args);

        if (response.getErrorCode() != 0) {
            RpcLog.e(response.getErrorCode() + ":BaseRpcInvokeHandler invoke result error: " + response.getException());
            throw new RuntimeException("error when call server");
        }
        return response.getResult();
    }

    /**
     * @return 返回值封装的是执行的结果。并非是远端的执行结果。
     * 比如：aidl的执行过程，有同步结果我们需要把结果放在result。
     *  如果服务暂时没有连接上，并且是非void的接口，将直接抛出异常；
     *
     */
    protected abstract ReturnSerial sendCall(Method method, Object[] args);
}
