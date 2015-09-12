package com.codemonkeylabs.fpslibrary.sample.UI;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.codemonkeylabs.fpslibrary.sample.FPSApplication;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * Created by 206847 on 9/12/15.
 */
public class FPSRecyclerView extends RecyclerView {
    @Inject
    FPSSampleAdpater adapter;

    public FPSRecyclerView(Context context) {
        this(context, null);
    }

    public FPSRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        setLayoutManager(layoutManager);
        addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
        setAdapter(adapter);

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ButterKnife.unbind(this);
    }

    public FPSRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        ((FPSApplication) (context).getApplicationContext()).getComponent().inject(this);
    }


    public void setMegaBytes(Float megaBytes) {
        adapter.setMegaBytes(megaBytes);
    }

    public void notifyDataSetChanged() {
        adapter.notifyDataSetChanged();
    }
}
