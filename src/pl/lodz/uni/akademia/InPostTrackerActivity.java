package pl.lodz.uni.akademia;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.app.Activity;
import android.content.Intent;
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
	
	private IntentIntegrator intentIntegrator = new IntentIntegrator(this);

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
				String trackingNumber = editTextTrackingNumber.getText()
						.toString();
				sendQuery(trackingNumber);
			}
		});

		imageButtonScan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				intentIntegrator.initiateScan();
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

	private void sendQuery(final String trackingNumber) {
		if (trackingNumber == null || trackingNumber.length() == 0)
			return;
		
		toggleButtons(false);
		new Thread() {
			public void run() {
				final String result = httpParser.execute(trackingNumber);
				handler.post(new Runnable() {

					@Override
					public void run() {
						setTrackResult(result);
					}
				});

			};
		}.start();
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
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != Activity.RESULT_OK)
			return;
		
		IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
		
		if (result == null)
			return;
		
		sendQuery(result.getContents());
	}

}
