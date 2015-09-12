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
}
