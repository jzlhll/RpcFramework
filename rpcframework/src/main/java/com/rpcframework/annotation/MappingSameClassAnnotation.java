package com.rpcframework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用来注解在客户端业务接口上。
 * 也可以不用注解，保持类名和package完全相同即可，这就要求跨APP或跨服务器。
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MappingSameClassAnnotation {
    String value();
}
