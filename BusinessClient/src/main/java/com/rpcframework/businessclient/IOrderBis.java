package com.rpcframework.businessclient;

import com.rpcframework.annotation.MappingSameClassAnnotation;
import com.rpcframework.server.IHasCallbackBis;

@MappingSameClassAnnotation("com.rpcframework.businessserver.IOrderBis")
public interface IOrderBis extends IHasCallbackBis {
    ReturnBean markAOrder(InInfo info, String name);
}