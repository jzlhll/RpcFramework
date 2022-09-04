package com.rpcframework.businessserver;

import com.rpcframework.businessserver.business.IOrder;

import java.util.ArrayList;
import java.util.List;

public class OrderImpl implements IOrder {

    private final List<String> orderList = new ArrayList<>();
    @Override
    public String createOrder() {
        String s = "orderID:" + Math.random() * 100;
        orderList.add(s);
        System.out.println("service create add: " + s);
        return s;
    }
}
