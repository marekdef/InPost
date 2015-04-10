package net.retsat1.starlab.inpost;

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

    private HttpQuery httpQuery = new HttpQuery();

    private JSoupParser htmlParser = new JSoupParser();

    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_QUERY = "net.retsat1.starlab.inpost.action.QUERY";

    // TODO: Rename parameters
    private static final String PARAM_TRACKING_NUMBER = "net.retsat1.starlab.inpost.param.TRACKING_NUMBER";

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
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
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_QUERY.equals(action)) {
                final String param1 = intent.getStringExtra(PARAM_TRACKING_NUMBER);
                handleActionFoo(param1);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String numer_przesylki) {
        try {
            String execute = httpQuery.execute(numer_przesylki);

            String parse = htmlParser.parse(execute);

            Intent intent = new Intent(TrackingCheckActivity.ACTION_RESULT);
            intent.putExtra(TrackingCheckActivity.RESULT, parse);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        } catch (JSoupParserException e) {

        } catch (HttpRequestException e) {

        }
    }
}
