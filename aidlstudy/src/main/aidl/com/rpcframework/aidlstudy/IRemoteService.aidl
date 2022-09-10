// IRemoteService.aidl
package com.rpcframework.aidlstudy;
import android.os.Bundle;
import com.rpcframework.aidlstudy.IRemoteCallback;
// Declare any non-default types here with import statements

interface IRemoteService {
    Bundle send(in Bundle bundle);
    boolean registerCallback(IRemoteCallback callback);
    boolean unRegisterCallback(IRemoteCallback callback);
}