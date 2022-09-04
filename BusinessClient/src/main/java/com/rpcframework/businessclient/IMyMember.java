package com.rpcframework.businessclient;

import com.rpcframework.annotation.ServerInterfaceClassName;

@ServerInterfaceClassName("com.rpcframework.businessserver.business.IMember")
public interface IMyMember {
    MemberInfo createAccount(String name, String password);
}