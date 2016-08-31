package com.plit.leakdemo;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {

    private Button btn_load;
    private TextView textView;

    private MyHandler mHandler;
    private MyThread mt;
    private boolean isClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_load = (Button)findViewById(R.id.btn_load);
        textView = (TextView)findViewById(R.id.textView);

        mHandler = new MyHandler(this);
        mt = new MyThread(this);
        SystemClock.sleep(1000);

        /*android.os.Debug.startMethodTracing("jackytest");// 开始监听
        for (int i = 0; i < 100; i++) {
            for (int j =0; j < i; j++) {
                String name="jackytan"+i*j;
                Log.i("ddd", "my name is: "+name);
            }
        }
        android.os.Debug.stopMethodTracing();//结束监听*/


        btn_load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("btn_load", "loading datas");
                loadData();
            }
        });

        //asycData();
    }

    private void loadData() {
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                //do sonething
                SystemClock.sleep(10000);
                //发送消息
                mHandler.sendEmptyMessageDelayed(0, 20000);
            }
        }).start();*/
        mHandler = new MyHandler(this);
        mt = new MyThread(this);
        mt.start();
    }


    private static class  MyThread extends  Thread {
        private WeakReference<MainActivity> weak;

        public MyThread(MainActivity activity) {
            weak = new WeakReference<MainActivity>(activity);
        }

        public void close() {
            if(null != weak && null != weak.get()) {
                weak.get().isClose = true;
            }
        }

        @Override
        public void run() {
            if(null != weak && null != weak.get()) {
                if(weak.get().isClose) {
                    //直接返回
                    return;
                }
            }
            //do sonething
            SystemClock.sleep(100);
            //发送消息
            if(null != weak && null != weak.get()) {
                weak.get().mHandler.sendEmptyMessageDelayed(0, 20000);
            }
        }


    }


    private static class  MyHandler extends Handler {
        private WeakReference<MainActivity> weak;

        public MyHandler(MainActivity activity) {
            weak = new WeakReference<MainActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 0) {
                Log.i("handleMessage", "got datas");
                if(null != weak && null != weak.get()) {
                    weak.get().textView.setText("goodbye world");
                }
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
        mt.close();
    }















    public void asycData() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                SystemClock.sleep(30000);
                return null;
            }
        }.execute();
    }
}
