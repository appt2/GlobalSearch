package com.bluewhaleyt.globalsearch;

import android.app.Application;
import android.content.Context;

import com.bluewhaleyt.common.DynamicColorsUtil;

public class App extends Application {

    private Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        DynamicColorsUtil.setDynamicColorsIfAvailable(this);
    }

    public Context getContext() {
        return context;
    }
}
