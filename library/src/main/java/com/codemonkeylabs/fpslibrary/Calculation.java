package com.codemonkeylabs.fpslibrary;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
public class Calculation
{
    public enum Metric {GOOD, BAD, MEDIUM};

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
                
                long droppedCount = (diffMs / dev);
                droppedSet.add((int) droppedCount);
            }
        }
        return droppedSet;
    }

    public static AbstractMap.SimpleEntry<Metric, Long> calculateMetric(FPSConfig fpsConfig,
                                                                        List<Long> dataSet,
                                                                        List<Integer> droppedSet)
    {
        long timeInNS = dataSet.get(dataSet.size() - 1) - dataSet.get(0);
        long size = getNumberOfFramesInSet(timeInNS, fpsConfig);

        //metric
        int runningOver = 0;
        // total dropped
        int dropped = 0;

        for(Integer k : droppedSet){
            dropped+=k;
            if (k >=2) {
                runningOver+=k;
            }
        }

        float multiplier = fpsConfig.refreshRate / size;
        float answer = multiplier * (size - dropped);
        long realAnswer = Math.round(answer);

        // calculate metric
        float percentOver = (float)runningOver/(float)size;
        Metric metric = Metric.GOOD;
        if (percentOver >= fpsConfig.redFlagPercentage) {
            metric = Metric.BAD;
        } else if (percentOver >= fpsConfig.yellowFlagPercentage) {
            metric = Metric.MEDIUM;
        }

        return new AbstractMap.SimpleEntry<Metric, Long>(metric, realAnswer);
    }

    protected static long getNumberOfFramesInSet(long realSampleLengthNs, FPSConfig fpsConfig)
    {
        float realSampleLengthMs = TimeUnit.MILLISECONDS.convert(realSampleLengthNs, TimeUnit.NANOSECONDS);
        float size =  realSampleLengthMs/fpsConfig.deviceRefreshRateInMs;
        return Math.round(size);
    }

}
