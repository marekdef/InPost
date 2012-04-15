package pl.lodz.uni.akademia;

import android.app.Activity;
import android.app.Instrumentation;
import android.app.Instrumentation.ActivityMonitor;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;

/**
 * This is a simple framework for a test of an Application.  See
 * {@link android.test.ApplicationTestCase ApplicationTestCase} for more information on
 * how to write and extend Application tests.
 * <p/>
 * To run this test, you can type:
 * adb shell am instrument -w \
 * -e class pl.lodz.uni.akademia.SplashScreenActivityTest \
 * pl.lodz.uni.akademia.tests/android.test.InstrumentationTestRunner
 */
public class SplashScreenActivityTest extends ActivityInstrumentationTestCase2<SplashScreenActivity> {

    public SplashScreenActivityTest() {
        super("pl.lodz.uni.akademia", SplashScreenActivity.class);
    }
    
    public void testSplash() {
    	getActivity();
    	Instrumentation instrumentation = getInstrumentation();
    	
    	ActivityMonitor monitor = instrumentation.addMonitor(InPostTrackerActivity.class.getName(), null, false);
		Activity waitForMonitorWithTimeout = instrumentation.waitForMonitorWithTimeout(monitor, 3000);
		
		assertNotNull(waitForMonitorWithTimeout);
    }
    
    public void testImageView() throws Exception {
    	SplashScreenActivity activity = getActivity();
    	
    	View imageView = activity.findViewById(R.id.imageViewSplash);
    	View rootView = imageView.getRootView();
    	
    	assertEquals(imageView.getWidth(), rootView.getWidth());
    }
    
    

}
