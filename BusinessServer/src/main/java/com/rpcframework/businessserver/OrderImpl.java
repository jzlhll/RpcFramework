package com.rpcframework.businessserver;

import com.rpcframework.businessserver.business.IOrder;
import com.rpcframework.businessserver.business.MemberBean;

import java.util.ArrayList;
import java.util.List;

public class OrderImpl implements IOrder {

    private final List<String> orderList = new ArrayList<>();

    @Override
    public boolean createOrder(MemberBean bean) {
        return false;
    }
}
