package com.codemonkeylabs.fpslibrary;

/**
 * Created by brianplummer on 11/12/15.
 */
public interface FrameDataCallback
{
    /**
     * this is called for every doFrame() on the choreographer callback
     * use this very judiciously.  you don't even want to Log.d() from here, bad idea
     * this will get called every ~16 or ~32ms, hopefully ;)
     * @param previousFrameNS previous vsync frame time in NS
     * @param currentFrameNS current vsync frame time in NS
     * @param droppedFrames number of dropped frames between current and previous times
     */
    void doFrame(long previousFrameNS, long currentFrameNS, int droppedFrames);
}
