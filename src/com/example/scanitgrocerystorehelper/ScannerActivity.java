package com.example.scanitgrocerystorehelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ScannerActivity extends DrawerActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scanner);
		IntentIntegrator it = new IntentIntegrator(this);
		it.initiateScan();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		IntentResult scanResult = IntentIntegrator.parseActivityResult(
				requestCode, resultCode, data);
		if (scanResult != null) {
			Log.d(DrawerActivity.SCANIT, scanResult.getContents());
			new BarcodeLookup().execute(new EnsureLookupDialog(),
					scanResult.getContents());
		}
	}

	private class EnsureLookupDialog extends DialogFragment {
		private EditText editText;
		private String productName;

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					ScannerActivity.this);
			View v = getLayoutInflater().inflate(R.layout.dialog_ensure_lookup,
					null);
			editText = (EditText) v.findViewById(R.id.editBarcodeLookupName);
			if (productName != "") {
				editText.setText(productName);
			}
			builder.setView(v);
			return builder.create();
		}

		public void setProductName(String productName) {
			Log.d(DrawerActivity.SCANIT, productName);
			this.productName = productName;
		}
	}

	private class BarcodeLookup extends AsyncTask<Object, Void, String> {
		private EnsureLookupDialog dialog;
		private final String SEARCH_UPC_URL = "http://www.searchupc.com/handlers/upcsearch.ashx?request_type=3&access_token=";
		private final String SEARCH_UPC_KEY = "642FC14C-D4C2-46F3-91BB-784855F3DCCE";
		private final String OUTPAN_URL_GET = "http://www.outpan.com/api/get-product.php?barcode=";
		private final String OUTPAN_URL_SET = "http://www.outpan.com/api/edit-name.php?apikey=";
		private final String OUTPAN_KEY = "0fafc777e3692cf41bade0fb6c7926b9";
		private ProgressDialog pd;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(ScannerActivity.this);
			pd.setTitle(R.string.progress_dialog_title);
			pd.setMessage(getString(R.string.progress_dialog_message));
			pd.show();
		}
		
		@Override
		protected String doInBackground(Object... params) {
			dialog = (EnsureLookupDialog) params[0];
			String code = (String) params[1];
			String searchUPC = getSearchUpcUrl(code);
			String searchUPCResult = getResponse(searchUPC);
			Log.d(DrawerActivity.SCANIT, "searchupc: " + searchUPCResult);
			String productName = parseSearchUpcResponse(searchUPCResult);
			Log.d(DrawerActivity.SCANIT, productName
					+ (productName.length() == 0));
			if (productName.length() == 0) {
				String outpanUrl = getOutpanLookupUrl(code);
				String outpanResult = getResponse(outpanUrl);
				Log.d(DrawerActivity.SCANIT, "outpan: " + outpanResult);
				productName = parseOutpanResponse(outpanResult);
			}
			return productName;
		}

		@Override
		protected void onPostExecute(String productName) {
			pd.dismiss();
			dialog.setProductName(productName);
			dialog.show(getFragmentManager(), "barcode lookup");
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
				Log.e(DrawerActivity.SCANIT, "Error with http request to "
						+ url + " : " + e);
			} finally {
				if (inStream != null) {
					try {
						inStream.close();
					} catch (IOException e) {
						Log.e(DrawerActivity.SCANIT,
								"Error with closing the stream to " + url
										+ " : " + e);
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
}
