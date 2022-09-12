package com.rpcframework.pack;

import android.os.Bundle;
import android.os.Parcelable;

import com.rpcframework.util.GsonConvertor;

import java.io.Serializable;

public class ServerReturnBundle {
    private static final String CLASS_TYPE_KEY = "c";
    private static final String VALUE_KEY = "v";

    public static ReturnSerial parse(Bundle serverReturnBundle) {
        String k = serverReturnBundle.getString(CLASS_TYPE_KEY);
        Object v = serverReturnBundle.get(VALUE_KEY);
        if (v instanceof Parcelable) {
            return new ReturnSerial(v, k);
        }
        if (v instanceof Serializable) {
            return new ReturnSerial(v, k);
        }
        //todo array or other
        return new ReturnSerial(v, k); //json转换
    }

    public static Bundle addReturnType(Class<?> returnType, Object value) {
        Bundle bundle = new Bundle();
        if (returnType.isAssignableFrom(Parcelable.class)) {
            bundle.putString(CLASS_TYPE_KEY, returnType.getName());
            bundle.putParcelable(VALUE_KEY, (Parcelable) value);
        } else if (returnType.isAssignableFrom(Serializable.class)) {
            bundle.putString(CLASS_TYPE_KEY, returnType.getName());
            bundle.putSerializable(VALUE_KEY, (Serializable) value);
        } else {
            //证明是参数
            //如果有标注注解；就是服务端的自定义对象。我们就要把参数转变过去
            if (returnType.isArray()) {
                bundle.putString(CLASS_TYPE_KEY, returnType.getName());//todo 验证数组
                bundle.putString(VALUE_KEY, GsonConvertor.sGson.toJson(value));
            } else {
                bundle.putString(CLASS_TYPE_KEY, returnType.getName());
                bundle.putSerializable(VALUE_KEY, value.toString());
            }
        }
        return bundle;
    }
}
