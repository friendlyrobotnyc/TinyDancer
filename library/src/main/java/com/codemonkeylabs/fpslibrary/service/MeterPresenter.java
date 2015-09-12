package com.codemonkeylabs.fpslibrary.service;

import android.app.Application;
import android.app.Service;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.codemonkeylabs.fpslibrary.Calculation;
import com.codemonkeylabs.fpslibrary.FPSConfig;
import com.codemonkeylabs.fpslibrary.R;

import java.util.AbstractMap;
import java.util.List;

public class MeterPresenter {

    private FPSConfig fpsConfig;
    private View meterView;
    private final WindowManager windowManager;

    public MeterPresenter(Application context, FPSConfig config) {

        fpsConfig = config;
        LayoutInflater inflater = LayoutInflater.from(context);

        //create and configure floating view
        meterView = createView(inflater);
        //set initial fps value....might change...
        ((TextView) meterView).setText((int) fpsConfig.refreshRate + "");

        // grab window manager and add view to the window
        windowManager = (WindowManager) meterView.getContext().getSystemService(Service.WINDOW_SERVICE);
        initView(meterView);
    }

    private View createView(LayoutInflater layoutInflater) {
        View view = layoutInflater.inflate(R.layout.meter_view, null);
        return view;
    }

    private void initView(View view) {

        WindowManager.LayoutParams paramsF = new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        paramsF.gravity = Gravity.TOP | Gravity.START;
        paramsF.x = fpsConfig.startingXPosition;
        paramsF.y = fpsConfig.startingYPosition;
        windowManager.addView(view, paramsF);
        setMeterListener(paramsF);
    }

    public void showData(FPSConfig fpsConfig, List<Long> dataSet) {

        List<Integer> droppedSet = Calculation.getDroppedSet(fpsConfig, dataSet);
        AbstractMap.SimpleEntry<Calculation.Metric, Long> answer = Calculation.calculateMetric(fpsConfig, dataSet, droppedSet);

        if (answer.getKey() == Calculation.Metric.BAD) {
            meterView.setBackgroundResource(R.drawable.fpsmeterring_bad);
        } else if (answer.getKey() == Calculation.Metric.MEDIUM) {
            meterView.setBackgroundResource(R.drawable.fpsmeterring_medium);
        } else {
            meterView.setBackgroundResource(R.drawable.fpsmeterring_good);
        }

        ((TextView) meterView).setText(answer.getValue() + "");
    }

    private void setMeterListener(final WindowManager.LayoutParams paramsF) {
        try {
            //TODO: move this out to a separate class...maybe....
            meterView.setOnTouchListener(new View.OnTouchListener() {
                private int initialX;
                private int initialY;
                private float initialTouchX;
                private float initialTouchY;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
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
                            windowManager.updateViewLayout(v, paramsF);
                            break;
                    }
                    return false;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void destroy() {
        meterView.setVisibility(View.GONE);
        windowManager.removeView(meterView);
        meterView = null;
    }

}
