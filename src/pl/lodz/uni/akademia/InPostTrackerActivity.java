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
import android.widget.Button;
import android.widget.ProgressBar;

public class InPostTrackerActivity extends Activity {
	private static final String ENCODING = "utf-8";

	private static final String MIME_TYPE = "text/html";

	protected static final String EMPTY_TEXT = "";

	private static final String WEB_VIEW_RESULT_CONTENT = "webViewResult.content";
	private static final String WEB_VIEW_RESULT_VISIBILITY = "webViewResult.visibility";

	private EditText editTextTrackingNumber;
	private Button buttonSearch;
	private Button buttonScan;
	private Button buttonClear;
	private ProgressBar progressBar;
	private android.webkit.WebView webViewResult;

	private HttpParser httpParser = new HttpParser();

	private IntentIntegrator intentIntegrator = new IntentIntegrator(this);

	private Handler handler;

	private String savedTrackResultResult;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		handler = new Handler();

		setContentView(R.layout.track);

		editTextTrackingNumber = (EditText) findViewById(R.id.editTextTrackingNumber);
		buttonSearch = (Button) findViewById(R.id.buttonSearch);
		buttonScan = (Button) findViewById(R.id.buttonScan);
		buttonClear = (Button) findViewById(R.id.buttonClear);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		webViewResult = (android.webkit.WebView) findViewById(R.id.webViewResult);

		buttonSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String trackingNumber = editTextTrackingNumber.getText()
						.toString();
				sendQuery(trackingNumber);
			}
		});
		
		buttonSearch.post(new Runnable() {

			@Override
			public void run() {
				makeButtonSquare(buttonSearch);
			}
		});


		buttonScan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				intentIntegrator.initiateScan();
			}
		});
		
		buttonScan.post(new Runnable() {
			
			@Override
			public void run() {
				makeButtonSquare(buttonScan);
			}
		});

		buttonClear.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				editTextTrackingNumber.setText(EMPTY_TEXT);
				setTrackResult(null);
			}
		});
		
		buttonClear.post(new Runnable() {
			
			@Override
			public void run() {
				makeButtonSquare(buttonClear);
			}
		});
	}

	private void makeButtonSquare(Button button) {
		int width = button.getWidth();
		int height = button.getHeight();

		if (width == 0 || height == 0)
			return;

		button.setHeight(width > height ? width : height);
		button.setWidth(width > height ? width : height);
	}

	private void sendQuery(final String trackingNumber) {
		if (trackingNumber == null || trackingNumber.length() == 0)
			return;

		toggleButtons(false);
		new Thread() {
			public void run() {
				savedTrackResultResult = httpParser.parse(trackingNumber);
				handler.post(new Runnable() {

					@Override
					public void run() {
						setTrackResult(savedTrackResultResult);
					}
				});

			};
		}.start();
	}

	private void toggleButtons(boolean enabled) {
		editTextTrackingNumber.setEnabled(enabled);
		buttonClear.setEnabled(enabled);
		buttonScan.setEnabled(enabled);
		buttonSearch.setEnabled(enabled);

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

		IntentResult result = IntentIntegrator.parseActivityResult(requestCode,
				resultCode, data);

		if (result == null)
			return;

		sendQuery(result.getContents());
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(WEB_VIEW_RESULT_VISIBILITY,
				webViewResult.getVisibility());
		outState.putString(WEB_VIEW_RESULT_CONTENT, savedTrackResultResult);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		savedTrackResultResult = savedInstanceState
				.getString(WEB_VIEW_RESULT_CONTENT);
		int visibility = savedInstanceState.getInt(WEB_VIEW_RESULT_VISIBILITY,
				View.GONE);
		webViewResult.setVisibility(visibility);
		setTrackResult(savedTrackResultResult);
	}

}
