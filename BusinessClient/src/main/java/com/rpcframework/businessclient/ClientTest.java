package com.rpcframework.businessclient;

import android.content.Context;
import android.util.Log;

import com.rpcframework.ClientSDK;
import com.rpcframework.client.IClientMgr;

public class ClientTest {
    private static final String TAG = "ClientTest";

    public void test() {
        IOrderBis order = ClientSDK.getProxyInProcess(IOrderBis.class, ClientSDK.TYPE_INNER_PROCESS_NOT_SAME_CLASS);
        order.addListener(new IOrderCallback() {
            @Override
            public void callback(InInfo info, String aa) {
                Log.d(TAG, "callback info: " + info.info + ", " + info.info2 + ", aa " + aa);
            }

            @Override
            public void callback2(InInfo info, boolean aa) {
                Log.d(TAG, "callback2 info: " + info.info + ", " + info.info2 + ", aa " + aa);
            }
        });

        InInfo info = new InInfo();
        info.info = "clientTest";
        ReturnBean returnBean = order.markAOrder(info, "client007");
        Log.d(TAG, "return bean " + returnBean.r + ", " + returnBean.s);
    }

    private IClientMgr mgr;
    private boolean mBinderState = false;
    private IOrderBis orderBis;

    /**
     * Activitiy or service
     */
    public void testAidl(Context context) {
        IClientMgr[] mgrs = new IClientMgr[1];
        orderBis = ClientSDK.getProxyAidl(context, IOrderBis.class, "", "", mgrs);
        this.mgr = mgrs[0];
        mgr.addMgrServiceChanged(bindState -> {
            mBinderState = bindState;
            if (bindState) {
                InInfo info = new InInfo();
                info.info = "clientTest";
                ReturnBean returnBean = orderBis.markAOrder(info, "client007");
                Log.d(TAG, "return bean " + returnBean.r + ", " + returnBean.s);
            }
        });
        mgr.connect();
    }
}
