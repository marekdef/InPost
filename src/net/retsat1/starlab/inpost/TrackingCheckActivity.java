package net.retsat1.starlab.inpost;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

@ContentView(R.layout.main)
public class TrackingCheckActivity extends RoboActivity {
	@InjectView(R.id.buttonFind)
	private Button buttonFind;

	@InjectView(R.id.buttonScan)
	private Button buttonScan;

	@InjectView(R.id.editTextNumber)
	private EditText editTextNumber;

	@InjectView(R.id.webView)
	private WebView webView;

	@InjectView(R.id.buttonClear)
	private Button buttonClear;

	@InjectView(R.id.progress)
	public LinearLayout progress;

	@InjectView(R.id.progressBar)
	public ProgressBar progressBar;

	@InjectView(R.id.textProgress)
	public TextView textViewProgress;

	private void sendQuery(final String numer_przesylki) {
		Handler handler = new Handler();

		handler.post(new Runnable() {
			public void run() {
				String parse = new HttpQuery(TrackingCheckActivity.this)
						.execute(numer_przesylki);
				
				webView.setVisibility(View.VISIBLE);
				webView.loadDataWithBaseURL(null, parse, "text/html", "utf-8",
						null);
			}
		});
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		buttonFind.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				final String numer_przesylki = editTextNumber.getText()
						.toString();

				sendQuery(numer_przesylki);

			}
		});

		buttonScan.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				IntentIntegrator integrator = new IntentIntegrator(
						TrackingCheckActivity.this);
				integrator.initiateScan();
			}
		});

		buttonClear.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				editTextNumber.setText("");
			}
		});
	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		IntentResult scanResult = IntentIntegrator.parseActivityResult(
				requestCode, resultCode, intent);
		if (scanResult != null) {
			String numer_przesylki = scanResult.getContents();
			editTextNumber.setText(numer_przesylki);
			sendQuery(numer_przesylki);
		}
	}

}
