package com.rpcframework.pack;

import com.rpcframework.client.invocations.process.InnerProcessUnknownClassHandler;

import java.io.Serializable;
import java.util.Objects;

/**
 * 1. 同进程，同接口业务类，InnerProcessHandler直接使用return type即可, 不需要使用本返回参数。
 * 2. 同进程，不同接口业务类，在{@link InnerProcessUnknownClassHandler}中sendCall，
 *      就可以进行类型转换（虽然是通过将原Object变成jsonStr，再行转换而成）。
 */
public class ReturnSerial implements Serializable {
    private static final long serialVersionUID = -22008413379294L;

    private String returnType; //返回值的类名。
    private Object result; //表示方法的执行结果 如果方法正常执行,则 result 为方法返回值。否则空着。

    private int errorCode; //-1 svr error；-2 class error; -3 server not connected...
    private String exception; //表示出错的结果。不出错没有。

    public ReturnSerial(){}

    public ReturnSerial(Object result, String type) {
        this.result = result;
        this.returnType = type;
    }

    public ReturnSerial(Object result, boolean withReturnType) {
        this.result = result;
        this.returnType = result.getClass().getName();
    }

    public ReturnSerial(String exception, int errorCode) {
        this.exception = exception;
        this.errorCode = errorCode;
    }

    /////////////////static /////
    public static final ReturnSerial ERROR_SVR_NOT_ALIVE = new ReturnSerial("Server not connected", -3);
    public static final ReturnSerial ERROR_UNKNOWN_NULL = new ReturnSerial("unknown null", -4);

    /////////////////set; get /////////////////

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    /**
     * //-1 svr error；-2 class error; -3 other...
     */
    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int resultCode) {
        this.errorCode = resultCode;
    }

    /**
     * 表示方法的执行结果 如果方法正常执行,则 result 为方法返回值,如果方法抛出异常,那么 result 为该异常。
     */
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

    public void copyFrom(ReturnSerial r) {
        this.errorCode = r.errorCode;
        this.exception = r.exception;
        this.result = r.result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReturnSerial that = (ReturnSerial) o;
        return errorCode == that.errorCode && Objects.equals(returnType, that.returnType) && Objects.equals(result, that.result) && Objects.equals(exception, that.exception);
    }

    @Override
    public int hashCode() {
        return Objects.hash(returnType, result, errorCode, exception);
    }
}
