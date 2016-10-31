package com.codemonkeylabs.fpslibrary;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnit4.class)
public class CalculationsTest {

    // 16.6ms
    private long oneFrameNS = TimeUnit.NANOSECONDS.convert(16600, TimeUnit.MICROSECONDS);

    @Test
    public void testBaseCase() {
        FPSConfig fpsConfig = new FPSConfig();
        List<Long> dataSet = new ArrayList<>();
        dataSet.add(0L);
        dataSet.add(TimeUnit.NANOSECONDS.convert(50, TimeUnit.MILLISECONDS));
        List<Integer> droppedSet = Calculation.getDroppedSet(fpsConfig, dataSet);
        assertThat(droppedSet.size()).isEqualTo(1);
        assertThat(droppedSet.get(0)).isEqualTo(2);
    }

    @Test
    public void testBaseGetAmountOfFramesInSet() {
        FPSConfig fpsConfig = new FPSConfig();
        assertThat(Calculation.getNumberOfFramesInSet(oneFrameNS, fpsConfig)).isEqualTo(1);
        assertThat(Calculation.getNumberOfFramesInSet(oneFrameNS * 5, fpsConfig)).isEqualTo(5);
        assertThat(Calculation.getNumberOfFramesInSet(oneFrameNS * 58, fpsConfig)).isEqualTo(58);
    }

    @Test
    public void testCalculateMetric() {
        FPSConfig fpsConfig = new FPSConfig();
        long start = 0;
        long end = oneFrameNS * 100;
        assertThat(Calculation.getNumberOfFramesInSet(end, fpsConfig)).isEqualTo(100);

        List<Long> dataSet = new ArrayList<>();
        dataSet.add(start);
        dataSet.add(end);

        List<Integer> droppedSet = new ArrayList<>();

        droppedSet.add(4);
        assertThat(Calculation.calculateMetric(fpsConfig, dataSet, droppedSet).getKey())
                .isEqualTo(Calculation.Metric.GOOD);

        droppedSet.add(6);
        assertThat(Calculation.calculateMetric(fpsConfig, dataSet, droppedSet).getKey())
                .isEqualTo(Calculation.Metric.MEDIUM);

        droppedSet.add(10);
        assertThat(Calculation.calculateMetric(fpsConfig, dataSet, droppedSet).getKey())
                .isEqualTo(Calculation.Metric.BAD);
    }

}
