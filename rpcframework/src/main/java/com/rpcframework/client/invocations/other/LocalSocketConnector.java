package com.rpcframework.client.invocations.other;

import android.net.LocalSocket;
import android.net.LocalSocketAddress;

import com.rpcframework.pack.CallSerial;
import com.rpcframework.pack.ReturnSerial;
import com.rpcframework.server.socket.LocalSocketSvrConnector;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.CountDownLatch;

/**
 * todo 实现非oneshot
 */
class LocalSocketConnector implements IClientConnector {
    public static final boolean ONESHOT_MODE = true; //只干一次。不做循环。每次invoke都是一次重新的链接
    private LocalSocketClientThread thread;

    @Override
    public boolean isConnected() {
        return thread != null && thread.mIsConnect;
    }

    @Override
    public void connect() {

    }

    @Override
    public ReturnSerial sendCall(CallSerial call) {
        final CountDownLatch latch = new CountDownLatch(1);
        ReturnSerial r = new ReturnSerial();
        thread = new LocalSocketClientThread(call, latch, r);
        thread.start();
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return r;
    }

    @Override
    public void disconnect() {

    }

    private static class LocalSocketClientThread extends Thread {
        LocalSocketClientThread(CallSerial c, CountDownLatch latch, ReturnSerial r) {
            call = c;
            this.latch = latch;
            this.response = r;
        }

        private final ReturnSerial response;
        private final CountDownLatch latch;
        private final CallSerial call;

        private static final String TAG = LocalSocketHandler.class.getSimpleName();
        private ObjectInputStream mIn;
        private ObjectOutputStream mOut;
        private LocalSocket mSocket;

        private boolean mIsConnect = false;

        private void init() {
            mSocket = new LocalSocket();
            //设置连接地址
            LocalSocketAddress address = new LocalSocketAddress(LocalSocketSvrConnector.LOCAL_SOCKET_SERVER_NAME,
                    LocalSocketAddress.Namespace.RESERVED);
            //建立连接
            try {
                mSocket.connect(address);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //获取数据输入流 可以读数据
            try {
                mIn = new ObjectInputStream(mSocket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
                mIn = null;
            }
            //获取数据输出流 可以写数据
            try {
                mOut = new ObjectOutputStream(mSocket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
                mOut = null;
            }

            mIsConnect = true;
        }

        private void unInit() {
            mIsConnect = false;

            if (mOut != null) {
                try {
                    mOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (mIn != null) {
                try {
                    mIn.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            try {
                mSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            if (ONESHOT_MODE) {
                init();

                try {
                    mOut.writeObject(call);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    response.setResult(mIn.readObject());
                } catch (ClassNotFoundException | IOException e) {
                    e.printStackTrace();
                    response.setErrorCode(-1);
                    response.setException(e.getMessage());
                }

                latch.countDown();
                unInit();
            }
        }
    }
}
