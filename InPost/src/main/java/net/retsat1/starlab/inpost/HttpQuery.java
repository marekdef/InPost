package net.retsat1.starlab.inpost;

import com.google.common.io.Closeables;

import net.retsat1.starlab.inpost.exceptions.HttpBadStatusCodeException;
import net.retsat1.starlab.inpost.exceptions.HttpRequestException;

import org.apache.commons.io.IOUtils;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class HttpQuery {

    protected static final String TRACKING_URL = "https://inpost.pl/pl/pomoc/znajdz-przesylke?parcel=";

    public String execute(final String trackingNumber)
            throws HttpRequestException {
        HttpsURLConnection connection = null;
        try {
            URL url = new URL(TRACKING_URL + trackingNumber);
            HttpsURLConnection.setFollowRedirects(true);
            connection = (HttpsURLConnection) url.openConnection();

            int statusLine = connection.getResponseCode();
            if (statusLine != HttpURLConnection.HTTP_OK) {

                // Closes the connection when status is not OK
                throw new HttpBadStatusCodeException(String.valueOf(statusLine));
            }

            return extractPageAsString(connection.getInputStream());

        } catch (IOException e) {
            throw new HttpRequestException(e);
        } finally {
            if (connection != null)
                connection.disconnect();
        }

    }

    private String extractPageAsString(InputStream stream)
            throws IOException {
        String responseString = IOUtils.toString(stream);
        return responseString;
    }
}
