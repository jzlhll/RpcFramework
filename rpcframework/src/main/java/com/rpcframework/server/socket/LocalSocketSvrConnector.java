package com.rpcframework.server.socket;

import android.net.LocalServerSocket;
import android.net.LocalSocket;
import android.util.Log;

import com.rpcframework.server.ISvrConnector;

import java.io.IOException;
import java.io.InputStream;

public class LocalSocketSvrConnector implements ISvrConnector {
    private static final String TAG = "LocalSocketConnector";
    public static final String LOCAL_SOCKET_SERVER_NAME = "rpc_framework_local_socket";

    private ServerSocketThread serverSocketThread;

    /* 内部类begin */
    private static class ServerSocketThread extends Thread {
        private volatile boolean keepRunning = true;
        private LocalServerSocket serverSocket;

        private void stopRun() {
            keepRunning = false;
            if (serverSocket != null) {
                _stopSocket();
            }
        }

        @Override
        public void run() {
            try {
                serverSocket = new LocalServerSocket(LOCAL_SOCKET_SERVER_NAME);
            } catch (IOException e) {
                e.printStackTrace();
                keepRunning = false;
            }

            while (keepRunning) {
                Log.d(TAG, "wait for new client coming !");

                try {
                    LocalSocket interactClientSocket = serverSocket.accept();
                    //由于accept()在阻塞时，所以再次检查keepRunning
                    if (keepRunning) {
                        Log.d(TAG, "new client coming !");

                        new InteractClientSocketThread(interactClientSocket).start();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    keepRunning = false;
                }
            }

            _stopSocket();
        }

        private void _stopSocket() {
            LocalServerSocket lss = serverSocket;
            if (lss != null) {
                try {
                    lss.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            serverSocket = null;
        }
    }

    private static class InteractClientSocketThread extends Thread {
        private final LocalSocket interactClientSocket;

        public InteractClientSocketThread(LocalSocket interactClientSocket) {
            this.interactClientSocket = interactClientSocket;
        }

        @Override
        public void run() {
            StringBuilder recvStrBuilder = new StringBuilder();
            InputStream inputStream = null;
            try {
                inputStream = interactClientSocket.getInputStream();
                byte[] buf = new byte[4096];
                int readBytes;
                while ((readBytes = inputStream.read(buf)) != -1) {
                    String tempStr = new String(buf, 0, readBytes);
                    recvStrBuilder.append(tempStr);
                }
            } catch (IOException e) {
                e.printStackTrace();

                Log.d(TAG, "resolve data error !");
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (interactClientSocket != null) {
                    try {
                        interactClientSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    /* 内部类end */

    @Override
    public void prepare() {
        if (serverSocketThread == null || !serverSocketThread.keepRunning) {
            serverSocketThread = new ServerSocketThread();
            serverSocketThread.start();
        }
    }

    @Override
    public void shutdown() {
        ServerSocketThread s = serverSocketThread;
        if (s != null) {
            s.stopRun();
        }

        serverSocketThread = null;
    }
}
