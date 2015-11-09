# Tiny Dancer


![Tiny Dancer](http://i.ytimg.com/vi/KBWfUc5jKiM/hqdefault.jpg "Tiny Dancer")

A tiny android library for displaying fps and percentage of time with two or more frames dropped

add to Gradle: TBD

Add to onCreate of Application Class:

`TinyDancer.create().show(getApplicationContext());`

And to hide the button programmatically you can:

`TinyDancer.hide((Application) getApplicationContext());`

We have included a sample application that simulates excessive bind time:

![Tiny Dancer Sample](http://i.imgur.com/iJxzr01.png "Tiny Dancer Sample")

