package com.codemonkeylabs.fpslibrary.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.TypedValue;
import android.view.Choreographer;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.codemonkeylabs.fpslibrary.FPSConfig;
import com.codemonkeylabs.fpslibrary.FPSFrameCallback;
import com.codemonkeylabs.fpslibrary.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by brianplummer on 8/29/15.
 */
public class FPSService extends Service
{
    private FPSFrameCallback fpsFrameCallback = null;
    private FPSMeterController fpsMeterController = null;
    public static final String ARG_FPS_CONFIG = "ARG_FPS_CONFIG";

    public void onCreate()
    {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        if (fpsFrameCallback != null || intent == null ||!intent.hasExtra(ARG_FPS_CONFIG)) {
            return super.onStartCommand(intent, flags, startId);
        }

        // fetch config passed in from the builder
        FPSConfig fpsConfig = (FPSConfig) intent.getSerializableExtra(ARG_FPS_CONFIG);

        //create and configure floating view
        TextView image = createFloatingView(fpsConfig);

        // create the controller that updates the view
        fpsMeterController = new FPSMeterController(image);

        // create our choreographer callback and register it
        fpsFrameCallback = new FPSFrameCallback(fpsConfig, fpsMeterController);
        Choreographer.getInstance().postFrameCallback(fpsFrameCallback);

        return super.onStartCommand(intent, flags, startId);
    }

    @NonNull
    private TextView createFloatingView(FPSConfig fpsConfig)
    {
        TextView image = new TextView(this);
        image.setTextSize(TypedValue.COMPLEX_UNIT_SP, 23);
        image.setTextColor(Color.BLACK);
        image.setText((int) fpsConfig.refreshRate + "");
        image.setPadding(30,30,30,30);
        image.setBackgroundResource(R.drawable.fpsmeterring_good);
        return image;
    }

    @Override
    public void onDestroy()
    {
        fpsMeterController.destroy();
        fpsFrameCallback.setEnabled(false);
        Choreographer.getInstance().removeFrameCallback(fpsFrameCallback);
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

}
