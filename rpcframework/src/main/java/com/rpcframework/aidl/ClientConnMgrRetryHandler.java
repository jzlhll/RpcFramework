package com.rpcframework.aidl;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import com.rpcframework.snip.IAction;
import com.rpcframework.snip.IAction1;
import com.rpcframework.util.RpcLog;

final class ClientConnMgrRetryHandler extends Handler {
    private static final String TAG = ClientConnMgr.TAG + "Retry";

    private final IAction mBind, mUnBind;
    private IAction1 mStopper, mTimeout;

    public void setStopper(IAction1 stopper) {
        mStopper = stopper;
    }
    public void setTimeout(IAction1 timeout) {
        mTimeout = timeout;
    }

    private boolean mBindState;
    private boolean mGotoDestroy;
    /**
     * 通过Success来回调成功。
     */
    public ClientConnMgrRetryHandler(IAction bind, IAction unbind) {
        super(Looper.getMainLooper());
        mBind = bind;
        mUnBind = unbind;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        switch (msg.what) {
            case MSG_UNBIND:
            {
                //RpcLog.d(TAG, "Handler: unbind...");
                currentDelay = DELAY_TIME_MIN;
                mBindState = false;
                mUnBind.onAction();
            }
            break;
            case MSG_RETRY_BIND:
            {
                //RpcLog.d(TAG, "Handler: retry..");
                if (calNextRetryDelayTime()) {
                    mBind.onAction();
                    sendMessageDelayed(obtainMessage(MSG_RETRY_BIND), currentDelay * 1000L);
                } else {
                    //超过限制。直接停止不再重连。
                    RpcLog.d(TAG, "timeout retry!");
                    currentDelay = DELAY_TIME_MIN;
                    if(mTimeout != null) mTimeout.onAction(mBindState);
                }
            }
                break;
            case MSG_RETRY_CANCEL:
            {
                //RpcLog.d(TAG, "Handler: retry cancel end.");
                currentDelay = DELAY_TIME_MIN;
                mBindState = msg.arg1 == 1;
                if(mStopper != null) mStopper.onAction(mBindState);
            }
                break;
        }
    }

    //所有的Retry都共用一个。
    private static final int MSG_RETRY_BIND = 1;
    private static final int MSG_UNBIND = 2;
    private static final int MSG_RETRY_CANCEL = 3;

    private static final int DELAY_TIME_MAX = 120;
    private static final int DELAY_TIME_MIN = 12;
    private static final int DELAY_NEXT_DELTA = 12;

    private static final int DELAY_TIME_AFTER_DIED = 20;

    private int currentDelay;
    private boolean mHasTimeout = true;
    //传递false标识永远不断尝试链接。不做停止。
    public void setHasTimeout(boolean hasTimeout) {
        mHasTimeout = hasTimeout;
    }

    /**
     *
     */
    private boolean calNextRetryDelayTime() {
        if (mHasTimeout && currentDelay >= DELAY_TIME_MAX) {
            return false;
        }
        currentDelay += DELAY_NEXT_DELTA;
        if (currentDelay >= DELAY_TIME_MAX) {
            currentDelay = DELAY_TIME_MAX;
        }
        return true;
    }

    void startRetry() {
        if (mGotoDestroy) {
            return;
        }
        //只能移除自己；不能移除解绑和停止消息。
        removeMessages(MSG_RETRY_BIND);
        currentDelay = DELAY_TIME_MIN;
        //直接开始
        RpcLog.d(TAG, "...startRetry...");
        sendMessageDelayed(obtainMessage(MSG_RETRY_BIND), 250L);
    }

    void startRetryWithDelay() {
        if (mGotoDestroy) {
            return;
        }
        //只能移除自己；不能移除解绑和停止消息。
        currentDelay = DELAY_TIME_MIN;
        //直接开始
        RpcLog.d(TAG, "...startRetryWithDelay:");
        //可以移除任何消息。
        removeMessages(MSG_RETRY_BIND);
        removeMessages(MSG_RETRY_CANCEL);
        removeMessages(MSG_UNBIND);
        RpcLog.d(TAG, "...unbind...");
        sendMessage(obtainMessage(MSG_UNBIND));
        sendMessageDelayed(obtainMessage(MSG_RETRY_BIND), DELAY_TIME_AFTER_DIED * 1000L);
    }

    void stopRetry(boolean bindState) {
        if (mGotoDestroy) {
            return;
        }
        //只移除retry逻辑
        removeMessages(MSG_RETRY_BIND);
        RpcLog.d(TAG, "...stopRetry...");
        sendMessage(obtainMessage(MSG_RETRY_CANCEL, bindState ? 1 : 0, 0));
    }

    void unbind() {
        if (mGotoDestroy) {
            return;
        }
        //可以移除任何消息。
        removeMessages(MSG_RETRY_BIND);
        removeMessages(MSG_RETRY_CANCEL);
        removeMessages(MSG_UNBIND);
        RpcLog.d(TAG, "...unbind...");
        sendMessage(obtainMessage(MSG_UNBIND));
    }

    void destroy() {
        mGotoDestroy = true;
        removeMessages(MSG_RETRY_BIND);
        removeMessages(MSG_RETRY_CANCEL);
        removeMessages(MSG_UNBIND);
        RpcLog.d(TAG, "...destroy...");
        unbind();
    }
}
