package com.rpcframework.businessserver.business;

import com.rpcframework.sdk.BeanTest;

public interface IMember {
    MemberBean createAccount(String name, String password);
}
