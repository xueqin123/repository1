package com.xue.qin.server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;

import com.xue.qin.aidl.IMyAidlInterface;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by xue.qin on 2017/6/1.
 */

public class ServerService extends Service {
    private static final String TAG = "ServerService";
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG,"onCreate()");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    private class MyBinder extends IMyAidlInterface.Stub{


        @Override
        public String getCurrentTime(int which) throws RemoteException {
            String result="";
            switch (which){
                case 0:
                    SimpleDateFormat sdf = new SimpleDateFormat("", Locale.SIMPLIFIED_CHINESE);
                    sdf.applyPattern("yyyy年MM月dd日 \nHH时mm分ss秒");
                    result = sdf.format(System.currentTimeMillis());
                    break;
                case 1:
                    result = "开机之后过去了\n "+formatTime(SystemClock.elapsedRealtime());
                    break;
                case 2:
                    //SystemClock.uptimeMillis() 去掉休眠的时间
                    result = "实际使用了\n" +formatTime(SystemClock.uptimeMillis());
                    break;
            }
            return result;
        }
    }

    private String formatTime(long time ){
        int total = (int) (time/1000);
        int seconds = total%60;
        int minute = total%3600/60;
        int hours = total/3600;
        return String.format("%d小时%d分钟 %d秒",hours,minute,seconds);
    }
}
