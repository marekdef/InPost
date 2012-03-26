package pl.lodz.atp.inpost.http;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import pl.lodz.atp.inpost.ProgressCallback;
import pl.lodz.atp.inpost.ResponseCallback;
import pl.lodz.atp.inpost.parser.HttpParser;

public class HttpQuery extends AsyncTask<String, Integer, AsyncHttpResponse<String>>
{

    private static final String PARAM_NUMER_PRZESYLKI = "numer_przesylki";
    private static final String TRACKING_URL = "http://www.inpost.pl/index.php?id=89";
    private static final String TAG = HttpQuery.class.getName();
    private ResponseCallback mCallback;

    public HttpQuery(ResponseCallback callback) {
        this.mCallback = callback;
    }

    private AsyncHttpResponse<String> fetchTrackingInfo( final String numerPrzesylki ) {
        AsyncHttpResponse<String> response = new AsyncHttpResponse<String>();
        try {
            String webPage = makeHttpRequest(numerPrzesylki);
            response = parseWebPage(webPage);
        } catch (IOException e) {
            Log.d(TAG, "Error when request and parse response for przesylka " + numerPrzesylki, e);
            response.setError(e);
        }
        return response;
    }

    private AsyncHttpResponse<String> parseWebPage( String webPage ) {
        AsyncHttpResponse<String> response = new AsyncHttpResponse<String>();
        HttpParser htmlParser = new HttpParser();
        String parsedResponse = htmlParser.parseHtml(webPage);
        response.setRespnse(parsedResponse);
        return response;
    }

    private String makeHttpRequest( final String numerPrzesylki ) throws IOException {
        HttpRequest request = new HttpRequest(new ProgressCallback() {

            @Override
            public void onProgress( int progress ) {
                publishProgress(progress);
            }
        });
        List<NameValuePair> nameValuePairs = createParams(numerPrzesylki);
        String webPage = request.extractPageAsString(TRACKING_URL, nameValuePairs);
        return webPage;
    }

    private List<NameValuePair> createParams( final String numerPrzesylki ) {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair(PARAM_NUMER_PRZESYLKI, numerPrzesylki));
        return nameValuePairs;
    }

    @Override
    protected AsyncHttpResponse<String> doInBackground( String... params ) {
        String numerPrzesylki = params[0];
        return fetchTrackingInfo(numerPrzesylki);
    }

    @Override
    protected void onProgressUpdate( Integer... values ) {
        mCallback.onProgress(values[0]);
    }

    @Override
    protected void onPostExecute( AsyncHttpResponse<String> result ) {
        if (result.isError()) {
            mCallback.onError(result.getError());
        } else {
            mCallback.onSuccess(result.getResponse());
        }
    }
}
