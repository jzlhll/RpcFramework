package com.rpcframework.sdk;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用来注解在客户端接口上。填写上服务器对应的添加到ServerManager中的key的接口全路径名
 * 具体逻辑参考{@link com.rpcframework.sdk.server.ServerManager#get(Class)}
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ServerInterfaceClassName {
    String value();
}
