package com.rpcframework.aidlstudy;

import android.os.Bundle;

public class MyBundle {
    public static final String RETURN_KEY = "k";
    public static final String RETURN_TYPE_CLASS = "r";
    public static final String ERROR_CODE = "c";
    public static final String EXCEPTION = "e";
    public static final String HASH_CODE = "h";

    private final Bundle bundle;
    public Bundle getBundle() {return bundle;}
    public MyBundle() {
        this.bundle = new Bundle();
    }

    public void copyFrom(Bundle bundle) {
        bundle.putAll(bundle);
    }

    public void putReturnClass(Class<?> clazz) {
        bundle.putSerializable(RETURN_TYPE_CLASS, clazz);
    }
}
