package com.codemonkeylabs.fpslibrary;

import android.content.Context;

public class TinyDancer {
    public static TinyDancerBuilder create() {
        return new TinyDancerBuilder();
    }

    public static void hide(Context context) {
      // no-op
    }
}
