package com.codemonkeylabs.fpslibrary.sample;

import android.app.Application;

public class FpsGraphFactory {

    public static final AppComponent getObjectGraph(Application context) {
        return DaggerAppComponent.builder()
                .appModule(new AppModule(context))
                .build();
    }
}