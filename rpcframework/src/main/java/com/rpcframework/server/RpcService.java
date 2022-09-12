package com.rpcframework.server;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;

import com.rpcframework.aidl.ServerStub;
import com.rpcframework.util.ReflectUtil;
import com.rpcframework.util.RpcLog;
import com.rpcframework.util.Util;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class RpcService extends Service {
    private static final String TAG = "RpcService";
    private ServerStubImpl stub;

    @Override
    public void onCreate() {
        super.onCreate();
        RpcLog.d(TAG, "on create...");
        NotificationUtil.startForeground(this, "a", "channelDesc", "hah", "我们一起点滴");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        NotificationUtil.stopForegound(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        RpcLog.d(TAG, "onBind...");
        if (stub == null) {
            stub = new ServerStubImpl();
        }
        return stub;
    }

    private final class ServerStubImpl extends ServerStub {

        //如果是注册函数则处理一下。
        private boolean doIRegisterBisMethods(Method method, Object[] args, Object svrInstance) {
            String name = method.getName();
            Boolean b = null;
            if ("registerListener".equals(name)) {
                b = Boolean.TRUE;
            } else if ("unRegisterListener".equals(name)) {
                b = Boolean.FALSE;
            }

            if (b == null) {
                return false;
            }

            if (!(svrInstance instanceof IBusiness)) {
                RpcLog.d("doIRegisterBisMethods svrInstance is not an IRegisterBis");
                return false;
            }

            Class<?>[] paramTypes = method.getParameterTypes();
            if (paramTypes.length != 1) {
                throw new RuntimeException("Error01 param of " + method.getDeclaringClass() + ", " + name);
            }
            //todo 改成泛型，减少一个注解
            Class<?> paramType = ((IBusiness) svrInstance).getCallbackClass();
            IBusiness svrInstanceBis = (IBusiness) svrInstance;

            //证明是一个回调接口
            //是回调接口类型，就不能如上直接转了，需要构建出服务端的callback并代理上客户端的代码。
            //思考后：这里都需要将对象
            // 同进程：直接将接口传递过去，虽然服务端无法直接获取，但可以在服务端可以基于这个对象getMethod列表；
            // 跨进程 todo：这个对象需要是aidl对象或者转变为list<函数名+参数类型+返回值类型>传递。后续基于exchanger交换。
            // 跨进程实现的时候，转变为list<函数名+参数类型+返回值类型>
            //todo 这里就要求客户端的回调ICallback接口设计，不能有非bean，非基础类型的参数。

            //得到服务端接口类 todo 加速
            Class<?> svrCallbackClass = ReflectUtil.clientClassToSvrClass(paramType);
            //再创建代理
            IBusiness.ICallback o = (IBusiness.ICallback) Proxy.newProxyInstance(IBusiness.ICallback.class.getClassLoader(),
                    new Class[] {svrCallbackClass},
                    new ClientCallbackHandler(args[0]));
            boolean r = svrInstanceBis.addListener(o);
            Log.d("allan", "r " + r);
            return true;
        }

        @Override
        public Bundle send(Bundle bundle) throws RemoteException {
            RpcLog.d(TAG, "send.....bundle!!!");
            Util.bundlePrint(TAG, bundle);
            return null;
        }
    }
}
