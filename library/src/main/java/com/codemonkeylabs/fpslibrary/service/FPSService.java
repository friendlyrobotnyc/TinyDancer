package com.codemonkeylabs.fpslibrary.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.Choreographer;
import android.widget.TextView;

import com.codemonkeylabs.fpslibrary.FPSConfig;
import com.codemonkeylabs.fpslibrary.FPSFrameCallback;
import com.codemonkeylabs.fpslibrary.FPSMeterController;
import com.codemonkeylabs.fpslibrary.R;

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

        FPSConfig fpsConfig = (FPSConfig) intent.getSerializableExtra(ARG_FPS_CONFIG);

        TextView image = new TextView(this);
        image.setTextSize(TypedValue.COMPLEX_UNIT_SP, 23);
        image.setTextColor(Color.BLACK);
        image.setText((int) fpsConfig.refreshRate + "");
        image.setPadding(20,20,20,20);



        //View view = new View(this);
        //view.setBackground(new FPSMeterDrawable());//new ColorDrawable(Color.RED));

        //FPSMeterDrawable fpsMeterDrawable = new FPSMeterDrawable();
        //imfage.setBackground(fpsMeterDrawable);
        image.setBackgroundResource(R.drawable.fpsmeterring_good);

        fpsMeterController = new FPSMeterController(image);
        fpsFrameCallback = new FPSFrameCallback(fpsConfig, fpsMeterController);
        Choreographer.getInstance().postFrameCallback(fpsFrameCallback);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy()
    {
        fpsMeterController.destroy();
        fpsFrameCallback.setEnabled(false);
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

}
