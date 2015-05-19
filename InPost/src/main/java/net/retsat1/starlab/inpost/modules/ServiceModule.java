package net.retsat1.starlab.inpost.modules;

import android.content.Context;

import net.retsat1.starlab.inpost.ServiceBacked;
import net.retsat1.starlab.inpost.TrackingService;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by marekdef on 18.05.15.
 */
@Module
public class ServiceModule {
    @Provides
    @Singleton
    @Inject
    TrackingService providesTrackingService(Context context) {
        return new ServiceBacked(context);
    }
}
