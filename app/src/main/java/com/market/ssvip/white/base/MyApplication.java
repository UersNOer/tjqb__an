package com.market.ssvip.white.base;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.market.ssvip.white.mode.EnvManager;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * @author LiCola
 * @date 2018/10/29
 */
public class MyApplication extends Application {


    private static MyApplication application;

    public static MyApplication getApplication() {
        return application;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
        CrashReport.initCrashReport(getApplicationContext(), "a1e2097a4f", false);


    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        provide(this);

    }

    private void provide(MyApplication application) {
        EnvManager.getEnvManager().initEnvironment(application);
    }
}
