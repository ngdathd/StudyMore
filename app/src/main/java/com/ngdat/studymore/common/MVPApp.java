package com.ngdat.studymore.common;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

public class MVPApp extends Application {
    @SuppressLint("StaticFieldLeak")
    private static Context mContextApp;

    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        mContextApp = this;
    }

    public static Context getmContextApp() {
        return mContextApp;
    }
}