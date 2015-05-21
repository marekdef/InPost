package net.retsat1.starlab.inpost;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.view.ViewPropertyAnimator;

import org.apache.commons.io.IOUtils;

import java.io.IOException;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import io.fabric.sdk.android.Fabric;
import rx.functions.Action1;

public class TrackingCheckActivity extends ActionBarActivity {

    public static final String TEXT_HTML = "text/html";
    public static final String TEXT_PLAIN = "text/plain";

    public static final String ENCODING = "utf-8";

    @InjectView(R.id.editTextNumber)
    protected EditText editTextNumber;

    @InjectView(R.id.webView)
    protected WebView webView;

    @InjectView(R.id.progressBar)
    protected ProgressBar progressBar;

    @InjectView(R.id.textViewHistory)
    protected TextView history;

    @InjectView(R.id.scrollView)
    protected ScrollView scrollView;

    @Inject
    protected TrackingService trackingService;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        InPostApplication.injector(this).inject(this);


        Fabric.with(this, new Crashlytics());

        setContentView(R.layout.main);

        ButterKnife.inject(this);

        try {
            history.setText(Html.fromHtml(IOUtils.toString(getResources().openRawResource(R.raw.history))));
        } catch (IOException ignore) {

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        trackingService.getObservable().subscribe(new Action1<TrackingService.Result>() {
            @Override
            public void call(TrackingService.Result result) {
                onResult(result.result);
            }
        } , new Action1<Throwable>() {

            @Override
            public void call(Throwable throwable) {
                onError((Exception) throwable);
            }
        });
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
        if(TextUtils.isEmpty(numer_przesylki))
            return;

        final Animator.AnimatorListener animatorListener = new FadeNoClickAnimatorListener();
        ViewPropertyAnimator.animate(scrollView).alpha(0.0f).setDuration(3000).setListener(animatorListener).start();


        progressBar.setVisibility(View.VISIBLE);
        webView.setVisibility(View.GONE);
        trackingService.onNext(numer_przesylki);
    }

    public void onResult(String result) {
        progressBar.setVisibility(View.GONE);
        webView.setVisibility(View.VISIBLE);

        webView.loadDataWithBaseURL(null, result, TEXT_HTML,
                                    ENCODING, null);
    }

    public void onError(Exception e) {
        progressBar.setVisibility(View.GONE);
        webView.setVisibility(View.VISIBLE);

        webView.loadDataWithBaseURL(null, e.getMessage(), TEXT_PLAIN,
                ENCODING, null);
    }

    private class FadeNoClickAnimatorListener implements Animator.AnimatorListener {

        @Override
        public void onAnimationStart(Animator animation) {
            scrollView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });

        }

        @Override
        public void onAnimationEnd(Animator animation) {
            scrollView.setVisibility(View.GONE);
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    }
}
