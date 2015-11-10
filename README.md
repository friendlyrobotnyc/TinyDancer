# Tiny Dancer

A real time frames per second measuring library for Android 

*“Perf Matters ”* - Random Guy at Meetup

![Tiny Dancer](http://i.ytimg.com/vi/KBWfUc5jKiM/hqdefault.jpg "Tiny Dancer")

## Getting started

In your `build.gradle`:

```gradle
 dependencies {
   compile 'com.github.brianPlummer:tinydancer:0.0.1'
 }
```

In your `Application` class:

```java
public class ExampleApplication extends Application {

  @Override public void onCreate() {
   TinyDancer.create()
            //TODO:  other builder things 
             .show(getApplicationContext());
  }
}
```

**You're good to go!** Tiny Dancer will show a small draggable view overlay with FPS as well as a color indicator of when FPS drop.





See sample application that simulates excessive bind time

![Tiny Dancer Sample](http://i.imgur.com/iJxzr01.png "Tiny Dancer Sample")

