package com.rpcframework.businessclient;

import com.rpcframework.annotation.MappingSameClassAnnotation;
import com.rpcframework.server.IBusiness;

@MappingSameClassAnnotation("com.rpcframework.businessserver.IOrderCallback")
public interface IOrderCallback extends IBusiness.ICallback {
    void callback(InInfo info, String aa);
    void callback2(InInfo info, boolean aa);
}