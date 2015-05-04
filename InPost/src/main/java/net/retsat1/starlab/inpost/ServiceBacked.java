package net.retsat1.starlab.inpost;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

/**
 * Created by marekdef on 20.04.15.
 */
public class ServiceBacked implements TrackingService {
    private final Context context;

    public ServiceBacked(Context applicationContext) {
        this.context = applicationContext.getApplicationContext();
    }

    @Override
    public void checkTracking(String token, final Callback callback) {

        IntentFilter filter = new IntentFilter();
        filter.addAction(QueryService.ACTION_RESULT);
        filter.addAction(QueryService.ACTION_FAILURE);
        filter.addAction(QueryService.ACTION_TIMEOUT);


        final BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(QueryService.ACTION_RESULT.equals(intent.getAction())) {
                    callback.onResult(intent.getStringExtra(QueryService.RESULT));
                }
                else if(QueryService.ACTION_FAILURE.equals(intent.getAction())) {
                    callback.onError((Exception) intent.getSerializableExtra(QueryService.RESULT));
                }
                else if(QueryService.ACTION_TIMEOUT.equals(intent.getAction())) {
                    callback.onError((Exception) intent.getSerializableExtra(QueryService.RESULT));
                }
                LocalBroadcastManager.getInstance(context).unregisterReceiver(this);
            }
        };

        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, filter);
        QueryService.startActionFoo(context, token);


    }
}
