package com.rpcframework.businessserver.business;

import com.rpcframework.business.IHasCbBusiness;

public interface INewOrder extends IHasCbBusiness {
    void createOrder();
}