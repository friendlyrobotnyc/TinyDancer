package com.codemonkeylabs.fpslibrary.service;

import android.app.Service;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.codemonkeylabs.fpslibrary.Calculation;
import com.codemonkeylabs.fpslibrary.FPSConfig;
import com.codemonkeylabs.fpslibrary.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by brianplummer on 8/29/15.
 */
public class FPSMeterController
{
    private View meterView;
    private WindowManager mWindowManager;
    private Handler handler = new Handler();

    public FPSMeterController(View meterView){
        this.meterView = meterView;
        mWindowManager = (WindowManager) meterView.getContext().getSystemService(Service.WINDOW_SERVICE);
        createMeter();
    }

    private void createMeter(){

        WindowManager.LayoutParams paramsF = new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);


        paramsF.gravity = Gravity.TOP | Gravity.LEFT;
        paramsF.x=200;
        paramsF.y=600;
        mWindowManager.addView(meterView, paramsF);
        setMeterListener(paramsF);
    }

    public void showData(FPSConfig fpsConfig, List<Long> dataSet) {


        //metric
        int runningOver = 0;
        // total dropped
        int dropped = 0;

        List<Integer> droppedSet = Calculation.getDroppedSet(fpsConfig, dataSet);

        for(Integer k : droppedSet){
            dropped+=k;
            if (k >=2) {
                runningOver+=k;
            }
        }

        long realSampleLengthNs = dataSet.get(dataSet.size()-1) - dataSet.get(0);
        long realSampleLengthMs = TimeUnit.MILLISECONDS.convert(realSampleLengthNs, TimeUnit.NANOSECONDS);
        long size = (long) realSampleLengthMs/(long) fpsConfig.deviceRefreshRateInMs;

        float multiplier = fpsConfig.refreshRate / size;
        float answer = multiplier * (size - dropped);
        long realAnswer = Math.round(answer);

        // calculate metric
        float percentOver = (float)runningOver/(float)size;
        //Log.e("########","METRIC"+percentOver);

        if (percentOver >= fpsConfig.redFlagPercentage) {
            meterView.setBackgroundResource(R.drawable.fpsmeterring_bad);
        } else if (percentOver >= fpsConfig.yellowFlagPercentage) {
            meterView.setBackgroundResource(R.drawable.fpsmeterring_medium);
        } else {
            meterView.setBackgroundResource(R.drawable.fpsmeterring_good);
        }

        ((TextView) meterView).setText(realAnswer + "");

    }



    private void setMeterListener(final WindowManager.LayoutParams paramsF) {
        try{

            meterView.setOnTouchListener(new View.OnTouchListener() {
                WindowManager.LayoutParams paramsT = paramsF;
                private int initialX;
                private int initialY;
                private float initialTouchX;
                private float initialTouchY;
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch(event.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            initialX = paramsF.x;
                            initialY = paramsF.y;
                            initialTouchX = event.getRawX();
                            initialTouchY = event.getRawY();
                            break;
                        case MotionEvent.ACTION_UP:
                            break;
                        case MotionEvent.ACTION_MOVE:
                            paramsF.x = initialX + (int) (event.getRawX() - initialTouchX);
                            paramsF.y = initialY + (int) (event.getRawY() - initialTouchY);
                            mWindowManager.updateViewLayout(v, paramsF);
                            break;
                    }
                    return false;
                }
            });
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void destroy(){
        meterView.setVisibility(View.GONE);
        ((WindowManager)meterView.getContext().getSystemService(Service.WINDOW_SERVICE)).removeView(meterView);
        meterView = null;
    }

}
