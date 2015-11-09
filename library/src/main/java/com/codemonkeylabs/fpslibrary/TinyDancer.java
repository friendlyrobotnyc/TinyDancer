package com.codemonkeylabs.fpslibrary;

import android.app.Application;

/**
 * Created by brianplummer on 8/29/15.
 */
public class TinyDancer
{
    public static TinyDancerBuilder create(){
        return new TinyDancerBuilder();
    }

    public static void hide(Application context) {
        TinyDancerBuilder.hide(context);
    }

}
