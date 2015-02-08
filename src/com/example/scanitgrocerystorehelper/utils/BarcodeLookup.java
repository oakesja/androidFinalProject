package com.example.scanitgrocerystorehelper.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.example.scanitgrocerystorehelper.DrawerActivity;
import com.example.scanitgrocerystorehelper.R;
import com.example.scanitgrocerystorehelper.dialogs.BarcodeDialog;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class BarcodeLookup extends AsyncTask<Object, Void, String> {
	private Context mContext;

	public BarcodeLookup(Context context) {
		mContext = context;
	}

	private BarcodeDialog dialog;
	private final String SEARCH_UPC_URL = "http://www.searchupc.com/handlers/upcsearch.ashx?request_type=3&access_token=";
	private final String SEARCH_UPC_KEY = "642FC14C-D4C2-46F3-91BB-784855F3DCCE";
	private final String OUTPAN_URL_GET = "http://www.outpan.com/api/get-product.php?barcode=";
	public static final String OUTPAN_KEY = "0fafc777e3692cf41bade0fb6c7926b9";
	private ProgressDialog pd;

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		pd = new ProgressDialog(mContext);
		pd.setTitle(R.string.progress_dialog_title);
		pd.setMessage(mContext.getString(R.string.progress_dialog_message));
		pd.show();
	}

	@Override
	protected String doInBackground(Object... params) {
		dialog = (BarcodeDialog) params[0];
		String code = (String) params[1];
		dialog.setBarcode(code);

		String outpanUrl = getOutpanLookupUrl(code);
		String outpanResult = getResponse(outpanUrl);
		Log.d(DrawerActivity.SCANIT, "outpan: " + outpanResult);
		String productName = parseOutpanResponse(outpanResult);
		if (productName.length() == 0) {
			String searchUPC = getSearchUpcUrl(code);
			String searchUPCResult = getResponse(searchUPC);
			Log.d(DrawerActivity.SCANIT, "searchupc: " + searchUPCResult);
			productName = parseSearchUpcResponse(searchUPCResult);
		}
		return productName;
	}

	@Override
	protected void onPostExecute(String productName) {
		pd.dismiss();
		dialog.setProductName(productName);
		dialog.show(((Activity) mContext).getFragmentManager(),
				"barcode lookup");
	}

	private String getSearchUpcUrl(String barcode) {
		return SEARCH_UPC_URL + SEARCH_UPC_KEY + "&upc=" + barcode;
	}

	private String getOutpanLookupUrl(String barcode) {
		return OUTPAN_URL_GET + barcode + "&apikey=" + OUTPAN_KEY;
	}

	private String getResponse(String url) {
		BufferedReader inStream = null;
		String result = "";
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
			result = buffer.toString();
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
		return result;
	}

	private String parseSearchUpcResponse(String response) {
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(response);
		String productname = "";
		if (element.isJsonObject()) {
			JsonObject results = element.getAsJsonObject();
			if (results.has("0")) {
				JsonObject first = results.getAsJsonObject("0");
				productname = first.get("productname").getAsString().trim();
			}
		}
		return productname;
	}

	private String parseOutpanResponse(String response) {
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(response);
		String productname = "";
		if (element.isJsonObject()) {
			JsonObject results = element.getAsJsonObject();
			if (results.has("name")) {
				JsonElement name = results.get("name");
				if (!name.isJsonNull()) {
					productname = name.getAsString().trim();
				}
			}
		}
		return productname;
	}
}
