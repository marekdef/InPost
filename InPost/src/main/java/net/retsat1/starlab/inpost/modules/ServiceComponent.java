package net.retsat1.starlab.inpost.modules;

import net.retsat1.starlab.inpost.activities.TrackingCheckActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by marekdef on 18.05.15.
 */
@Component(modules = {ApplicationModule.class})
@Singleton
public interface ServiceComponent {
    void inject(TrackingCheckActivity trackingCheckActivity);
}
