package com.codemonkeylabs.fpslibrary.service;

import android.app.Application;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.Choreographer;

import com.codemonkeylabs.fpslibrary.FPSConfig;
import com.codemonkeylabs.fpslibrary.FPSFrameCallback;

public class FPSService extends Service {
    private FPSFrameCallback fpsFrameCallback;
    private MeterPresenter meterPresenter;
    public static final String ARG_FPS_CONFIG = "ARG_FPS_CONFIG";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (fpsFrameCallback != null || intent == null || !intent.hasExtra(ARG_FPS_CONFIG)) {
            return super.onStartCommand(intent, flags, startId);
        }

        // fetch config from intent
        FPSConfig fpsConfig = (FPSConfig) intent.getSerializableExtra(ARG_FPS_CONFIG);

        // create the presenter that updates the view
        meterPresenter = new MeterPresenter((Application) getApplicationContext(), fpsConfig);

        // create our choreographer callback and register it
        fpsFrameCallback = new FPSFrameCallback(fpsConfig, meterPresenter);
        Choreographer.getInstance().postFrameCallback(fpsFrameCallback);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        // remove the view from the window
        meterPresenter.destroy();
        // tell callback to stop registering itself
        fpsFrameCallback.setEnabled(false);

        // paranoia cha-cha-cha
        meterPresenter = null;
        fpsFrameCallback = null;
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
