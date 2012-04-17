package pl.lodz.uni.akademia;

import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.jayway.android.robotium.solo.Solo;

public class SplashScreenActivityRobotiumTest extends
		ActivityInstrumentationTestCase2<SplashScreenActivity> {

	private Solo solo;

	public SplashScreenActivityRobotiumTest() {
		super("pl.lodz.uni.akademia", SplashScreenActivity.class);
	}
	
	public void setUp() {
		solo = new Solo(getInstrumentation(), getActivity());
	}

	public void testTransits() {
		solo.assertCurrentActivity("Expeceted splash", SplashScreenActivity.class);
		
		assertTrue(solo.waitForActivity("InPostTrackerActivity", 3000));
	}
}
