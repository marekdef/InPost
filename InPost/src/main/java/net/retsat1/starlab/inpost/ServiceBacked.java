package net.retsat1.starlab.inpost;

import net.retsat1.starlab.inpost.TrackingService.*;
import net.retsat1.starlab.inpost.exceptions.TimeoutException;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

import static net.retsat1.starlab.inpost.QueryService.ACTION_FAILURE;
import static net.retsat1.starlab.inpost.QueryService.ACTION_RESULT;
import static net.retsat1.starlab.inpost.QueryService.ACTION_TIMEOUT;
import static net.retsat1.starlab.inpost.QueryService.RESULT;

/**
 * Created by marekdef on 20.04.15.
 */
public class ServiceBacked implements TrackingService {

    public static final String TRACKING_RESULT = "resultsStream";

    public static final String TRACKING_NUMBER = "tracking_result";

    public static final String SHARED_PREFERENCE_NAME = "trackings";

    private static final long TIMEOUT = 10_000;

    private final Context context;

    private final SharedPreferences sharedPreferences;

    private final Handler handler;

    private Subject resultsStream = PublishSubject.<Result>create();

    private Subject errorsStream = PublishSubject.<Error>create();

    public ServiceBacked(Context applicationContext) {
        this.context = applicationContext.getApplicationContext();
        this.sharedPreferences = applicationContext.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        handler = new Handler();
        
        loadResult(this.sharedPreferences);
    }

    private void loadResult(SharedPreferences sharedPreferences) {
        final String trackingNumber = sharedPreferences.getString(TRACKING_NUMBER, null);
        final String result = sharedPreferences.getString(TRACKING_RESULT, null);

        if (trackingNumber != null && result != null) {
            this.resultsStream.onNext(new TrackingService.Result(trackingNumber, result));
        }
    }

    public void sendQuery(final String trackingNumber) {



        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_RESULT);
        filter.addAction(ACTION_FAILURE);
        filter.addAction(ACTION_TIMEOUT);

        final BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final String action = intent.getAction();
                if (ACTION_RESULT.equals(action)) {
                    final TrackingService.Result result = (TrackingService.Result) intent.getSerializableExtra
                            (RESULT);

                    resultsStream.onNext(result);
                    saveResult(result);
                } else if (ACTION_TIMEOUT.equals(action) || ACTION_FAILURE.equals(action)) {
                    final Error error = (Error) intent.getSerializableExtra
                            (RESULT);
                    errorsStream.onNext(error);
                    return;//skip unregistering for timeout we still might get an answer
                }
                LocalBroadcastManager.getInstance(context).unregisterReceiver(this);
            }
        };

        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, filter);
        
        
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent timeoutIntent = new Intent(ACTION_TIMEOUT);
                timeoutIntent.putExtra(RESULT, new TrackingService.Error(trackingNumber, new TimeoutException()));
                LocalBroadcastManager.getInstance(ServiceBacked.this.context).sendBroadcast(timeoutIntent);
            }
        }, TIMEOUT);
        QueryService.startActionFoo(context, trackingNumber);
    }

    private void saveResult(TrackingService.Result result) {
        ServiceBacked.this.sharedPreferences.edit()
                                            .putString(TRACKING_RESULT, result.result)
                                            .putString(TRACKING_NUMBER, result.number)
                                            .apply();
    }

    @Override
    public Observable<Result> getResultStream() {
        return resultsStream;
    }

    @Override
    public Observable<Error> getErrorStream() {
        return errorsStream;
    }
}
