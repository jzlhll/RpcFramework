package com.rpcframework.client;

import java.io.Serializable;

/**
 * 创建封装客户端请求和返回结果信息的Call类
 * 为了便于按照面向对象的方式来处理客户端与服务器端的通信,可以把它们发送的信息用 Call 类来表示。
 * 一个 Call 对象表示客户端发起的一个远程调用,
 * 它包括调用的类名或接口名、方法名、方法参数类型、方法参数值和方法执行结果。
 */
public class Call implements Serializable {
    private static final long serialVersionUID = -2200841337929164L;

    public Call() {}
    public Call(String className, String methodName, Class<?>[] paramTypes, Object[] params) {
        this.className = className;
        this.methodName = methodName;
        this.paramTypes = paramTypes;
        this.params = params;
    }

    private String className; // 调用的类名或接口名
    private String methodName; // 调用的方法名
    private Class<?>[] paramTypes; // 方法参数类型
    private Object[] params; // 调用方法时传入的参数值
    /**
     * 表示方法的执行结果 如果方法正常执行,则 result 为方法返回值,
     * 如果方法抛出异常,那么 result 为该异常。
     */
    private Object result;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getParamTypes() {
        return paramTypes;
    }

    public void setParamTypes(Class<?>[] paramTypes) {
        this.paramTypes = paramTypes;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

}
