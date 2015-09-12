package com.codemonkeylabs.fpslibrary;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
//TODO: Clean this shit up, ugh is not a variable name :-)
//todo word....agreed....will do... :)
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
                long ugh = (diffMs / dev);//(long)fpsConfig.deviceRefreshRateInMs);
                droppedSet.add((int)ugh);
            }
        }
        return droppedSet;
    }

    public static AbstractMap.SimpleEntry<Metric, Long> calculateMetric(FPSConfig fpsConfig, List<Long> dataSet, List<Integer> droppedSet) {
        Metric metric = Metric.GOOD;

        long realSampleLengthNs = dataSet.get(dataSet.size()-1) - dataSet.get(0);
        long realSampleLengthMs = TimeUnit.MILLISECONDS.convert(realSampleLengthNs, TimeUnit.NANOSECONDS);
        long size = (long) realSampleLengthMs/(long) fpsConfig.deviceRefreshRateInMs;

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

        if (percentOver >= fpsConfig.redFlagPercentage) {
            metric = Metric.BAD;
        } else if (percentOver >= fpsConfig.yellowFlagPercentage) {
            metric = Metric.MEDIUM;
        }

        return new AbstractMap.SimpleEntry<Metric, Long>(metric, realAnswer);
    }


}
