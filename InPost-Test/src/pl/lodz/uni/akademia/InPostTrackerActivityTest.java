package pl.lodz.uni.akademia;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class InPostTrackerActivityTest extends
		ActivityInstrumentationTestCase2<InPostTrackerActivity> {

	private static final int X = 0;
	private static final int Y = 1;
	private InPostTrackerActivity activity;
	private Instrumentation instrumentation;
	private Button buttonScan;
	private Button buttonSearch;
	private Button buttonClear;
	private View progressBar;
	private View webViewResult;
	private TextView textView;

	public InPostTrackerActivityTest() {
		super("pl.lodz.uni.akademia", InPostTrackerActivity.class);
	}

	public void testButtonsAligned() {
		InPostTrackerActivity activity = getActivity();
		getInstrumentation().waitForIdleSync();
		
		
		int[] locationClear = new int[2];
		int[] locationSearch = new int[2];
		int[] locationScan = new int[2];
		buttonClear.getLocationInWindow(locationClear);
		buttonSearch.getLocationInWindow(locationSearch);
		buttonScan.getLocationInWindow(locationScan);
		
		assertTrue(locationClear[X] > locationScan[X]);
		assertTrue(locationScan[X] > locationSearch[X]);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		activity = getActivity();
		instrumentation = getInstrumentation();
		instrumentation.waitForIdleSync();

		buttonClear = (Button) activity.findViewById(R.id.buttonClear);
		buttonSearch = (Button) activity.findViewById(R.id.buttonSearch);
		buttonScan = (Button) activity.findViewById(R.id.buttonScan);

		progressBar = activity.findViewById(R.id.progressBar);
		webViewResult = activity.findViewById(R.id.webViewResult);

		textView = (TextView)activity.findViewById(R.id.editTextTrackingNumber);
	}
	
	public void testProgressAndResultNotShown() {
		assertEquals(View.GONE, progressBar.getVisibility());
		assertEquals(View.GONE, webViewResult.getVisibility());
	}
	
	public void testButtonClear() {
		instrumentation.runOnMainSync(new Runnable() {
			
			@Override
			public void run() {
				buttonClear.performClick();
			}
		});
		instrumentation.waitForIdleSync();
		
		assertEquals(0, textView.getText().length());
	}

}
