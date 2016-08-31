package com.plit.leakdemo;

import android.app.Application;

import com.github.moduth.blockcanary.BlockCanary;
import com.squareup.leakcanary.LeakCanary;

/**
 * @author Cwugs.Chen.
 * @time 2016/8/29  22:15
 * @desc ${TODD}
 */
public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        LeakCanary.install(this);
        BlockCanary.install(this, new AppBlockCanaryContext()).start();
    }
}
