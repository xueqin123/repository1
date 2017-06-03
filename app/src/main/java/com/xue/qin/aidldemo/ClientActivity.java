package com.xue.qin.aidldemo;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xue.qin.aidl.IMyAidlInterface;

/**
 * Created by xue.qin on 2017/6/1.
 */

public class ClientActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "ClientActivity";
    private TextView mTextView;
    private Button mButton;
    private IMyAidlInterface mService = null;
    private ServiceConnection mConntection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(TAG, "onServiceConnected()");
            mService = IMyAidlInterface.Stub.asInterface(service);
            mHandler.post(mRunable);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "onServiceDisconnected()");
            mService = null;
        }
    };
    Handler mHandler = new Handler();
    Runnable mRunable = new Runnable() {
        @Override
        public void run() {
            if(mService!=null){
                try {
                    mTextView.setText(mService.getCurrentTime(mCount%3));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            mHandler.postDelayed(mRunable,500);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_layout);
        mTextView = (TextView) findViewById(R.id.result);
        mButton = (Button) findViewById(R.id.switchmodel);
        mButton.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent("remoteService");
        intent.setPackage("com.xue.qin.server");
        boolean success = bindService(intent, mConntection, Context.BIND_AUTO_CREATE);
        Log.i(TAG, "onResume() success = " + success);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(mConntection);
    }
    private volatile int mCount = 0;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.switchmodel:
                mCount++;
                if(mCount>100){
                    mCount = 0;
                }
                break;
        }
    }
}
