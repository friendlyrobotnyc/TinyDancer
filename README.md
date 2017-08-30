# Tiny Dancer [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-TinyDancer-green.svg?style=true)](https://android-arsenal.com/details/1/3172) [![Build Status](https://travis-ci.org/friendlyrobotnyc/TinyDancer.svg?branch=master)](https://travis-ci.org/friendlyrobotnyc/TinyDancer)

A real time frames per second measuring library for Android that also shows a color coded metric.  This metric is based on percentage of time spent when you have dropped 2 or more frames.  If the application spends more than 5% in this state then the color turns yellow, when you have reached the 20% threshold the indicator turns red.  

 **New** Double tap overlay to hide!

*“Perf Matters”* - Random Guy at Meetup

![Tiny Dancer](https://raw.githubusercontent.com/brianPlummer/TinyDancer/master/assets/tinydancer2.png "Tiny Dancer")

## Min SDK
Tiny Dancer min sdk is API 16

**Unfortunately this will not work on Android TV**

## Getting started

In your `build.gradle`:

```gradle
 dependencies {
   debugCompile "com.github.brianPlummer:tinydancer:0.1.2"
   releaseCompile "com.github.brianPlummer:tinydancer-noop:0.1.2"
   testCompile "com.github.brianPlummer:tinydancer-noop:0.1.2"
 }
```

In your `DebugApplication` class:

```java
public class DebugApplication extends Application {

  @Override public void onCreate() {

   TinyDancer.create()
             .show(context);
             
   //alternatively
   TinyDancer.create()
      .redFlagPercentage(.1f) // set red indicator for 10%....different from default
      .startingXPosition(200)
      .startingYPosition(600)
      .show(context);

   //you can add a callback to get frame times and the calculated
   //number of dropped frames within that window
   TinyDancer.create()
       .addFrameDataCallback(new FrameDataCallback() {
          @Override
          public void doFrame(long previousFrameNS, long currentFrameNS, int droppedFrames) {
             //collect your stats here
          }
        })
        .show(context);
  }
}
```

**You're good to go!** Tiny Dancer will show a small draggable view overlay with FPS as well as a color indicator of when FPS drop.  You can double tap the overlay to explicitly hide it.


See sample application that simulates excessive bind time:

![Tiny Dancer Sample](https://raw.githubusercontent.com/brianPlummer/TinyDancer/master/assets/tiny_dancer_011_example.gif "Tiny Dancer Sample")

Have an project with performance issues? We'd be happy to help tune it.  mike@friendlyrobot.nyc
