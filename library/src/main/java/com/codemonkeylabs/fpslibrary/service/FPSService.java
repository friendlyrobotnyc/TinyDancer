package com.codemonkeylabs.fpslibrary.service;

import android.app.Application;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.Choreographer;

import com.codemonkeylabs.fpslibrary.FPSConfig;
import com.codemonkeylabs.fpslibrary.FPSFrameCallback;
import com.codemonkeylabs.fpslibrary.Foreground;

public class FPSService extends Service {

    public static final String ARG_FPS_CONFIG = "ARG_FPS_CONFIG";

    private FPSFrameCallback fpsFrameCallback;
    private MeterPresenter meterPresenter;
    private Foreground.Listener foregroundListener = new Foreground.Listener() {
        @Override
        public void onBecameForeground() {
            meterPresenter.show();
        }

        @Override
        public void onBecameBackground() {
            meterPresenter.hide(false);
        }
    };

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

        //set activity background/foreground listener
        Foreground.init(getApplication()).addListener(foregroundListener);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {

        // tell callback to stop registering itself
        fpsFrameCallback.setEnabled(false);

        Foreground.get(this).removeListener(foregroundListener);
        // remove the view from the window
        meterPresenter.destroy();

        // paranoia cha-cha-cha
        foregroundListener = null;
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
