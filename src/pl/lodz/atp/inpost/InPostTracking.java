package pl.lodz.atp.inpost;

import android.app.Activity;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class InPostTracking extends Activity {

	private static final String WEB_VIEW_RESULT_CONTENT = "webViewResult.content";
	private static final String WEB_VIEW_RESULT_VISIBILITY = "webViewResult.visibility";
	private static final String TEXT_EMPTY = "";
	private EditText editTextTrackingNumber;
	private Button buttonFind;
	private Button buttonScan;
	private Button buttonClear;
	private ProgressBar progressBar;
	private android.webkit.WebView webViewResult;

	private HttpParser parser = new HttpParser();
	private IntentIntegrator intentIntegrator = new IntentIntegrator(this);

	private String parsedInformation = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.track);

		editTextTrackingNumber = (EditText) findViewById(R.id.editTextTrackingNumber);
		buttonFind = (Button) findViewById(R.id.buttonFind);
		buttonScan = (Button) findViewById(R.id.buttonScan);
		buttonClear = (Button) findViewById(R.id.buttonClear);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		webViewResult = (android.webkit.WebView) findViewById(R.id.webViewResult);

		buttonFind.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String trackingNumber = editTextTrackingNumber.getText()
						.toString();
				sendQuery(trackingNumber);
			}
		});

		buttonScan.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				intentIntegrator.initiateScan();
			}
		});

		buttonClear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				editTextTrackingNumber.setText(TEXT_EMPTY);
				progressBar.setVisibility(View.GONE);
				webViewResult.setVisibility(View.GONE);
			}
		});
	}

	private void sendQuery(final String trackingNumber) {
		if (trackingNumber == null || trackingNumber.length() == 0)
			return;

		toggleButtons(false);

		final Handler handler = new Handler();

		new Thread() {
			public void run() {
				parsedInformation = parser.execute(trackingNumber);
				handler.post(new Runnable() {
					@Override
					public void run() {
						showResult(parsedInformation);

						toggleButtons(true);
					}

				});
			};
		}.start();

	}

	private void showResult(String parsedInformation) {
		webViewResult.loadDataWithBaseURL(null, parsedInformation, "text/html",
				"utf-8", null);
	}

	private void toggleButtons(boolean toggle) {
		buttonFind.setEnabled(toggle);
		buttonClear.setEnabled(toggle);
		buttonScan.setEnabled(toggle);
		editTextTrackingNumber.setEnabled(toggle);

		progressBar.setVisibility(toggle ? View.GONE : View.VISIBLE);
		webViewResult.setVisibility(toggle ? View.VISIBLE : View.GONE);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != Activity.RESULT_OK)
			return;

		IntentResult scanResult = IntentIntegrator.parseActivityResult(
				requestCode, resultCode, data);

		if (scanResult == null)
			return;

		String numer_przesylki = scanResult.getContents();
		editTextTrackingNumber.setText(numer_przesylki);
		sendQuery(numer_przesylki);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(WEB_VIEW_RESULT_VISIBILITY,
				webViewResult.getVisibility());
		outState.putString(WEB_VIEW_RESULT_CONTENT, parsedInformation);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

		parsedInformation = savedInstanceState
				.getString(WEB_VIEW_RESULT_CONTENT);
		int visibility = savedInstanceState.getInt(WEB_VIEW_RESULT_VISIBILITY,
				View.GONE);
		webViewResult.setVisibility(visibility);
		showResult(parsedInformation);
	}

}
