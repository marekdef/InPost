package pl.lodz.atp.inpost;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

public class InPostTracking extends Activity {
	private HttpParser parser = new HttpParser();
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		final Button buttonClear = (Button) findViewById(R.id.buttonClear);
		final Button buttonFind = (Button) findViewById(R.id.buttonFind);
		final Button buttonScan = (Button) findViewById(R.id.buttonScan);
		final EditText editTextTrackingNumber = (EditText) findViewById(R.id.editTextTrackingNumber);
		final WebView webView = (WebView) findViewById(R.id.webViewStatus);
		final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);

		buttonFind.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final String trackingNumber = editTextTrackingNumber.getText().toString();
				progressBar.setVisibility(View.VISIBLE);
				webView.setVisibility(View.GONE);
				
				Handler handler = new Handler();
				handler.post(new Runnable() {
					@Override
					public void run() {
						try {
							String parsedInformation = parser.execute(trackingNumber);
							webView.loadDataWithBaseURL(null, parsedInformation, "text/html", "utf-8", null);
						}
						finally {
							progressBar.setVisibility(View.GONE);
							webView.setVisibility(View.VISIBLE);
						}
					}
				});
			}
		});

		buttonClear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			}
		});

		buttonScan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			}
		});
	}
}
