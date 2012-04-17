package pl.lodz.uni.akademia;

import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.jayway.android.robotium.solo.Solo;

public class InPostTrackerActivityRobotiumTest extends
		ActivityInstrumentationTestCase2<InPostTrackerActivity> {

	private Solo solo;

	public InPostTrackerActivityRobotiumTest() {
		super("pl.lodz.uni.akademia", InPostTrackerActivity.class);
	}
	
	public void setUp() {
		solo = new Solo(getInstrumentation(), getActivity());
	}

	public void testValidFlow() {
		solo.assertCurrentActivity("Expected inpost tracker", InPostTrackerActivity.class);
		solo.clickOnButton("Clear");
		solo.enterText(0, "900000295100000916048230");
		solo.clickOnButton(0);
		solo.waitForView(ProgressBar.class, 0, 1000);
		solo.waitForView(WebView.class, 0, 1000);
		
		solo.clickOnButton("Clear");
		assertEquals(0, solo.getEditText(0).getText().length());
		
		assertEquals(View.GONE, solo.getCurrentProgressBars().get(0).getVisibility());
	}
	
	public void testEmptyString() {
		solo.assertCurrentActivity("Expected inpost tracker", InPostTrackerActivity.class);
		solo.clickOnButton("Clear");
		solo.clickOnButton("Search");
		
		assertEquals(View.GONE, solo.getCurrentProgressBars().get(0).getVisibility());
	}
}
