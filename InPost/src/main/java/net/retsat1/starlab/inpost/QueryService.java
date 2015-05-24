package net.retsat1.starlab.inpost;

import net.retsat1.starlab.inpost.exceptions.HttpBadStatusCodeException;
import net.retsat1.starlab.inpost.exceptions.HttpRequestException;
import net.retsat1.starlab.inpost.exceptions.JSoupParserException;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
public class QueryService extends IntentService {

    public static final String RESULT = "result";

    private HttpQuery httpQuery = new HttpQuery();

    private JSoupParser htmlParser = new JSoupParser();

    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    public static final String ACTION_QUERY = "net.retsat1.starlab.inpost.action.QUERY";

    public static final String ACTION_RESULT = "net.retsat1.starlab.inpost.action.RESULT";

    public static final String ACTION_FAILURE = "net.retsat1.starlab.inpost.action.FAILURE";

    public static final String ACTION_TIMEOUT = "net.retsat1.starlab.inpost.action.TIMEOUT";

    private static final String PARAM_TRACKING_NUMBER = "net.retsat1.starlab.inpost.param.TRACKING_NUMBER";

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionFoo(Context context, String param1) {
        Intent intent = new Intent(context, QueryService.class);
        intent.setAction(ACTION_QUERY);
        intent.putExtra(PARAM_TRACKING_NUMBER, param1);
        context.startService(intent);
    }

    public QueryService() {
        super("QueryService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent == null) {
            return;
        }
        final String action = intent.getAction();
        if (ACTION_QUERY.equals(action)) {
            final String param1 = intent.getStringExtra(PARAM_TRACKING_NUMBER);
            handleActionSendQuery(param1);
        }
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        int status = super.onStartCommand(intent, flags, startId);
        if (intent == null) {
            return status;
        }
        final String action = intent.getAction();
        return status;
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionSendQuery(String trackingNumber) {
        try {
            String execute = httpQuery.execute(trackingNumber);

            String parse = htmlParser.parse(execute);

            Intent intent = new Intent(ACTION_RESULT);
            intent.putExtra(RESULT, new TrackingService.Result(trackingNumber, parse));
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        } catch (JSoupParserException | HttpRequestException e) {
            Intent intent = new Intent(ACTION_FAILURE);
            intent.putExtra(RESULT, new TrackingService.Error(trackingNumber, e));
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }
    }
}

