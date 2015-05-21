package net.retsat1.starlab.inpost;

import android.app.Activity;
import android.app.Application;

import net.retsat1.starlab.inpost.modules.ApplicationModule;
import net.retsat1.starlab.inpost.modules.DaggerServiceComponent;
import net.retsat1.starlab.inpost.modules.ServiceComponent;

/**
 * Created by marekdef on 18.05.15.
 */
public class InPostApplication extends Application {

    private ServiceComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        component = DaggerServiceComponent.builder().applicationModule(new ApplicationModule(this)).build();
    }

    public static ServiceComponent injector(Activity trackingCheckActivity) {
        return ((InPostApplication)trackingCheckActivity.getApplication()).component;
    }
}
