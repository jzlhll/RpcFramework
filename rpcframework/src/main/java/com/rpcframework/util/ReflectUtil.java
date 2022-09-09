package com.rpcframework.util;

import com.rpcframework.ICallback;
import com.rpcframework.pack.MethodParcel;
import com.rpcframework.annotation.MappingSameClassAnnotation;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReflectUtil {
    public static Class<?> classForName(String clsName) {
        try {
            return Class.forName(clsName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String clientClassToSvrClassName(Class<?> clazz) {
        MappingSameClassAnnotation annotation = clazz.getAnnotation(MappingSameClassAnnotation.class);
        String clsName;
        if (annotation != null) { //如果有注解，我们就使用注解
            clsName = annotation.value();
        } else { //没有注解，我们则认为跟远端同接口。比较适用于不同app
            clsName = clazz.getName();
        }
        return clsName;
    }

    public static Class<?> clientClassToSvrClass(Class<?> clazz) {
        return classForName(clientClassToSvrClassName(clazz));
    }

    public static List<MethodParcel> classToMethodParcelList(Class<?> clazz) {
        if (!clazz.isAssignableFrom(ICallback.class)) {
            throw new RuntimeException(" pack up >" + clazz + "< is not assign from ICallback.class");
        }

        Method[] methods = clazz.getMethods();
        List<MethodParcel> methodParcels = new ArrayList<>(methods.length);
        //todo 验证，应该没有Object的方法吧。
        for (Method method : methods) {
            MethodParcel methodParcel = new MethodParcel();
            methodParcel.setReturnType(method.getReturnType().getName());
            methodParcel.setMethodName(method.getName());

            Class<?>[] classes = method.getParameterTypes();
            for (Class<?> cls : classes) {
                methodParcel.addParamPair(cls.getName());
            }
            methodParcels.add(methodParcel);
        }
        return methodParcels;
    }

    public static HashMap<String, List<MethodParcel>> classToMethodParcelMap(Class<?> clazz) {
        List<MethodParcel> methodParcels = classToMethodParcelList(clazz);
        HashMap<String, List<MethodParcel>> map = new HashMap<>();
        for (MethodParcel mp : methodParcels) {
            if (map.containsKey(mp.getMethodName())) {
                map.get(mp.getMethodName()).add(mp);
            } else {
                List<MethodParcel> list = new ArrayList<>(1);
                list.add(mp);
                map.put(mp.getMethodName(), list);
            }
        }

        return map;
    }
}
