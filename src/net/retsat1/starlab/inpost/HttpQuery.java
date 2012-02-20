package net.retsat1.starlab.inpost;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import net.retsat1.starlab.inpost.exceptions.JSoupParserException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.google.inject.Inject;

import android.os.AsyncTask;
import android.view.View;

public class HttpQuery extends AsyncTask<String, String, String>{
	protected static final String TRACKING_URL = "http://www.inpost.pl/index.php?id=89";
	private TrackingCheckActivity trackingCheckActivity;
	
	@Inject
	private JSoupParser htmlParser;

	public HttpQuery(TrackingCheckActivity trackingCheckActivity) {
		this.trackingCheckActivity = trackingCheckActivity;
	}

	public String execute(final String numer_przesylki)
			{
		HttpClient httpclient = new DefaultHttpClient();
		HttpResponse response;
		try {
			HttpPost request = new HttpPost(URI.create(TRACKING_URL));
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("numer_przesylki", numer_przesylki));
			request.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			response = httpclient.execute(request);
			StatusLine statusLine = response.getStatusLine();
			if (statusLine.getStatusCode() != HttpStatus.SC_OK) {
				// Closes the connection when status is not OK
				response.getEntity().getContent().close();
				return statusLine.getReasonPhrase();
			}
			
			String webPage = extractPageAsString(response);
			String parse = htmlParser.parse(webPage);
			return parse;

		} catch (ClientProtocolException e) {
			return e.toString();
		} catch (IOException e) {
			return e.toString();
		} catch (JSoupParserException e) {
			return e.toString();
		}

	}

	private String extractPageAsString(final HttpResponse response)
			throws IOException {
		
		final long contentLength = response.getEntity().getContentLength();
		ByteArrayOutputStream out = new ByteArrayOutputStream((int)contentLength) {
			@Override
			public void write(byte[] buffer) throws IOException {
				super.write(buffer);
				publish();
			}
			
			@Override
			public synchronized void write(int oneByte) {
				super.write(oneByte);
				publish();
			}
			
			@Override
			public synchronized void write(byte[] buffer, int offset, int len) {
				super.write(buffer, offset, len);
				publish();
			}
			
			public void publish() {
				HttpQuery.this.publishProgress(String.format("Read %d/%l bytes", this.size(), contentLength));
			}
		};
		response.getEntity().writeTo(out);
		out.close();
		String responseString = out.toString();
		return responseString;
	}

	@Override
	protected String doInBackground(String... params) {
		String numer_przesylki = params[0];
		return this.execute(numer_przesylki);
	}
	
	@Override
	protected void onProgressUpdate(String... values) {
		trackingCheckActivity.textViewProgress.setText(values[0]);
	}
	
	@Override
	protected void onPreExecute() {
		trackingCheckActivity.progress.setVisibility(View.VISIBLE);
		trackingCheckActivity.progressBar.setIndeterminate(true);
	}
	
	@Override
	protected void onPostExecute(String result) {
		trackingCheckActivity.progress.setVisibility(View.GONE);
		trackingCheckActivity.progressBar.setIndeterminate(false);
	}
}
