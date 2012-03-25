package pl.lodz.atp.inpost.http;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import pl.lodz.atp.inpost.ProgressCallback;

public class HttpRequest {

    private static final int PROCENTAGE = 100;

    private static final int CONTENT_LENGHT_NOT_AVALIABLE = -1;

    private static final String TAG = HttpRequest.class.getName();
    
    private final ProgressCallback mCallback;

    public HttpRequest(ProgressCallback task) {
        this.mCallback = task;
    }

    private static final int IO_BUFFER_SIZE = 4 * 1024;
    private static final long DEFAULT_PAGE_LEN = 21*1024;
    /**
     * List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(); nameValuePairs.add(new
     * BasicNameValuePair(PARAM_NUMER_PRZESYLKI, numerPrzesylki));
     * @param url
     * @param nameValuePairs
     * @return
     * @throws IOException
     */
    public String extractPageAsString( String url, List<NameValuePair> nameValuePairs ) throws IOException {
        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response;
        HttpPost request = new HttpPost(URI.create(url));
        request.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        response = httpclient.execute(request);
        StatusLine statusLine = response.getStatusLine();
        Log.d(TAG, "Response from url = " + url + " status line " + statusLine);
        if (statusLine.getStatusCode() != HttpStatus.SC_OK) {
            // Closes the connection when status is not OK
            response.getEntity().getContent().close();
            return statusLine.getReasonPhrase();
        }
        return extractHttpResponseAsString(response);
    }
    
    

    private String extractHttpResponseAsString( HttpResponse response ) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        InputStream is = response.getEntity().getContent();
        long length = response.getEntity().getContentLength();
        if (length == CONTENT_LENGHT_NOT_AVALIABLE){
            length = DEFAULT_PAGE_LEN;
        }
        byte[] buffer = new byte[IO_BUFFER_SIZE];
        int read;
        long sum = 0;
        while ((read = is.read(buffer)) != -1) {
            out.write(buffer, 0, read);
            sum += read;
            int progress  = (int)((sum*PROCENTAGE)/length);
            Log.d(TAG, "progress of readding length " + length + " sum " + sum + " progress " + progress + "%");
            mCallback.onProgress(progress);
        }
        
        String responseString = out.toString();
        return responseString;
    }

    
        
    
}
