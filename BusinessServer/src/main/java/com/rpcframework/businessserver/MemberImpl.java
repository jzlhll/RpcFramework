package com.rpcframework.businessserver;

import android.util.Log;

import com.rpcframework.businessserver.business.IMember;
import com.rpcframework.businessserver.business.MemberBean;
import com.rpcframework.sdk.BeanTest;

import java.util.ArrayList;
import java.util.List;

public class MemberImpl implements IMember {
    List<MemberBean> beans = new ArrayList<>();
    @Override
    public MemberBean createAccount(String name, String password) {
        MemberBean b = new MemberBean();
        b.accountName = name;
        b.id = String.valueOf(Math.random() * 10000);
        b.token = b.id;
        beans.add(b);
        Log.d("allan", "service create a new Member: " + b);
        return b;
    }
}
