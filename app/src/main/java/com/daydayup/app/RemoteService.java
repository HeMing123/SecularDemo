package com.daydayup.app;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**
 * @author wanghm
 * @date 2017/7/17.
 */

public class RemoteService extends Service {
    private MyBinder binder;
    private ServiceConnection conn;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (binder == null) {
            binder = new MyBinder();
        }
        if (conn == null) {
            conn = new MyConn();
        }
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        this.bindService(new Intent(RemoteService.this, LocalService.class), conn, BIND_IMPORTANT);
    }

    private class MyBinder extends SecularDemo.Stub {

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public String getServiceName() throws RemoteException {
            return "remoteService";
        }
    }

    private class MyConn implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e("tag", "RemoteService绑定成功");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Toast.makeText(RemoteService.this, "LocalService被杀死", Toast.LENGTH_SHORT).show();
            startService(new Intent(RemoteService.this, LocalService.class));
            bindService(new Intent(RemoteService.this, LocalService.class), conn, BIND_IMPORTANT);
        }
    }
}
