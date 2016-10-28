package com.codemonkeylabs.fpslibrary.sample;

public class FPSApplication extends android.app.Application {
    private AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = FpsGraphFactory.getObjectGraph(this);
        component.inject(this);
    }

    public AppComponent getComponent() {
        return component;
    }


}