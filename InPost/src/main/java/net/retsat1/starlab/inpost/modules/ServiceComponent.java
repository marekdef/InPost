package net.retsat1.starlab.inpost.modules;

import net.retsat1.starlab.inpost.TrackingCheckActivity;
import net.retsat1.starlab.inpost.TrackingService;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by marekdef on 18.05.15.
 */
@Component(modules = {ApplicationModule.class, ServiceModule.class})
@Singleton
public interface ServiceComponent {
    TrackingService maker();
    void inject(TrackingCheckActivity trackingCheckActivity);
}