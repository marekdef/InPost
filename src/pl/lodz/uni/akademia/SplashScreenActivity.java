package pl.lodz.uni.akademia;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class SplashScreenActivity extends Activity {
    private static final long SPLASH_DELAY = 2000;
	private View imageView;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        imageView = findViewById(R.id.imageViewSplash);
        
        imageView.postDelayed(new Runnable() {
			@Override
			public void run() {
//				goToTrackerActivity();
			}
		}, SPLASH_DELAY);
        
        imageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//goToTrackerActivity();
			}
		});
    }
    
    private void goToTrackerActivity() {
		startActivity(new Intent(getApplicationContext(), InPostTrackerActivity.class));
	}
}
