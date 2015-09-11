package com.codemonkeylabs.fpslibrary;

import android.content.Context;

/**
 * Created by brianplummer on 8/29/15.
 */
public class FPSLibrary
{
    public static FPSBuilder create(){
        return new FPSBuilder();
    }

    public static void hide(Context context) {
        FPSBuilder.hide(context);
    }

}
