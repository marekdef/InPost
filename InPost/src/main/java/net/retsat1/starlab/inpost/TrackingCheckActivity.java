package net.retsat1.starlab.inpost;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.apache.commons.io.IOUtils;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import io.fabric.sdk.android.Fabric;

public class TrackingCheckActivity extends ActionBarActivity implements TrackingService.Callback {
    @InjectView(R.id.editTextNumber)
    protected EditText editTextNumber;

    @InjectView(R.id.webView)
    protected WebView webView;

    @InjectView(R.id.progressBar)
    protected ProgressBar progressBar;

    @InjectView(R.id.textViewHistory)
    protected TextView history;

    private BroadcastReceiver mBroadcastReceiver;

    private TrackingService trackingService;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());

        setContentView(R.layout.main);

        ButterKnife.inject(this);

        trackingService = new ServiceBacked(getApplicationContext());

        try {
            history.setText(Html.fromHtml(IOUtils.toString(getResources().openRawResource(R.raw.history))));
        } catch (IOException ignore) {

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @OnClick(R.id.buttonFind)
    void trackQuery() {
        final String numer_przesylki = editTextNumber.getText()
                                                     .toString();

        sendQuery(numer_przesylki);
    }

    @OnClick(R.id.buttonScan)
    void launchScan() {
        IntentIntegrator integrator = new IntentIntegrator(
                this);
        integrator.initiateScan();
    }

    @OnClick(R.id.buttonClear)
    void clearEntry() {
        editTextNumber.setText("");
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

    private void sendQuery(final String numer_przesylki) {
        progressBar.setVisibility(View.VISIBLE);
        webView.setVisibility(View.GONE);
        trackingService.checkTracking(numer_przesylki, this);
    }

    @Override
    public void onResult(String result) {
        progressBar.setVisibility(View.GONE);
        webView.setVisibility(View.VISIBLE);

        webView.loadDataWithBaseURL(null, result, "text/html",
                "utf-8", null);
    }

    @Override
    public void onError(Exception e) {
        progressBar.setVisibility(View.GONE);
        webView.setVisibility(View.VISIBLE);

        webView.loadDataWithBaseURL(null, e.getMessage(), "text/plain",
                "utf-8", null);
    }
}
