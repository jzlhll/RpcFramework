package com.rpcframework;

import com.rpcframework.client.invokehandler.inprocess.InnerProcessUnknownClassHandler;

import java.io.Serializable;

/**
 * 同进程的模式下，是用不上这个的。
 * 1. 同进程，同接口业务类，InnerProcessHandler直接使用return type即可；
 * 2. 同进程，不同接口业务类，在{@link InnerProcessUnknownClassHandler}中sendCall，
 *      就可以进行类型转换（虽然是通过将原Object变成jsonStr，再行转换而成）。
 */
public class RpcReturnVal implements Serializable {
    private static final long serialVersionUID = -22008413379294L;

    public RpcReturnVal(){}

    public RpcReturnVal(Object result) {
        this.result = result;
    }

    public RpcReturnVal(String exception, int errorCode) {
        this.exception = exception;
        this.errorCode = errorCode;
    }

    /**
     * 返回值的类名。
     */
    private String returnType;

    /**
     * 表示方法的执行结果 如果方法正常执行,则 result 为方法返回值,
     * 如果方法抛出异常,那么 result 为该异常。
     */
    private Object result;

    /**
     * -1 svr error；-2 class error; -3 other...
     */
    private int errorCode;
    /**
     * 表示出错的结果。不出错没有。
     */
    private String exception;

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

    public void copyFrom(RpcReturnVal r) {
        this.errorCode = r.errorCode;
        this.exception = r.exception;
        this.result = r.result;
    }
}
