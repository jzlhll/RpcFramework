package com.rpcframework.pack;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 单个函数，模拟封装。
 */
public class MethodSerial implements Serializable{
    private static final long serialVersionUID = -5466192765067344772L;

    /**
     * 单个参数封装
     */
    public static final class ParamPair implements Serializable {
        private static final long serialVersionUID = 8095987175693324537L;
        private String type;
        private Object value;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    private String methodName;
    private List<ParamPair> paramList;
    private String returnType;

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public List<ParamPair> getParamList() {
        return paramList;
    }

    public void addParamPair(ParamPair pair) {
        if (paramList == null) {
            paramList = new ArrayList<>(2);
        }
        paramList.add(pair);
    }

    public void addParamPair(String type) {
        if (paramList == null) {
            paramList = new ArrayList<>(2);
        }
        ParamPair pair = new ParamPair();
        pair.type = type;
        paramList.add(pair);
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }
}