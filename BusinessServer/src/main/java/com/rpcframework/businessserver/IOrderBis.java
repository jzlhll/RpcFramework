package com.rpcframework.businessserver;

import com.rpcframework.server.IHasCallbackBis;

public interface IOrderBis extends IHasCallbackBis {
    ReturnBean markAOrder(InInfo info, String name);
}