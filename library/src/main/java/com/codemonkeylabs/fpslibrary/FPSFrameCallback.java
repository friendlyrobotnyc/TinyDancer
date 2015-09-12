package com.codemonkeylabs.fpslibrary;

import android.view.Choreographer;

import com.codemonkeylabs.fpslibrary.service.MeterPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brianplummer on 8/29/15.
 */
public class FPSFrameCallback implements Choreographer.FrameCallback
{
    private FPSConfig fpsConfig;
    private MeterPresenter meterPresenter;
    private List<Long> dataSet; //holds the frame times of the sample set
    private boolean enabled = true;
    private long startSampleTimeInNs = 0;

    public FPSFrameCallback(FPSConfig fpsConfig, MeterPresenter meterPresenter) {
        this.fpsConfig = fpsConfig;
        this.meterPresenter = meterPresenter;
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

        //we have exceeded the sample length...we should push results and save current
        //frame time in new list
        if (frameTimeNanos-startSampleTimeInNs > fpsConfig.getSampleTimeInNs()){
            List<Long> dataSetCopy = new ArrayList<>();
            dataSetCopy.addAll(dataSet);

            //push data to the controller
            meterPresenter.showData(fpsConfig, dataSetCopy);

            // clear data
            dataSet.clear();
            //reset sample timer to last frame
            startSampleTimeInNs = frameTimeNanos;
        }
        dataSet.add(frameTimeNanos);

        //we need to register for the next frame callback
        Choreographer.getInstance().postFrameCallback(this);
    }

    private void destroy()
    {
        dataSet.clear();
        fpsConfig = null;
        meterPresenter = null;
    }

}
