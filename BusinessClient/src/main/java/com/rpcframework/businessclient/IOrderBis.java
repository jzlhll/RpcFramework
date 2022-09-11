package com.rpcframework.businessclient;

import com.rpcframework.annotation.MappingSameClassAnnotation;
import com.rpcframework.server.IBusiness;

@MappingSameClassAnnotation("com.rpcframework.businessserver.IOrderBis")
public interface IOrderBis extends IBusiness {
    ReturnBean markAOrder(InInfo info, String name);
}