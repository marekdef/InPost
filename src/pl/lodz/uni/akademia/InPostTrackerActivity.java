package pl.lodz.uni.akademia;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

public class InPostTrackerActivity extends Activity {
	private EditText editTextTrackingNumber;
	private ImageButton imageButtonSearch;
	private ImageButton imageButtonScan;
	private ImageButton imageButtonClear;
	private ProgressBar progressBar;
	private android.webkit.WebView webViewResult;
	
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

	}

}
