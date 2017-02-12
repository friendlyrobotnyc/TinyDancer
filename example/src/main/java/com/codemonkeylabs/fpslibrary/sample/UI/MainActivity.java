package com.codemonkeylabs.fpslibrary.sample.UI;

import android.app.Application;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;

import com.codemonkeylabs.fpslibrary.TinyDancer;
import com.codemonkeylabs.fpslibrary.sample.AppComponent;
import com.codemonkeylabs.fpslibrary.sample.FPSApplication;
import com.codemonkeylabs.fpslibrary.sample.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    private AppComponent component;

    @Bind(R.id.recyclerView)
    FPSRecyclerView recyclerView;
    @Bind(R.id.loadIndicator)
    SeekBar loadIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        FPSApplication application = (FPSApplication) getApplication();
        component = application.getComponent();
        component.inject(this);
        ButterKnife.bind(this);

        setupRadioGroup();
        TinyDancer.create().show(getApplicationContext());
    }

    private void setupRadioGroup() {

        recyclerView.setMegaBytes(50f);
        recyclerView.notifyDataSetChanged();

        loadIndicator.setProgress(50);
        loadIndicator.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                recyclerView.setMegaBytes(Float.valueOf(i));
                recyclerView.notifyDataSetChanged();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

    }

    @OnClick(R.id.start)
    public void start() {
        TinyDancer.create().show(getApplicationContext());
    }

    @OnClick(R.id.stop)
    public void stop() {
        TinyDancer.hide((Application) getApplicationContext());
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        component=null;
        ButterKnife.unbind(this);
    }
}
