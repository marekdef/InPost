package net.retsat1.starlab.inpost;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class TrackingCheckActivity extends ActionBarActivity {

    public static final String RESULT = "RESULT";

    public static final String ACTION_RESULT = "net.retsat1.starlab.inpost.result.OK";

    @InjectView(R.id.editTextNumber)
    protected EditText editTextNumber;

    @InjectView(R.id.webView)
    protected WebView webView;

    private IntentFilter mIntentFilter = new IntentFilter(ACTION_RESULT);

    private BroadcastReceiver mBroadcastReceiver;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        ButterKnife.inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(final Context context, final Intent intent) {
                if (ACTION_RESULT.equals(intent.getAction())) {
                    webView.setVisibility(View.VISIBLE);
                    final String parse = intent.getStringExtra(RESULT);

                    webView.loadDataWithBaseURL(null, parse, "text/html",
                                                "utf-8", null);
                }
            }
        };

        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver, mIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();

        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
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
        QueryService.startActionFoo(this, numer_przesylki);
    }

}
