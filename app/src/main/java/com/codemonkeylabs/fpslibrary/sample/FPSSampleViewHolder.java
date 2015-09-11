package com.codemonkeylabs.fpslibrary.sample;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by brianplummer on 8/30/15.
 */
public class FPSSampleViewHolder extends RecyclerView.ViewHolder
{

    private long[] data;
    private ImageView colorImg;
    private TextView colorValue;

    public FPSSampleViewHolder(View itemView)
    {
        super(itemView);
        colorImg = (ImageView) itemView.findViewById(R.id.colorImg);
        colorValue = (TextView) itemView.findViewById(R.id.colorValue);
        data = new long[1024*10];
    }

    public void onBind(int value, float megaBytes) {

        int multiplier = 22;
        int hundred = value/100;
        int tens = (value- (hundred)*100)/10;
        int ones = value - (hundred*100) - (tens*10);
        int colorVal = Color.rgb(hundred*multiplier, tens*multiplier, ones*multiplier);
        colorImg.setImageDrawable(new ColorDrawable(colorVal));
        colorValue.setText("#" + hundred*multiplier + "" + tens*multiplier + "" + ones*multiplier);


        int total = (int)(megaBytes*100f);
        long time = System.currentTimeMillis();
        for (int i = 0; i < total; i++)
        {
            for (int e = 0; e < data.length; e++)
            {
                data[e] = time;
            }
        }
        long end = System.currentTimeMillis();
    }

}
