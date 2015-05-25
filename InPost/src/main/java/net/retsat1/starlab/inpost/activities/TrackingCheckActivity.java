package net.retsat1.starlab.inpost.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.view.ViewPropertyAnimator;

import net.retsat1.starlab.inpost.InPostApplication;
import net.retsat1.starlab.inpost.R;
import net.retsat1.starlab.inpost.TrackingService;
import net.retsat1.starlab.inpost.exceptions.HttpBadStatusCodeException;
import net.retsat1.starlab.inpost.exceptions.HttpRequestException;
import net.retsat1.starlab.inpost.exceptions.JSoupParserException;
import net.retsat1.starlab.inpost.exceptions.TimeoutException;
import net.retsat1.starlab.inpost.fragments.ResultFragment;

import org.apache.commons.io.IOUtils;

import java.io.IOException;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import io.fabric.sdk.android.Fabric;
import rx.Subscription;
import rx.functions.Action1;

import static rx.android.schedulers.AndroidSchedulers.mainThread;

public class TrackingCheckActivity extends ActionBarActivity {
    private static final String TAG = TrackingCheckActivity.class.getSimpleName();

    public static final int FADE_DURATION = 3000;

    public static final String WAS_HTML_INTRO_SHOWN = "was_html_intro_shown";

    public static final int TRANSLATE_BY = 50;

    public static final float FULLY_TRANSPARENT = 0.0f;


    @InjectView(R.id.editTextNumber)
    protected EditText editTextNumber;

    @InjectView(R.id.progressBar)
    protected ProgressBar progressBar;

    @InjectView(R.id.textViewHistory)
    protected TextView history;

    @InjectView(R.id.scrollView)
    protected ScrollView scrollView;

    @InjectView(R.id.buttonFind)
    protected Button buttonFind;

    @InjectView(R.id.buttonScan)
    protected Button buttonScan;

    @InjectView(R.id.buttonClear)
    protected Button buttonClear;


    @Inject
    protected TrackingService trackingService;

    private Subscription mResultSubscription;
    private Subscription mErrorSubscription;

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
        mResultSubscription = trackingService.getResultStream().subscribeOn(mainThread()).subscribe(new Action1<TrackingService.Result>() {
            @Override
            public void call(TrackingService.Result result) {
                onResult(result.number, result.result);
            }
        });

        mErrorSubscription = trackingService.getErrorStream().subscribeOn(mainThread()).subscribe(new Action1<TrackingService.Error>() {
            @Override
            public void call(TrackingService.Error error) {
                onError(error.number, error.throwable);
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
        mResultSubscription.unsubscribe();
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
        ViewPropertyAnimator.animate(scrollView).alpha(FULLY_TRANSPARENT).translationYBy(TRANSLATE_BY).setDuration(FADE_DURATION).setListener(animatorListener).start();

        toggleInputReady(false);
        trackingService.sendQuery(numer_przesylki);
    }

    public void onResult(String trackingNumber, String trackingResult) {
        toggleInputReady(true);

        getSupportFragmentManager().beginTransaction().add(R.id.resultContainer, ResultFragment.newInstance(trackingNumber, trackingResult)).commit();
    }

    public void onError(String trackingNumber, Exception e) {
        toggleInputReady(true);

        Toast.makeText(this, getMessage(e), Toast.LENGTH_LONG).show();
    }

    private void toggleInputReady(boolean enabled) {
        progressBar.setVisibility(enabled ? View.GONE : View.VISIBLE);
        buttonFind.setEnabled(enabled);
        buttonScan.setEnabled(enabled);
        buttonClear.setEnabled(enabled);
        editTextNumber.setEnabled(enabled);
    }

    private String getMessage(Exception e) {
        final Class<? extends Exception> exceptionClass = e.getClass();
        if (exceptionClass.equals(JSoupParserException.class)) {
            return getString(R.string.parse_error);
        }
        if (exceptionClass.equals(HttpBadStatusCodeException.class)) {
            return getString(R.string.http_status_error, getSafeCause(e));
        }
        if (exceptionClass.equals(HttpRequestException.class)) {
            return getString(R.string.http_response_error, getSafeCause(e));
        }
        if (exceptionClass.equals(TimeoutException.class)) {
            return getString(R.string.http_response_timeout);
        }
        return "";
    }

    private Object getSafeCause(Exception e) {
        Throwable cause = e.getCause();
        if(cause == null)
            return "";
        String localizedMessage = e.getCause().getLocalizedMessage();
        if(localizedMessage == null)
            return "";
        return localizedMessage;
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
