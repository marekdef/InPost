package pl.lodz.uni.akademia;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreenActivity extends Activity {
    private static final long SPLASH_DELAY = 2000;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				startActivity(new Intent(getApplicationContext(), InPostTrackerActivity.class));
			}
		}, SPLASH_DELAY);
    }
}