package com.rpcframework.businessserver;

import android.util.Log;

import com.rpcframework.businessserver.business.IOrder;

import java.util.ArrayList;
import java.util.List;

public class OrderImpl implements IOrder {

    private final List<String> orderList = new ArrayList<>();
    @Override
    public String createOrder() {
        String s = "orderID:" + Math.random() * 100;
        orderList.add(s);
        Log.w("allan", "service create add: " + s);
        return s;
    }
}
