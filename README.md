# Tiny Dancer

A real time frames per second measuring library for Android 

*“Perf Matters ”* - Random Guy at Meetup

![Tiny Dancer](http://i.ytimg.com/vi/KBWfUc5jKiM/hqdefault.jpg "Tiny Dancer")

## Getting started

In your `build.gradle`:

```gradle
 dependencies {
   debugCompile 'com.github.brianPlummer:tinydancer:0.0.1'
 }
```

In your `DebugApplication` class:

```java
public class DebugApplication extends Application {

  @Override public void onCreate() {
   TinyDancer.create()
             .show(this);
             
   //alternatively
   new TinyDancerBuilder()
      .redFlagPercentage(10)
      .startingGravity()
      .startingXPosition()
      .startingYPosition()
      .show(this);
  }
}
```

**You're good to go!** Tiny Dancer will show a small draggable view overlay with FPS as well as a color indicator of when FPS drop.





See sample application that simulates excessive bind time

![Tiny Dancer Sample](https://raw.githubusercontent.com/brianPlummer/TinyDancer/master/assets/tinydancer1.gif "Tiny Dancer Sample")

