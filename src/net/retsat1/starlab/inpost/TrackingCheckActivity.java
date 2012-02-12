package net.retsat1.starlab.inpost;

import net.retsat1.starlab.inpost.exceptions.HttpRequestException;
import net.retsat1.starlab.inpost.exceptions.JSoupParserException;

import com.google.inject.Inject;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

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

	@Inject
	private HttpQuery httpQuery;

	@Inject
	private JSoupParser htmlParser;

	private void sendQuery(final String numer_przesylki) {
		Handler handler = new Handler();

		handler.post(new Runnable() {
			public void run() {
				try {
					String execute = httpQuery.execute(numer_przesylki);

					String parse = htmlParser.parse(execute);
					webView.setVisibility(View.VISIBLE);
					webView.loadDataWithBaseURL(null, parse, "text/html",
							"utf-8", null);

				} catch (JSoupParserException e) {
					webView.setVisibility(View.GONE);
				} catch (HttpRequestException e) {
					webView.setVisibility(View.GONE);
				}
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
