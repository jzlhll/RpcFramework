package com.rpcframework.businessserver;

import com.rpcframework.server.ServerManager;

public class ServiceBusinessManager extends ServerManager {

    public void init() {
        put(IOrder.class, new IOrder() {
            @Override
            public void register(String name, String password, IOrderCallback callback) {
                new Thread(()->{
                    ReturnBean b = new ReturnBean();
                    b.r = true;
                    b.s = "test fdafdfadfd";
                    callback.onOrder(b, name);
                }).start();
            }

            @Override
            public ReturnBean canCreate(InInfo info) {
                ReturnBean b = new ReturnBean();
                b.r = true;
                b.s = "return test 182412";
                return b;
            }
        });
    }
}
