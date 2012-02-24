package net.retsat1.starlab.inpost;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import android.content.Intent;
import android.os.Bundle;

@ContentView(R.layout.splash)
public class SplashScreenActivity extends RoboActivity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, TrackingCheckActivity.class);
		this.startActivity(intent);
    }
}
