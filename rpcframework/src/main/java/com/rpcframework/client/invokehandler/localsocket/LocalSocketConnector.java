package com.rpcframework.client.invokehandler.localsocket;

import android.net.LocalSocket;
import android.net.LocalSocketAddress;

import com.rpcframework.Call;
import com.rpcframework.client.IClientConnector;
import com.rpcframework.server.socket.LocalSocketSvrConnector;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 * todo 实现非oneshot
 */
public class LocalSocketConnector implements IClientConnector {
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
    public void sendCall(Call call) {
        thread = new LocalSocketClientThread(call);
        thread.start();
    }

    @Override
    public void disconnect() {

    }

    private static class LocalSocketClientThread extends Thread {
        LocalSocketClientThread(Call c) {
            call = c;
        }

        private final Call call;

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

                unInit();
            }
        }
    }
}
