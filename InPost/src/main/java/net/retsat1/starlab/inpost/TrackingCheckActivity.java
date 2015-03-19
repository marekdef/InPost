package net.retsat1.starlab.inpost;

import net.retsat1.starlab.inpost.exceptions.HttpRequestException;
import net.retsat1.starlab.inpost.exceptions.JSoupParserException;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class TrackingCheckActivity extends ActionBarActivity {
	@InjectView(R.id.buttonFind)
	protected Button buttonFind;

	@InjectView(R.id.buttonScan)
        protected Button buttonScan;

	@InjectView(R.id.editTextNumber)
        protected EditText editTextNumber;

	@InjectView(R.id.webView)
        protected WebView webView;
	
	@InjectView(R.id.buttonClear)
        protected Button buttonClear;

	private HttpQuery httpQuery = new HttpQuery();

	private JSoupParser htmlParser = new JSoupParser();

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

                setContentView(R.layout.main);

            ButterKnife.inject(this);

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
