package com.codemonkeylabs.fpslibrary.sample;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by brianplummer on 8/30/15.
 */
public class FPSSampleAdpater extends RecyclerView.Adapter<FPSSampleViewHolder>
{

    private float megaBytes = 5;

    @Override
    public FPSSampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(
                parent.getContext()).inflate(R.layout.sample_item, parent, false);
        return new FPSSampleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FPSSampleViewHolder holder, int position)
    {
        holder.onBind(position, megaBytes);
    }

    @Override
    public int getItemCount()
    {
        return 255;
    }

    public void setMegaBytes(float megaBytes) {
        this.megaBytes = megaBytes;
    }
}
