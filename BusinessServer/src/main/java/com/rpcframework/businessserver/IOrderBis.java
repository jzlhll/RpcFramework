package com.rpcframework.businessserver;

import com.rpcframework.server.IBusiness;

public interface IOrderBis extends IBusiness {
    ReturnBean markAOrder(InInfo info, String name);
}