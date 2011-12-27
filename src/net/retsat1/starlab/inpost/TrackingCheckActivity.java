package net.retsat1.starlab.inpost;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

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

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;


@ContentView(R.layout.main)
public class TrackingCheckActivity extends RoboActivity{
	@InjectView(R.id.buttonFind)
	private Button buttonFind;
	
	@InjectView(R.id.buttonScan)
	private Button buttonScan;
	
	@InjectView(R.id.editTextNumber)
	private EditText editTextNumber;
	
	@InjectView(R.id.webView)
	private WebView webView;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        buttonFind.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				final String numer_przesylki = editTextNumber.getText().toString();
				
				v.post(
						new Runnable() {
							
							public void run() {
								try {
									String execute = new HttpQuery().execute(numer_przesylki);
									
									String parse = new JSoupParser().parse(execute);
									
									webView.setVisibility(View.VISIBLE);
									webView.loadData(parse, "text/html", null);
								} catch (HttpRequestException e) {
								}
							}
						});
				
			}
		});
    }
}
