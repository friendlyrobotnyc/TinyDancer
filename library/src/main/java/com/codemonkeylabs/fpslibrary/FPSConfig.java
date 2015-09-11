package com.codemonkeylabs.fpslibrary;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * Created by brianplummer on 8/29/15.
 */
public class FPSConfig implements Serializable
{
    public float redFlagPercentage = 0.2f;
    public float yellowFlagPercentage = 0.05f;
    public float refreshRate = 60; //60fps
    public long sampleTimeInMs = 736;//928;//736; // default sample time
    public float deviceRefreshRateInMs = 16.6f; //value from device ex 16.6 ms

    protected FPSConfig()
    {}

    public long getSampleTimeInNs()
    {
        return TimeUnit.NANOSECONDS.convert(sampleTimeInMs, TimeUnit.MILLISECONDS);
    }

    public long getDeviceRefreshRateInNs()
    {
        float value = deviceRefreshRateInMs * 1000000f;
        return (long) value;
    }
}
