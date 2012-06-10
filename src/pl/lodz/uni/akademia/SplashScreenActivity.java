package pl.lodz.uni.akademia;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;

public class SplashScreenActivity extends Activity {
    private static final long SPLASH_DELAY = 3000;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        View splashImage = findViewById(R.id.imageViewSplash);
        splashImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				proceedToInPostTracker();
			}
		});
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	
    	new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				proceedToInPostTracker();
				
			}
		}, SPLASH_DELAY);
    }
    
    private void proceedToInPostTracker() {
    	Intent intent = new Intent(getApplicationContext(), InPostTrackerActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity(intent);
    }
}