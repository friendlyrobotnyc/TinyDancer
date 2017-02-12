package com.codemonkeylabs.fpslibrary.sample.UI;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.SystemClock;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.codemonkeylabs.fpslibrary.sample.R;

/**
 * Created by brianplummer on 8/30/15.
 */
public class FPSSampleViewHolder extends RecyclerView.ViewHolder
{

    private int[] data;
    private ImageView colorImg;
    private TextView bindTime;

    public FPSSampleViewHolder(View itemView)
    {
        super(itemView);
        colorImg = (ImageView) itemView.findViewById(R.id.colorImg);
        bindTime = (TextView) itemView.findViewById(R.id.bindTime);
        data = new int[1024*10];
    }

    public void onBind(int value, float megaBytes) {

        configureColor(value);

        int total = (int)(megaBytes * 100f);
        long start = SystemClock.elapsedRealtime();
        int startInt = (int) start;
        for (int i = 0; i < total; i++)
        {
            for (int e = 0; e < data.length; e++)
            {
                // set dummy value (start time)
                data[e] = startInt;
            }
        }
        long end = SystemClock.elapsedRealtime();
        long bindTimeMs = end - start;

        bindTime.setText(bindTimeMs + "ms onBind()");
    }

    private void configureColor(int value) {
        int multiplier = 22;
        int hundred = value/100;
        int tens = (value- (hundred)*100)/10;
        int ones = value - (hundred*100) - (tens*10);
        int r = hundred*multiplier;
        int g = tens*multiplier;
        int b = ones*multiplier;
        int colorVal = Color.rgb(r,g,b);
        colorImg.setImageDrawable(new ColorDrawable(colorVal));
    }
}
