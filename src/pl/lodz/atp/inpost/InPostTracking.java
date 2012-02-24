package pl.lodz.atp.inpost;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

public class InPostTracking extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Button buttonClear = (Button) findViewById(R.id.buttonClear);
		Button buttonFind = (Button) findViewById(R.id.buttonFind);
		Button buttonScan = (Button) findViewById(R.id.buttonScan);
		EditText editTextTrackingNumber = (EditText) findViewById(R.id.editTextTrackingNumber);
		WebView webView = (WebView) findViewById(R.id.webViewStatus);
		ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);

		buttonFind.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		buttonClear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		buttonScan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
	}
}
