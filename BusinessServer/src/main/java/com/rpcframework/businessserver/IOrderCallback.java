package com.rpcframework.businessserver;

import com.rpcframework.common.IInProcessClientCallback;

public interface IOrderCallback extends IInProcessClientCallback {
    void onOrder(ReturnBean bean, String name);
}
