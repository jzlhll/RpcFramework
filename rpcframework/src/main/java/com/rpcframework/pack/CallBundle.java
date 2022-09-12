package com.rpcframework.pack;

import android.os.Bundle;
import android.os.Parcelable;

import com.rpcframework.annotation.MappingSameClassAnnotation;
import com.rpcframework.util.GsonConvertor;

import java.io.Serializable;

/**
 * 封装如下参数
 //private String className; // 调用的类名或接口名
 //private String methodName; // 调用的方法名
 //private Class<?>[] paramTypes; // 方法参数类型
 //private Object[] params; // 调用方法时传入的参数值
 */
public class CallBundle {
    public static final String KEY_CLASS_NAME = "c";
    public static final String KEY_METHOD_NAME = "m";
    public static final String KEY_PARAMS_NUM = "n";
    public static final String KEY_PARAMS_PREFIX = "p";
    public static final String KEY_ARGS_PREFIX = "a";

    private final Bundle bundle = new Bundle();
    public CallBundle addClassName(String className) {
        bundle.putString(KEY_CLASS_NAME, className);
        return this;
    }

    public CallBundle addMethodName(String methodName) {
        bundle.putString(KEY_METHOD_NAME, methodName);
        return this;
    }

    /**
     * 将参数列表，转变成clasNameList传进去。
     * todo：需要测试的列表有，registerListener ibinder对象。
     * int[] array;
     * parcelable，Serializable
     * List<Integer>,
     * List<XX>
     * @param paramTypes
     * @return
     */
    public CallBundle addParamTypes(Class<?>[] paramTypes, Object[] args) {
        bundle.putInt(KEY_PARAMS_NUM, paramTypes.length);

        int i = 0;
        for (Class<?> paramType : paramTypes) {
            //parcelable就直接传递。类名也直接传递。
            if (paramType.isAssignableFrom(Parcelable.class)) {
                bundle.putString(KEY_PARAMS_PREFIX + i, paramType.getName());
                bundle.putParcelable(KEY_ARGS_PREFIX + i, (Parcelable) args[i]);
            } else if (paramType.isAssignableFrom(Serializable.class)) {
                bundle.putString(KEY_PARAMS_PREFIX + i, paramType.getName());
                bundle.putSerializable(KEY_ARGS_PREFIX + i, (Serializable) args[i]);
            } else {
                //证明是参数
                //如果有标注注解；就是服务端的自定义对象。我们就要把参数转变过去
                MappingSameClassAnnotation an = paramType.getAnnotation(MappingSameClassAnnotation.class);
                if (an != null && an.value() != null) {
                    bundle.putString(KEY_PARAMS_PREFIX + i, an.value());
                    bundle.putString(KEY_ARGS_PREFIX + i, GsonConvertor.sGson.toJson(args[i]));
                } else if (paramType.isArray()) {
                    bundle.putString(KEY_PARAMS_PREFIX + i, paramType.getName());//todo 验证数组
                    bundle.putString(KEY_ARGS_PREFIX + i, GsonConvertor.sGson.toJson(args[i]));
                } else {
                    bundle.putSerializable(KEY_PARAMS_PREFIX + i, paramType.getName());
                    bundle.putSerializable(KEY_ARGS_PREFIX + i, args[i].toString());
                }
            }

            i++;
        }
        return this;
    }

    public Bundle get() {
        return bundle;
    }
}
