// IRemoteCallback.aidl
package com.rpcframework.aidlstudy;
import android.os.Bundle;
// Declare any non-default types here with import statements

interface IRemoteCallback {
    void onCallback(in Bundle bundle);
}