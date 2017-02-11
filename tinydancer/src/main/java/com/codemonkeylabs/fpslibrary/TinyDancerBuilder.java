package com.codemonkeylabs.fpslibrary;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.view.Choreographer;
import android.view.Display;
import android.view.WindowManager;

import com.codemonkeylabs.fpslibrary.ui.TinyCoach;

/**
 * Created by brianplummer on 8/29/15.
 */
public class TinyDancerBuilder
{
    private static FPSConfig fpsConfig;
    private static FPSFrameCallback fpsFrameCallback;
    private static TinyCoach tinyCoach;
    private static Foreground.Listener foregroundListener = new Foreground.Listener() {
        @Override
        public void onBecameForeground() {
            tinyCoach.show();
        }

        @Override
        public void onBecameBackground() {
            tinyCoach.hide(false);
        }
    };

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
     * stops the frame callback and foreground listener
     * nulls out static variables
     * called from FPSLibrary in a static context
     * @param context
     */
    protected static void hide(Context context) {
        // tell callback to stop registering itself
        fpsFrameCallback.setEnabled(false);

        Foreground.get(context).removeListener(foregroundListener);
        // remove the view from the window
        tinyCoach.destroy();

        // null it all out
        tinyCoach = null;
        fpsFrameCallback = null;
        fpsConfig = null;
    }

    // PUBLIC BUILDER METHODS

    /**
     * show fps meter, this regisers the frame callback that
     * collects the fps info and pushes it to the ui
     * @param context
     */
    public void show(Context context) {

        if (overlayPermRequest(context)) {
            //once permission is granted then you must call show() again
            return;
        }

        //are we running?  if so, call tinyCoach.show() and return
        if (tinyCoach != null) {
            tinyCoach.show();
            return;
        }

        // set device's frame rate info into the config
        setFrameRate(context);

        // create the presenter that updates the view
        tinyCoach = new TinyCoach((Application) context.getApplicationContext(), fpsConfig);

        // create our choreographer callback and register it
        fpsFrameCallback = new FPSFrameCallback(fpsConfig, tinyCoach);
        Choreographer.getInstance().postFrameCallback(fpsFrameCallback);

        //set activity background/foreground listener
        Foreground.init((Application) context.getApplicationContext()).addListener(foregroundListener);
    }

    /**
     * this adds a frame callback that the library will invoke on the
     * each time the choreographer calls us, we will send you the frame times
     * and number of dropped frames.
     * @param callback
     * @return
     */
    public TinyDancerBuilder addFrameDataCallback(FrameDataCallback callback) {
        fpsConfig.frameDataCallback = callback;
        return this;
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
        fpsConfig.xOrYSpecified = true;
        return this;
    }

    /**
     * starting y positon of fps meter default is 600px
     * @param yPosition
     * @return
     */
    public TinyDancerBuilder startingYPosition(int yPosition) {
        fpsConfig.startingYPosition = yPosition;
        fpsConfig.xOrYSpecified = true;
        return this;
    }

    /**
     * starting gravity of fps meter default is Gravity.TOP | Gravity.START;
     * @param gravity
     * @return
     */
    public TinyDancerBuilder startingGravity(int gravity) {
        fpsConfig.startingGravity = gravity;
        fpsConfig.gravitySpecified = true;
        return this;
    }

    /**
     * request overlay permission when api >= 23
     * @param context
     * @return
     */
    private boolean overlayPermRequest(Context context) {
        boolean permNeeded = false;
        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (!Settings.canDrawOverlays(context))
            {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + context.getPackageName()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                permNeeded = true;
            }
        }
        return permNeeded;
    }

}
