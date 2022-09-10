package com.rpcframework.aidlstudy;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import com.rpcframework.IAction;
import com.rpcframework.IAction1;
import com.rpcframework.log.ALog;

final class ClientConnMgrRetry {
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
    public ClientConnMgrRetry(IAction bind, IAction unbind) {
        mBind = bind;
        mUnBind = unbind;
    }

    private static final class RetryHandler extends Handler {
        private final ClientConnMgrRetry retry;
        public RetryHandler(ClientConnMgrRetry retry) {
            super(Looper.getMainLooper());
            this.retry = retry;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case MSG_UNBIND:
                {
                    //ALog.d(TAG, "Handler: unbind...");
                    retry.currentDelay = DELAY_TIME_MIN;
                    retry.mBindState = false;
                    retry.mUnBind.onAction();
                }
                break;
                case MSG_RETRY_BIND:
                {
                    //ALog.d(TAG, "Handler: retry..");
                    if (retry.calNextRetryDelayTime()) {
                        retry.mBind.onAction();
                        retry.sRetryHandler.sendMessageDelayed(obtainMessage(MSG_RETRY_BIND), retry.currentDelay * 1000L);
                    } else {
                        //超过限制。直接停止不再重连。
                        ALog.d(TAG, "timeout retry!");
                        retry.currentDelay = DELAY_TIME_MIN;
                        if(retry.mTimeout != null) retry.mTimeout.onAction(retry.mBindState);
                    }
                }
                    break;
                case MSG_RETRY_CANCEL:
                {
                    //ALog.d(TAG, "Handler: retry cancel end.");
                    retry.currentDelay = DELAY_TIME_MIN;
                    retry.mBindState = msg.arg1 == 1;
                    if(retry.mStopper != null) retry.mStopper.onAction(retry.mBindState);
                }
                    break;
            }
        }
    }

    //所有的Retry都共用一个。
    private final Handler sRetryHandler = new RetryHandler(this);

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
        sRetryHandler.removeMessages(MSG_RETRY_BIND);
        currentDelay = DELAY_TIME_MIN;
        //直接开始
        ALog.d(TAG, "...startRetry...");
        sRetryHandler.sendMessageDelayed(sRetryHandler.obtainMessage(MSG_RETRY_BIND), 250L);
    }

    void startRetryWithDelay() {
        if (mGotoDestroy) {
            return;
        }
        //只能移除自己；不能移除解绑和停止消息。
        currentDelay = DELAY_TIME_MIN;
        //直接开始
        ALog.d(TAG, "...startRetryWithDelay:");
        //可以移除任何消息。
        sRetryHandler.removeMessages(MSG_RETRY_BIND);
        sRetryHandler.removeMessages(MSG_RETRY_CANCEL);
        sRetryHandler.removeMessages(MSG_UNBIND);
        ALog.d(TAG, "...unbind...");
        sRetryHandler.sendMessage(sRetryHandler.obtainMessage(MSG_UNBIND));
        sRetryHandler.sendMessageDelayed(sRetryHandler.obtainMessage(MSG_RETRY_BIND), DELAY_TIME_AFTER_DIED * 1000L);
    }

    void stopRetry(boolean bindState) {
        if (mGotoDestroy) {
            return;
        }
        //只移除retry逻辑
        sRetryHandler.removeMessages(MSG_RETRY_BIND);
        ALog.d(TAG, "...stopRetry...");
        sRetryHandler.sendMessage(sRetryHandler.obtainMessage(MSG_RETRY_CANCEL, bindState ? 1 : 0, 0));
    }

    void unbind() {
        if (mGotoDestroy) {
            return;
        }
        //可以移除任何消息。
        sRetryHandler.removeMessages(MSG_RETRY_BIND);
        sRetryHandler.removeMessages(MSG_RETRY_CANCEL);
        sRetryHandler.removeMessages(MSG_UNBIND);
        ALog.d(TAG, "...unbind...");
        sRetryHandler.sendMessage(sRetryHandler.obtainMessage(MSG_UNBIND));
    }

    void destroy() {
        mGotoDestroy = true;
        ALog.d(TAG, "...destroy...");
        unbind();
    }
}
