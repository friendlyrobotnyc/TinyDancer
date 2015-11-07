package com.codemonkeylabs.fpslibrary;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

/**
 * Created by brianplummer on 9/11/15.
 */
@RunWith(JUnit4.class)
public class CalculationsTest
{

    private long oneFrameNS = 16600000;

    @Test
    public void testBaseCase()
    {
        FPSConfig fpsConfig = new FPSConfig();
        List<Long> dataSet = new ArrayList<>();
        dataSet.add(0l);
        dataSet.add(TimeUnit.NANOSECONDS.convert(50, TimeUnit.MILLISECONDS));
        List<Integer> droppedSet = Calculation.getDroppedSet(fpsConfig, dataSet);
        assertTrue(droppedSet.size() == 1);
        assertTrue(droppedSet.get(0) == 2);
    }

    @Test
    public void testBaseGetAmountOfFramesInSet()
    {
        FPSConfig fpsConfig = new FPSConfig();
        assertTrue(Calculation.getNumberOfFramesInSet(oneFrameNS, fpsConfig) == 1);
        assertTrue(Calculation.getNumberOfFramesInSet(oneFrameNS * 5, fpsConfig) == 5);
        assertTrue(Calculation.getNumberOfFramesInSet(oneFrameNS * 58,fpsConfig) == 58);
    }

    @Test
    public void testCalculateMetric()
    {
        FPSConfig fpsConfig = new FPSConfig();
        long start = 0;
        long end = oneFrameNS * 100;
        assertTrue(Calculation.getNumberOfFramesInSet(end, fpsConfig) == 100);

        List<Long> dataSet = new ArrayList<>();
        dataSet.add(start);
        dataSet.add(end);

        List<Integer> droppedSet = new ArrayList<>();

        droppedSet.add(4);
        assertTrue(Calculation.calculateMetric(fpsConfig, dataSet, droppedSet).getKey() == Calculation.Metric.GOOD);

        droppedSet.add(6);
        assertTrue(Calculation.calculateMetric(fpsConfig, dataSet, droppedSet).getKey() == Calculation.Metric.MEDIUM);

        droppedSet.add(10);
        assertTrue(Calculation.calculateMetric(fpsConfig, dataSet, droppedSet).getKey() == Calculation.Metric.BAD);
    }

}
