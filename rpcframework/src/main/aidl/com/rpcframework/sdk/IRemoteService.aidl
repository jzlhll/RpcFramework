// IRemoteService.aidl
package com.rpcframework.sdk;
import android.os.Bundle;
import com.rpcframework.sdk.IRemoteCallback;

interface IRemoteService {
    Bundle send(in Bundle bundle);
    boolean registerCallback(IRemoteCallback callback);
    boolean unRegisterCallback(IRemoteCallback callback);
}