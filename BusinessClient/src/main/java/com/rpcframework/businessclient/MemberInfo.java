package com.rpcframework.businessclient;

import com.rpcframework.annotation.ServerInterfaceClassName;

import java.io.Serializable;

@ServerInterfaceClassName("com.rpcframework.businessserver.business.MemberBean")
public class MemberInfo implements Serializable {
    private static final long serialVersionUID = 563047496720237561L;
    public String accountName;
    public String token;
    public String id;
}
