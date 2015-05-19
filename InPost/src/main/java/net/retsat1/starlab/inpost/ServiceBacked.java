package net.retsat1.starlab.inpost;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import rx.Observable;
import rx.Observer;
import rx.subjects.ReplaySubject;
import rx.subjects.Subject;

/**
 * Created by marekdef on 20.04.15.
 */
public class ServiceBacked implements TrackingService, Observer<String> {
    private final Context context;
    private Subject<Result, Result > result = ReplaySubject.<Result>create();

    public ServiceBacked(Context applicationContext) {
        this.context = applicationContext.getApplicationContext();
    }

    public void checkTracking(final String numer_przesylki) {

        IntentFilter filter = new IntentFilter();
        filter.addAction(QueryService.ACTION_RESULT);
        filter.addAction(QueryService.ACTION_FAILURE);


        final BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(QueryService.ACTION_RESULT.equals(intent.getAction())) {
                    result.onNext(new TrackingService.Result(numer_przesylki, intent.getStringExtra(QueryService.RESULT)));
                }
                else if(QueryService.ACTION_FAILURE.equals(intent.getAction())) {
                    result.onError((Exception) intent.getSerializableExtra(QueryService.RESULT));
                }
                LocalBroadcastManager.getInstance(context).unregisterReceiver(this);
            }
        };

        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, filter);
        QueryService.startActionFoo(context, numer_przesylki);


    }

    @Override
    public Observable<Result> getObservable() {
        return result;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(String numer) {
        checkTracking(numer);
    }
}
