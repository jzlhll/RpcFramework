package com.rpcframework.businessserver;

import com.rpcframework.businessserver.business.IMember;
import com.rpcframework.businessserver.business.IOrder;
import com.rpcframework.sdk.server.ServerManager;

public class ServiceBusinessManager extends ServerManager {

    public void init() {
        put(IOrder.class, new OrderImpl());
        put(IMember.class, new MemberImpl());
    }
}
