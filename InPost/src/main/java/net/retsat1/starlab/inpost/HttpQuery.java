package net.retsat1.starlab.inpost;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import net.retsat1.starlab.inpost.exceptions.HttpBadStatusCodeException;
import net.retsat1.starlab.inpost.exceptions.HttpRequestException;

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

public class HttpQuery {
	protected static final String TRACKING_URL = "http://www.inpost.pl/index.php?id=89";

	public String execute(final String numer_przesylki)
			throws HttpRequestException {
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
				throw new HttpBadStatusCodeException(
						statusLine.getReasonPhrase());
			}

			return extractPageAsString(response);

		} catch (ClientProtocolException e) {
			throw new HttpRequestException(e);
		} catch (IOException e) {
			throw new HttpRequestException(e);
		}

	}

	private String extractPageAsString(HttpResponse response)
			throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		response.getEntity().writeTo(out);
		out.close();
		String responseString = out.toString();
		return responseString;
	}
}
