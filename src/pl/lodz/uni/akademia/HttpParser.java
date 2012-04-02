package pl.lodz.uni.akademia;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class HttpParser {
	private static final String PARAM_NUMER_PRZESYLKI = "numer_przesylki";
	private static final String TRACKING_URL = "http://www.inpost.pl/index.php?id=89";

	public String execute(String numer_przesylki) {
		String inpostPage;
		try {
			inpostPage = extractPageAsString(numer_przesylki);
			return parseHtml(inpostPage);
		} catch (IOException e) {
			return e.toString();
		}
	}

	private String parseHtml(String inpostPage) {
		return "Brak informacji";
	}

	private String extractPageAsString(String numer_przesylki)
			throws IOException {
		HttpClient httpclient = new DefaultHttpClient();
		
		HttpPost request = new HttpPost(URI.create(TRACKING_URL));
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair(PARAM_NUMER_PRZESYLKI,
				numer_przesylki));
		request.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		HttpResponse response = httpclient.execute(request);
		StatusLine statusLine = response.getStatusLine();
		if (statusLine.getStatusCode() != HttpStatus.SC_OK) {
			// Closes the connection when status is not OK
			response.getEntity().getContent().close();
			return statusLine.getReasonPhrase();
		}
		return extractHttpResponseAsString(response);
	}

	private String extractHttpResponseAsString(HttpResponse response)
			throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		response.getEntity().writeTo(out);
		out.close();
		String responseString = out.toString();
		return responseString;
	}
}