package com.rpcframework;

import com.rpcframework.client.invocations.process.InnerProcessUnknownClassHandler;

import java.io.Serializable;

/**
 * 同进程的模式下，是用不上这个的。
 * 1. 同进程，同接口业务类，InnerProcessHandler直接使用return type即可；
 * 2. 同进程，不同接口业务类，在{@link InnerProcessUnknownClassHandler}中sendCall，
 *      就可以进行类型转换（虽然是通过将原Object变成jsonStr，再行转换而成）。
 */
public final class ReturnParcel implements Serializable {
    private static final long serialVersionUID = -22008413379294L;

    private String returnType; //返回值的类名。
    private Object result; //表示方法的执行结果 如果方法正常执行,则 result 为方法返回值,如果方法抛出异常,那么 result 为该异常。
    private int errorCode; //-1 svr error；-2 class error; -3 other...
    private String exception; //表示出错的结果。不出错没有。
    private int hash; //表示从发送端发送的这个值的的hash。

    public ReturnParcel(){}

    public ReturnParcel(Object result) {
        this.result = result;
    }

    public ReturnParcel(String exception, int errorCode) {
        this.exception = exception;
        this.errorCode = errorCode;
    }

    /////////////////set; get /////////////////

    public int getHash() {
        return hash;
    }

    public void setHash(int hash) {
        this.hash = hash;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int resultCode) {
        this.errorCode = resultCode;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public void copyFrom(ReturnParcel r) {
        this.errorCode = r.errorCode;
        this.exception = r.exception;
        this.result = r.result;
    }
}
