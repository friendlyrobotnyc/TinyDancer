package com.codemonkeylabs.fpslibrary.sample;

import com.codemonkeylabs.fpslibrary.sample.UI.MainActivity;
import com.codemonkeylabs.fpslibrary.sample.UI.FPSRecyclerView;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Component to be used by SampleActivity/Application
 */
@Singleton
@Component(
        modules = {AppModule.class}
)
public interface AppComponent {
    void inject(FPSApplication a);

    void inject(MainActivity a);
    void inject(FPSRecyclerView a);

}