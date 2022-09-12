package com.rpcframework.client;

import com.rpcframework.aidl.IConnMgr;
import com.rpcframework.aidl.IConnMgrServiceChanged;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class ConnAdapter implements IClientMgr {
    private final IConnMgr connMgr;

    public IConnMgr getConnMgr() {
        return connMgr;
    }

    private final Set<WeakReference<IConnMgrServiceChanged>> changes = new CopyOnWriteArraySet<>();

    public ConnAdapter(IConnMgr mgr) {
        connMgr = mgr;
        //adpater和mgr是1对1的。因此就设置了一次。
        mgr.setMgrServiceChanged(bindState -> {
            WeakReference<IConnMgrServiceChanged> cleared = null;
            for (WeakReference<IConnMgrServiceChanged> weak : changes) {
                IConnMgrServiceChanged c = weak.get();
                if (c != null) {
                    c.onServiceChanged(bindState);
                } else {
                    cleared = weak;
                }
            }
            changes.remove(cleared); //later: 每次干活，我们就清理一次好了。一般来讲，我们的注册就是一个。就这样可以。
        });
    }

    @Override
    public boolean isAlive() {
        return connMgr.isAlive();
    }

    @Override
    public void connect() {
        if (!isAlive()) {
            connMgr.bind();
        }
    }

    @Override
    public void disConnect() {
        connMgr.destroy();
    }

    @Override
    public void addMgrServiceChanged(IConnMgrServiceChanged changed) {
        boolean curAlive = isAlive();
        changes.add(new WeakReference<>(changed));
        if (curAlive) { //如果当前是链接好了的，我们立刻通报一次。
            changed.onServiceChanged(true);
        }
    }

    @Override
    public void removeMgrServiceChanged(IConnMgrServiceChanged changed) {
        WeakReference<IConnMgrServiceChanged> target = null;
        List<WeakReference<IConnMgrServiceChanged>> clearedList = new ArrayList<>();
        for (WeakReference<IConnMgrServiceChanged> weak : changes) {
            IConnMgrServiceChanged c = weak.get();
            if (c != null && c == changed) {
                target = weak;
            }

            if (c == null) {
                clearedList.add(weak);
            }
        }
        if (target != null) changes.remove(target);
        for (WeakReference<IConnMgrServiceChanged> clear : clearedList) {
            changes.remove(clear);
        }
    }
}
