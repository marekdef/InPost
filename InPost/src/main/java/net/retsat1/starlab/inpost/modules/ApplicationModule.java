package net.retsat1.starlab.inpost.modules;

import android.content.Context;

import net.retsat1.starlab.inpost.InPostApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by marekdef on 18.05.15.
 */
@Module
public class ApplicationModule {
    private final InPostApplication application;

    public ApplicationModule(InPostApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Context providesApplicationContext() {
        return application;
    }
}
