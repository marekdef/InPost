package pl.lodz.atp.inpost;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

public class InPostTracking extends Activity {

	private EditText editTextTrackingNumber;
	private Button buttonFind;
	private Button buttonScan;
	private Button buttonClear;
	private ProgressBar progressBar;
	private android.webkit.WebView webViewResult;

	/* Please visit http://www.ryangmattison.com for updates */
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

			}
		});

		buttonScan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});

		buttonClear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});
	}

}
