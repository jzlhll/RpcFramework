package com.rpcframework;

import java.io.Serializable;

public class Response implements Serializable {
    private static final long serialVersionUID = -22008413379294L;

    public Response(){}

    public Response(Object result) {
        this.result = result;
    }

    public Response(String exception, int errorCode) {
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

    public void copyFrom(Response r) {
        this.errorCode = r.errorCode;
        this.exception = r.exception;
        this.result = r.result;
    }
}
