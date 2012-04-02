package pl.lodz.uni.akademia;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

public class InPostTrackerActivity extends Activity {
	private static final String ENCODING = "utf-8";

	private static final String MIME_TYPE = "text/html";

	protected static final String EMPTY_TEXT = "";

	private EditText editTextTrackingNumber;
	private ImageButton imageButtonSearch;
	private ImageButton imageButtonScan;
	private ImageButton imageButtonClear;
	private ProgressBar progressBar;
	private android.webkit.WebView webViewResult;

	private HttpParser httpParser = new HttpParser();

	private Handler handler;

	/* Please visit http://www.ryangmattison.com for updates */
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.track);

		editTextTrackingNumber = (EditText) findViewById(R.id.editTextTrackingNumber);
		imageButtonSearch = (ImageButton) findViewById(R.id.imageButtonSearch);
		imageButtonScan = (ImageButton) findViewById(R.id.imageButtonScan);
		imageButtonClear = (ImageButton) findViewById(R.id.imageButtonClear);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		webViewResult = (android.webkit.WebView) findViewById(R.id.webViewResult);

		imageButtonSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			}
		});

		imageButtonScan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			}
		});

		imageButtonClear.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				editTextTrackingNumber.setText(EMPTY_TEXT);
				setTrackResult(null);
			}
		});

		handler = new Handler();
	}

	private void toggleButtons(boolean enabled) {
		editTextTrackingNumber.setEnabled(enabled);
		imageButtonClear.setEnabled(enabled);
		imageButtonScan.setEnabled(enabled);
		imageButtonSearch.setEnabled(enabled);

		progressBar.setVisibility(enabled ? View.GONE : View.VISIBLE);
	}

	private void setTrackResult(final String result) {
		webViewResult.setVisibility(result != null ? View.VISIBLE : View.GONE);
		webViewResult.loadDataWithBaseURL(null, result, MIME_TYPE, ENCODING,
				null);

		toggleButtons(true);
	}

}
