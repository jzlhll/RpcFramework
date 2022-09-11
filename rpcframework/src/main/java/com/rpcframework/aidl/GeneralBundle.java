package com.rpcframework.aidl;

import android.os.Bundle;

public final class GeneralBundle {
    public static final String RETURN_TYPE = "t";
    public static final String RETURN_VALUE = "v";
    public static final String ERROR_CODE = "c";
    public static final String EXCEPTION = "e";
    public static final String HASH_CODE = "h";

    private String returnType; //返回值的类名。
    //private Object result; //表示方法的执行结果 如果方法正常执行,则 result 为方法返回值,如果方法抛出异常,那么 result 为该异常。
    private int errorCode; //0表示正常。 -1 svr error；-2 class error; -3 other...
    private String exception; //表示出错的结果。不出错没有。
    private int hash; //表示从发送端发送的这个值的的hash。

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public int getHash() {
        return hash;
    }

    public void setHash(int hash) {
        this.hash = hash;
    }

    public Bundle toBundle() {
        Bundle bundle = new Bundle();
        bundle.putInt(HASH_CODE, hash);
        bundle.putString(RETURN_TYPE, returnType);
        bundle.putString(EXCEPTION, exception);
        bundle.putInt(ERROR_CODE, errorCode);
        return bundle;
    }

    public static final class Builder {
        private String returnType;
        private int errorCode;
        private String exception;
        private int hash;

        private Builder() {
        }

        public static Builder create() {
            return new Builder();
        }

        public Builder addReturnType(String returnType) {
            this.returnType = returnType;
            return this;
        }

        public Builder addErrorCode(int errorCode) {
            this.errorCode = errorCode;
            return this;
        }

        public Builder addException(String exception) {
            this.exception = exception;
            return this;
        }

        public Builder addHash(int hash) {
            this.hash = hash;
            return this;
        }

        public GeneralBundle build() {
            GeneralBundle bundleCreator = new GeneralBundle();
            bundleCreator.setReturnType(returnType);
            bundleCreator.setErrorCode(errorCode);
            bundleCreator.setException(exception);
            bundleCreator.setHash(hash);
            return bundleCreator;
        }
    }
}