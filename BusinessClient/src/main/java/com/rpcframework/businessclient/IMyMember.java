package com.rpcframework.businessclient;

import com.rpcframework.sdk.ServerInterfaceClassName;

@ServerInterfaceClassName("com.rpcframework.businessserver.business.IMember")
public interface IMyMember {
    MemberInfo createAccount(String name, String password);
}