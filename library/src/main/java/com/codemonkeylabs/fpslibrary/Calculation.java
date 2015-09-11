package com.codemonkeylabs.fpslibrary;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by brianplummer on 9/11/15.
 */
public class Calculation
{
    public static List<Integer> getDroppedSet(FPSConfig fpsConfig, List<Long> dataSet)
    {
        List<Integer> droppedSet = new ArrayList<>();
        long start = -1;
        for (Long value : dataSet) {
            if (start == -1) {
                start = value;
                continue;
            }

            long diffNs = value - start;
            start = value;
            long diffMs = TimeUnit.MILLISECONDS.convert(diffNs, TimeUnit.NANOSECONDS);
            long dev = Math.round(fpsConfig.deviceRefreshRateInMs);
            if (diffMs > dev) {
                long ugh = (diffMs / dev);//(long)fpsConfig.deviceRefreshRateInMs);
                //Log.e("#####", "BLAH::" + diffMs + ":" + ugh);
                droppedSet.add((int)ugh);
            }
        }
        return droppedSet;
    }

}
