package com.rpcframework.aidlstudy;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.rpcframework.aidlstudy.databinding.ActivityMainBinding;
import com.rpcframework.bean.Info;
import com.rpcframework.conn.IConnMgr;
import com.rpcframework.log.ALog;
import com.rpcframework.study.Util;

import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    private IConnMgr mConnMgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ALog.d(TAG, "on create ... pid: " + android.os.Process.myPid());

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        View.OnClickListener l = v-> {
            if (v.getId() == binding.bindServiceBtn.getId()) {
                if (mConnMgr == null) {
                    ClientConnMgr mgr = new ClientConnMgr(MainActivity.this);
                    mgr.setServicePackage("com.rpcframework.aidlstudy");
                    mgr.setServiceName("com.rpcframework.aidlstudy.MyRemoteService");

                    mConnMgr = mgr;
                }
                mConnMgr.bind();
            } else if (v.getId() == binding.unbindServiceBtn.getId()) {
                if (mConnMgr != null) {
                    mConnMgr.unBind();
                }
            }else if (v.getId() == binding.killAnotherBtn.getId()) {
                Intent intent = new Intent("com.rpcframework.aidlstudy");
                intent.setComponent(new ComponentName("com.rpcframework.aidlstudy", "com.rpcframework.aidlstudy.MyRemoteService"));
                intent.putExtra("kill", true);
                startService(intent);
            }else if (v.getId() == binding.unbindServiceBtn.getId()) {
                Intent intent = new Intent("com.rpcframework.aidlstudy");
                intent.setComponent(new ComponentName("com.rpcframework.aidlstudy", "com.rpcframework.aidlstudy.MyRemoteService"));
                intent.putExtra("active", true);
                startService(intent);
            }else if (v.getId() == binding.callServerBtn.getId()) {
                Bundle bundle = new Bundle();
                bundle.putString("aa", "adfdfsdffaa");
                Info info = new Info();
                info.setName("alleen");
                info.setValue("djfkajsdfsdfs");
                info.setOther(12894);
                bundle.putParcelable("ininfo", info);
                try {
                    Bundle ret = mConnMgr.getAidl().send(bundle);
                    ret.setClassLoader(getClassLoader());
                    Util.bundlePrint("client", ret);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            } else if (v.getId() == binding.callServerBtn2.getId()) {
                try {
                    mConnMgr.getAidl().registerCallback(new IRemoteCallback.Stub() {
                        @Override
                        public void onCallback(Bundle bundle) throws RemoteException {
                            bundle.setClassLoader(getClassLoader());
                            ALog.d("onCallback....");
                            Util.bundlePrint("client", bundle);
                        }
                    });
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };
        binding.bindServiceBtn.setOnClickListener(l);
        binding.unbindServiceBtn.setOnClickListener(l);
        binding.startAnotherServiceBtn.setOnClickListener(l);
        binding.killAnotherBtn.setOnClickListener(l);
        binding.callServerBtn.setOnClickListener(l);
        binding.callServerBtn2.setOnClickListener(l);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mConnMgr != null) {
            mConnMgr.destroy();
        }
    }
}