package com.example.scanitgrocerystorehelper.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.example.scanitgrocerystorehelper.DrawerActivity;

import android.os.AsyncTask;
import android.util.Log;

public class ShareProductNameTask extends AsyncTask<String, Void, Void> {

	private String url = "http://www.outpan.com/api/edit-name.php?";

	@Override
	protected Void doInBackground(String... params) {
		String barcode = params[0];
		String name = params[1];

		try {
			name = URLEncoder.encode(name, "UTF-8");
			url += "apikey=" + BarcodeLookup.OUTPAN_KEY + "&barcode=" + barcode
					+ "&name=" + name;
		} catch (Exception e) {
			Log.d(DrawerActivity.SCANIT, "could not create share url");
		}

		BufferedReader inStream = null;
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpRequest = new HttpGet(url);
			HttpResponse response = httpClient.execute(httpRequest);
			inStream = new BufferedReader(new InputStreamReader(response
					.getEntity().getContent()));

			StringBuffer buffer = new StringBuffer("");
			String line = "";
			String NL = System.getProperty("line.separator");
			while ((line = inStream.readLine()) != null) {
				buffer.append(line + NL);
			}
			inStream.close();
		} catch (Exception e) {
			Log.e(DrawerActivity.SCANIT, "Error with http request to " + url
					+ " : " + e);
		} finally {
			if (inStream != null) {
				try {
					inStream.close();
				} catch (IOException e) {
					Log.e(DrawerActivity.SCANIT,
							"Error with closing the stream to " + url + " : "
									+ e);
				}
			}
		}
		return null;
	}

}
