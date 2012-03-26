package pl.lodz.atp.inpost;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreenActivity extends Activity
{

    private static final int SPLASH_SCREEN_DISPLAY_TIME = 2000;

    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                startActivity(new Intent(SplashScreenActivity.this, InPostTracking.class));
            }
        }, SPLASH_SCREEN_DISPLAY_TIME);
    }
}