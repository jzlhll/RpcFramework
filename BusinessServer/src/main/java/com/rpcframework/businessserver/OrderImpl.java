package com.rpcframework.businessserver;

import com.rpcframework.businessserver.business.IOrder;
import com.rpcframework.businessserver.business.MemberBean;
import com.rpcframework.businessserver.business.OrderBean;

import java.util.ArrayList;
import java.util.List;

public class OrderImpl implements IOrder {

    private final List<String> orderList = new ArrayList<>();

    @Override
    public OrderBean createOrder(MemberBean bean) {
        OrderBean d = new OrderBean();
        d.order = "111";
        return d;
    }
}
