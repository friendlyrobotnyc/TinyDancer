package com.codemonkeylabs.fpslibrary;

import android.view.Choreographer;

import com.codemonkeylabs.fpslibrary.service.FPSMeterController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brianplummer on 8/29/15.
 */
public class FPSFrameCallback implements Choreographer.FrameCallback
{
    private FPSConfig fpsConfig;
    private Choreographer choreographer;
    private FPSMeterController fpsMeterController;
    private List<Long> dataSet;
    private boolean enabled = true;
    private long startSampleTimeInNs = 0;

    public FPSFrameCallback(FPSConfig fpsConfig, FPSMeterController fpsMeterController) {
        this.fpsConfig = fpsConfig;
        this.fpsMeterController = fpsMeterController;
        choreographer = Choreographer.getInstance();
        dataSet = new ArrayList<Long>();
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public void doFrame(long frameTimeNanos)
    {
        if (!enabled){
            return;
        }

        //initial case
        if (startSampleTimeInNs == 0){
            startSampleTimeInNs = frameTimeNanos;
        }

        //we have exceeded the sample length...we should push results and save current
        //frame time in new list
        if (frameTimeNanos-startSampleTimeInNs > fpsConfig.getSampleTimeInNs()){
            List<Long> dataSetCopy = new ArrayList<Long>();
            dataSetCopy.addAll(dataSet);
            fpsMeterController.showData(fpsConfig, dataSetCopy);
            dataSet.clear();
            startSampleTimeInNs = frameTimeNanos;
        }
        dataSet.add(frameTimeNanos);

        choreographer.postFrameCallback(this);
    }

}
