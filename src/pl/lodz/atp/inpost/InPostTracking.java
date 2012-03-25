package pl.lodz.atp.inpost;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import pl.lodz.atp.inpost.http.HttpQuery;
import pl.lodz.atp.inpost.utils.Strings;

public class InPostTracking extends Activity implements ResponseCallback {

    private static final String WEB_VIEW_RESULT_CONTENT = "webViewResult.content";
    private static final String WEB_VIEW_RESULT_VISIBILITY = "webViewResult.visibility";
    private static final String TEXT_EMPTY = "";

    private static final String DEFAULT_WEBVIEW_ENCODING = "utf-8";
    private static final String DEFAULT_WEBVIEW_CONTENT = "text/html";
    
    private EditText mEditTextTrackingNumber;
    private Button mButtonFind;
    private Button mButtonScan;
    private Button mButtonClear;
    private ProgressBar mProgressBar;
    private android.webkit.WebView mWebViewResult;

    private String mParsedInformation = null;
    private IntentIntegrator intentIntegrator = new IntentIntegrator(this);

    /** Called when the activity is first created. */
    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.track);
        inflateView();
        mButtonFind.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick( View v ) {
                String trackingNumber = mEditTextTrackingNumber.getText().toString();
                sendQuery(trackingNumber);
            }
        });
        mButtonScan.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick( View v ) {
                intentIntegrator.initiateScan();
            }
        });
        mButtonClear.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick( View v ) {
                mEditTextTrackingNumber.setText(TEXT_EMPTY);
                mProgressBar.setVisibility(View.GONE);
                mWebViewResult.setVisibility(View.GONE);
            }
        });
    }

    private void inflateView() {
        mEditTextTrackingNumber = (EditText) findViewById(R.id.editTextTrackingNumber);
        mButtonFind = (Button) findViewById(R.id.buttonFind);
        mButtonScan = (Button) findViewById(R.id.buttonScan);
        mButtonClear = (Button) findViewById(R.id.buttonClear);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mWebViewResult = (android.webkit.WebView) findViewById(R.id.webViewResult);
    }

    private void sendQuery( final String trackingNumber ) {
        if (Strings.isEmpty(trackingNumber))
            return;
        toggleButtons(false);
        new HttpQuery(this).execute(trackingNumber);
    }

   
    private void toggleButtons( boolean toggle ) {
        mButtonFind.setEnabled(toggle);
        mButtonClear.setEnabled(toggle);
        mButtonScan.setEnabled(toggle);
        mEditTextTrackingNumber.setEnabled(toggle);
        mProgressBar.setVisibility(toggle ? View.GONE : View.VISIBLE);
        mWebViewResult.setVisibility(toggle ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data ) {
        if (resultCode != Activity.RESULT_OK)
            return;
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (scanResult == null)
            return;
        String numerPrzesylki = scanResult.getContents();
        mEditTextTrackingNumber.setText(numerPrzesylki);
        sendQuery(numerPrzesylki);
    }

    @Override
    protected void onSaveInstanceState( Bundle outState ) {
        super.onSaveInstanceState(outState);
        outState.putInt(WEB_VIEW_RESULT_VISIBILITY, mWebViewResult.getVisibility());
        outState.putString(WEB_VIEW_RESULT_CONTENT, mParsedInformation);
    }

    @Override
    protected void onRestoreInstanceState( Bundle savedInstanceState ) {
        super.onRestoreInstanceState(savedInstanceState);
        mParsedInformation = savedInstanceState.getString(WEB_VIEW_RESULT_CONTENT);
        int visibility = savedInstanceState.getInt(WEB_VIEW_RESULT_VISIBILITY, View.GONE);
        mWebViewResult.setVisibility(visibility);
        showResult(mParsedInformation);
    }

    @Override
    public void onSuccess( String result ) {
        mParsedInformation = result;
        showResult(result);
        toggleButtons(true);
    }

    private void showResult( String result ) {
        mProgressBar.setVisibility(View.GONE);
        mProgressBar.setIndeterminate(false);
        mWebViewResult.setVisibility(View.VISIBLE);
        mWebViewResult.loadDataWithBaseURL(null, result, DEFAULT_WEBVIEW_CONTENT, DEFAULT_WEBVIEW_ENCODING, null);
    }

    @Override
    public void onProgress( int progress ) {
        mProgressBar.setProgress(progress);
    }

    @Override
    public void onError( Exception e ) {
        toggleButtons(true);
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG);
    }
}
