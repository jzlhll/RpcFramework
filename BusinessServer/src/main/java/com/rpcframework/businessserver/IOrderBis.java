package com.rpcframework.businessserver;

import com.rpcframework.server.IRegisterBis;

public interface IOrderBis extends IRegisterBis {
    ReturnBean markAOrder(InInfo info, String name);
}