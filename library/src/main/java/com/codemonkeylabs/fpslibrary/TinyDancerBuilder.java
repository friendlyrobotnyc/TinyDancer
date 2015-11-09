package com.codemonkeylabs.fpslibrary;

import android.content.Context;
import android.content.Intent;
import android.view.Display;
import android.view.WindowManager;

import com.codemonkeylabs.fpslibrary.service.FPSService;

/**
 * Created by brianplummer on 8/29/15.
 */
public class TinyDancerBuilder
{
    private FPSConfig fpsConfig;

    protected TinyDancerBuilder(){
        fpsConfig = new FPSConfig();
    }

    /**
     * configures the fpsConfig to the device's hardware
     * refreshRate ex. 60fps and deviceRefreshRateInMs ex. 16.6ms
     * @param context
     */
    private void setFrameRate(Context context){
        Display display = ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        fpsConfig.deviceRefreshRateInMs = 1000f/display.getRefreshRate();
        fpsConfig.refreshRate = display.getRefreshRate();
    }

    /**
     * stops the FPSService and "hides" the meter
     * called from FPSLibrary in a static context
     * @param context
     */
    protected static void hide(Context context) {
        context.stopService(new Intent(context, FPSService.class));
    }

    // PUBLIC BUILDER METHODS

    /**
     * show fps meter, this starts the background service that
     * collects the fps info and pushes it to the ui
     * @param context
     */
    public void show(Context context) {
        Intent intent = new Intent(context, FPSService.class);
        setFrameRate(context);
        intent.putExtra(FPSService.ARG_FPS_CONFIG, fpsConfig);
        context.startService(intent);
    }

    /**
     *  set red flag percent, default is 20%
     *
     * @param percentage
     * @return
     */
    public TinyDancerBuilder redFlagPercentage(float percentage) {
        fpsConfig.redFlagPercentage = percentage;
        return this;
    }

    /**
     * set red flag percent, default is 5%
     * @param percentage
     * @return
     */
    public TinyDancerBuilder yellowFlagPercentage(float percentage) {
        fpsConfig.yellowFlagPercentage = percentage;
        return this;
    }

    /**
     * starting x position of fps meter default is 200px
     * @param xPosition
     * @return
     */
    public TinyDancerBuilder startingXPosition(int xPosition) {
        fpsConfig.startingXPosition = xPosition;
        return this;
    }

    /**
     * starting y positon of fps meter default is 600px
     * @param yPosition
     * @return
     */
    public TinyDancerBuilder startingYPosition(int yPosition) {
        fpsConfig.startingYPosition = yPosition;
        return this;
    }

    /**
     * starting gravity of fps meter default is Gravity.TOP | Gravity.START;
     * @param gravity
     * @return
     */
    public TinyDancerBuilder startingGravity(int gravity) {
        fpsConfig.startingGravity = gravity;
        return this;
    }
}
