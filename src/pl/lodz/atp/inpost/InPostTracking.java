package pl.lodz.atp.inpost;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class InPostTracking extends Activity {
	private HttpParser parser = new HttpParser();

	private Button buttonClear = null;
	private Button buttonFind = null;
	private Button buttonScan = null;
	private EditText editTextTrackingNumber = null;
	private WebView webView = null;
	private ProgressBar progressBar = null;

	private IntentIntegrator integrator = new IntentIntegrator(this);

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		buttonClear = (Button) findViewById(R.id.buttonClear);
		buttonFind = (Button) findViewById(R.id.buttonFind);
		buttonScan = (Button) findViewById(R.id.buttonScan);

		editTextTrackingNumber = (EditText) findViewById(R.id.editTextTrackingNumber);
		webView = (WebView) findViewById(R.id.webViewStatus);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);

		buttonFind.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final String trackingNumber = editTextTrackingNumber.getText()
						.toString();
				sendQuery(trackingNumber);
			}
		});

		buttonClear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				editTextTrackingNumber.setText("");
				webView.setVisibility(View.GONE);
				progressBar.setVisibility(View.GONE);
			}
		});

		buttonScan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				integrator.initiateScan();
			}
		});
	}

	private void sendQuery(final String trackingNumber) {
		progressBar.setVisibility(View.VISIBLE);
		progressBar.setIndeterminate(true);
		webView.setVisibility(View.GONE);

		final Handler handler = new Handler();

		new Thread() {
			public void run() {
				final String parsedInformation = parser.execute(trackingNumber);

				webView.loadDataWithBaseURL(null, parsedInformation,
						"text/html", "utf-8", null);

				handler.post(new Runnable() {
					@Override
					public void run() {
						webView.setVisibility(View.VISIBLE);
						progressBar.setVisibility(View.GONE);
					}
				});
			};
		}.start();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		IntentResult scanResult = IntentIntegrator.parseActivityResult(
				requestCode, resultCode, data);
		if (scanResult != null) {
			String numer_przesylki = scanResult.getContents();
			editTextTrackingNumber.setText(numer_przesylki);
			sendQuery(numer_przesylki);
		}
	}
}
