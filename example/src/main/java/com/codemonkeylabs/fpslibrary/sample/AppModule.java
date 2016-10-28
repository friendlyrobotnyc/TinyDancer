package com.codemonkeylabs.fpslibrary.sample;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    private Application context;

    public AppModule(Application context) {
        this.context = context;
    }



    @Singleton
    @Provides
    Application provideApplication() {
        return context;
    }


}