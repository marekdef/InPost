package net.retsat1.starlab.inpost.activities;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import com.crashlytics.android.Crashlytics;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.view.ViewPropertyAnimator;

import net.retsat1.starlab.inpost.InPostApplication;
import net.retsat1.starlab.inpost.R;
import net.retsat1.starlab.inpost.TrackingService;
import net.retsat1.starlab.inpost.fragments.ResultFragment;

import org.apache.commons.io.IOUtils;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.IOException;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import io.fabric.sdk.android.Fabric;
import rx.Subscription;
import rx.functions.Action1;

public class TrackingCheckActivity extends ActionBarActivity {

    public static final String TEXT_HTML = "text/html";

    public static final String TEXT_PLAIN = "text/plain";

    public static final String ENCODING = "utf-8";

    private static final String TAG = TrackingCheckActivity.class.getSimpleName();

    public static final int FADE_DURATION = 3000;

    public static final String WAS_HTML_INTRO_SHOWN = "was_html_intro_shown";


    @InjectView(R.id.editTextNumber)
    protected EditText editTextNumber;

    @InjectView(R.id.progressBar)
    protected ProgressBar progressBar;

    @InjectView(R.id.textViewHistory)
    protected TextView history;

    @InjectView(R.id.scrollView)
    protected ScrollView scrollView;

    @Inject
    protected TrackingService trackingService;

    private Subscription subscription;

    private boolean needDisplayHtmlIntroForThisSession = true;

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
        getSupportActionBar().setLogo(R.drawable.ic_launcher);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if(needDisplayHtmlIntroForThisSession) {
            displayHtmlIntro();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        subscription = trackingService.getObservable().subscribe(new Action1<TrackingService.Result>() {
            @Override
            public void call(TrackingService.Result result) {
                onResult(result.number, result.result);
            }
        }, new Action1<Throwable>() {

            @Override
            public void call(Throwable throwable) {
                onError("TODO", (Exception) throwable);
            }
        });
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        needDisplayHtmlIntroForThisSession = !savedInstanceState.getBoolean(WAS_HTML_INTRO_SHOWN, false);
    }



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(WAS_HTML_INTRO_SHOWN, true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        subscription.unsubscribe();
    }

    @OnClick(R.id.buttonFind)
    void trackQuery() {
        final String trackingNumber = editTextNumber.getText()
                                                    .toString();

        sendQuery(trackingNumber);
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

    private void displayHtmlIntro() {
        try {
            history.setText(Html.fromHtml(IOUtils.toString(getResources().openRawResource(R.raw.history))));

        } catch (IOException ignore) {
            Log.e(TAG, "Could not set history", ignore);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(
                requestCode, resultCode, intent);
        if (scanResult != null) {
            String trackingNumber = scanResult.getContents();
            editTextNumber.setText(trackingNumber);
            sendQuery(trackingNumber);
        }
    }

    private void sendQuery(final String numer_przesylki) {
        if (TextUtils.isEmpty(numer_przesylki)) {
            return;
        }

        final Animator.AnimatorListener animatorListener = new FadeNoClickAnimatorListener();
        ViewPropertyAnimator.animate(scrollView).alpha(0.0f).translationYBy(50).setDuration(FADE_DURATION).setListener(animatorListener).start();

        progressBar.setVisibility(View.VISIBLE);
        trackingService.sendQuery(numer_przesylki);
    }

    public void onResult(String trackingNumber, String trackingResult) {
        progressBar.setVisibility(View.GONE);

        getSupportFragmentManager().beginTransaction().add(R.id.resultContainer, ResultFragment.newInstance(trackingNumber, trackingResult)).commit();
    }

    public void onError(String trackingNumber, Exception e) {
        progressBar.setVisibility(View.GONE);
        onResult(trackingNumber, e.getMessage());
    }

    private class FadeNoClickAnimatorListener implements Animator.AnimatorListener {

        @Override
        public void onAnimationStart(Animator animation) {
            scrollView.setOnTouchListener(new DisablingTouchListener());

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

        private class DisablingTouchListener implements View.OnTouchListener {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        }
    }
}
