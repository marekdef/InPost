package pl.lodz.uni.akademia;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import com.xtremelabs.robolectric.shadows.ShadowActivity;
import com.xtremelabs.robolectric.shadows.ShadowIntent;
import org.junit.Before;

import static com.xtremelabs.robolectric.Robolectric.clickOn;
import static com.xtremelabs.robolectric.Robolectric.shadowOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
public class InPostTrackerActivityTest {
	private InPostTrackerActivity inPostTrackerActivity;

	@Before
	public void setUp() {
		inPostTrackerActivity = new InPostTrackerActivity();
		inPostTrackerActivity.onCreate(null);
	}

	@Test
	public void testOnCreateBundle() {
		Button buttonClear = (Button) inPostTrackerActivity
				.findViewById(R.id.buttonClear);
		EditText editText = (EditText) inPostTrackerActivity
				.findViewById(R.id.editTextTrackingNumber);

		buttonClear.performClick();
		assertEquals("", editText.getText().toString());

	}

}
