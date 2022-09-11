// IRemoteCallback.aidl
package com.rpcframework.sdk;
import android.os.Bundle;

interface IRemoteCallback {
    void onCallback(in Bundle bundle);
}