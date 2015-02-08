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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ShoppingActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shopping);
		IntentIntegrator it = new IntentIntegrator(this);
		it.initiateScan();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		IntentResult scanResult = IntentIntegrator.parseActivityResult(
				requestCode, resultCode, data);
		if (scanResult != null) {
			TextView t = (TextView) findViewById(R.id.shoppingTextView);
			Log.d(DrawerActivity.SCANIT, scanResult.getContents());
			new BarcodeLookup().execute(t, scanResult.getContents());
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private class BarcodeLookup extends AsyncTask<Object, Void, String> {
		private TextView textView;

		@Override
		protected String doInBackground(Object... params) {
			textView = (TextView) params[0];
			String code = (String) params[1];
			Log.d(DrawerActivity.SCANIT, code);

			String url = "http://www.searchupc.com/handlers/upcsearch.ashx?request_type=3&access_token=642FC14C-D4C2-46F3-91BB-784855F3DCCE&upc=";
			url += code;
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
				e.printStackTrace();
			} finally {
				if (inStream != null) {
					try {
						inStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			Gson gson = new Gson();
			Log.d(DrawerActivity.SCANIT, result);
			JsonParser parser = new JsonParser();
			JsonElement element = parser.parse(result);
			String s = "";
			if (element.isJsonObject()) {
				JsonObject results = element.getAsJsonObject();
				JsonObject first = results.getAsJsonObject("0");
				s += first.get("productname").getAsString();
				s += " ";
				s += first.get("imageurl").getAsString();
			}
			textView.setText(s);
		}
	}

	public void showAddDoneScanningDialog(View v) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.reuse_dialog_title));
		builder.setMessage(getString(R.string.reuse_dialog_message));

		builder.setPositiveButton("Reuse",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Intent returnIntent = new Intent();
						returnIntent.putExtra(MainActivity.DELETE_SWITCH, 0);
						setResult(RESULT_OK, returnIntent);
						ShoppingActivity.this.finish();
					}
				});
		builder.setNegativeButton("Delete",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Intent returnIntent = new Intent();
						returnIntent.putExtra(MainActivity.DELETE_SWITCH, 1);
						setResult(RESULT_OK, returnIntent);
						ShoppingActivity.this.finish();
					}
				});

		AlertDialog dialog = builder.create();
		dialog.show();
	}
}
