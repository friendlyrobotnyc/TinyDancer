package com.codemonkeylabs.fpslibrary.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.Choreographer;
import android.widget.TextView;

import com.codemonkeylabs.fpslibrary.FPSConfig;
import com.codemonkeylabs.fpslibrary.FPSFrameCallback;
import com.codemonkeylabs.fpslibrary.R;

public class FPSService extends Service {
    public static final int IMAGE_PADDING = 30;
    public static final int TEXT_SIZE = 23;
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

        //create and configure floating view
        TextView image = createFloatingView(fpsConfig);

        // create the presenter that updates the view
        meterPresenter = new MeterPresenter(image);

        // create our choreographer callback and register it
        fpsFrameCallback = new FPSFrameCallback(fpsConfig, meterPresenter);
        Choreographer.getInstance().postFrameCallback(fpsFrameCallback);

        return super.onStartCommand(intent, flags, startId);
    }

    @NonNull
    private TextView createFloatingView(FPSConfig fpsConfig) {
        TextView image = new TextView(this);
        image.setTextSize(TypedValue.COMPLEX_UNIT_SP, TEXT_SIZE);
        image.setTextColor(Color.BLACK);
        image.setText((int) fpsConfig.refreshRate + "");
        image.setPadding(IMAGE_PADDING,
                IMAGE_PADDING,
                IMAGE_PADDING,
                IMAGE_PADDING);
        image.setBackgroundResource(R.drawable.fpsmeterring_good);
        return image;
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
