package com.hello.youtubeplayer.common;

import android.app.Application;
import android.content.Context;

public class App extends Application {

    public static Context context;

    public App(Context context) {
        this.context = context;
    }

    public static Context getContext() {
        return context;
    }
}
