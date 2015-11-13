package com.codemonkeylabs.fpslibrary;

import android.view.Choreographer;

import com.codemonkeylabs.fpslibrary.ui.TinyCoach;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brianplummer on 8/29/15.
 */
public class FPSFrameCallback implements Choreographer.FrameCallback
{
    private FPSConfig fpsConfig;
    private TinyCoach tinyCoach;
    private List<Long> dataSet; //holds the frame times of the sample set
    private boolean enabled = true;
    private long startSampleTimeInNs = 0;

    public FPSFrameCallback(FPSConfig fpsConfig, TinyCoach tinyCoach) {
        this.fpsConfig = fpsConfig;
        this.tinyCoach = tinyCoach;
        dataSet = new ArrayList<>();
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public void doFrame(long frameTimeNanos)
    {
        //if not enabled then we bail out now and don't register the callback
        if (!enabled){
            destroy();
            return;
        }

        //initial case
        if (startSampleTimeInNs == 0){
            startSampleTimeInNs = frameTimeNanos;
        }
        // only invoked for callbacks....
        else if (fpsConfig.frameDataCallback != null)
        {
            long start = dataSet.get(dataSet.size()-1);
            int droppedCount = Calculation.droppedCount(start, frameTimeNanos, fpsConfig.deviceRefreshRateInMs);
            fpsConfig.frameDataCallback.doFrame(start, frameTimeNanos, droppedCount);
        }

        //we have exceeded the sample length ~700ms worth of data...we should push results and save current
        //frame time in new list
        if (isFinishedWithSample(frameTimeNanos))
        {
            collectSampleAndSend(frameTimeNanos);
        }

        // add current frame time to our list
        dataSet.add(frameTimeNanos);

        //we need to register for the next frame callback
        Choreographer.getInstance().postFrameCallback(this);
    }

    private void collectSampleAndSend(long frameTimeNanos)
    {
        //this occurs only when we have gathered over the sample time ~700ms
        List<Long> dataSetCopy = new ArrayList<>();
        dataSetCopy.addAll(dataSet);

        //push data to the presenter
        tinyCoach.showData(fpsConfig, dataSetCopy);

        // clear data
        dataSet.clear();

        //reset sample timer to last frame
        startSampleTimeInNs = frameTimeNanos;
    }

    /**
     * returns true when sample length is exceed
     * @param frameTimeNanos current frame time in NS
     * @return
     */
    private boolean isFinishedWithSample(long frameTimeNanos)
    {
        return frameTimeNanos-startSampleTimeInNs > fpsConfig.getSampleTimeInNs();
    }

    private void destroy()
    {
        dataSet.clear();
        fpsConfig = null;
        tinyCoach = null;
    }

}
