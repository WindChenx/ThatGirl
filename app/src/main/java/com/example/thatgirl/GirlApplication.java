package com.example.thatgirl;

import android.app.Application;
import android.content.Context;

import com.example.thatgirl.model.SpStorage;

public class GirlApplication extends Application {
    private static Context mAppContext;

    public static Context getAppConext() {
        return mAppContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mAppContext = getApplicationContext();
        SpStorage.init(this);
    }
}
