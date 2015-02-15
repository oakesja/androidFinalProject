package com.example.scanitgrocerystorehelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import com.example.scanitgrocerystorehelper.adapters.sql.ListSqlAdapter;
import com.example.scanitgrocerystorehelper.models.GroceryList;
import com.example.scanitgrocerystorehelper.models.ListItem;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class CouponActivity extends DrawerActivity {

	private final String mUrl = "http://www.groceryserver.com/groceryserver/gsmobile/site/index.jsp?";
	private ListSqlAdapter mSqlAdapter;
	private ArrayList<GroceryList> mLists;
	private long mListId;
	private LocationManager mLocationManager;
	private ProgressDialog mLocationProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_coupon);

		mSqlAdapter = new ListSqlAdapter(this);
		mSqlAdapter.open();

		mLists = new ArrayList<GroceryList>();
		mSqlAdapter.setAllLists(mLists);
		ArrayList<String> names = new ArrayList<String>();
		for (int i = 0; i < mLists.size(); i++) {
			names.add(mLists.get(i).getName());
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, names);
		ListView lv = (ListView) findViewById(R.id.list_view);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				long listId = mLists.get(position).getId();
				selectZipCodeDialog(listId);
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mSqlAdapter.close();
	}

	private void selectZipCodeDialog(final long listId) {
		DialogFragment df = new DialogFragment() {
			@Override
			public Dialog onCreateDialog(Bundle savedInstanceState) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						CouponActivity.this);
				final View v = getLayoutInflater().inflate(
						R.layout.dialog_enter_zip_code, null);
				builder.setView(v);
				builder.setTitle(R.string.zip_code_dialog_title);
				builder.setNegativeButton(android.R.string.cancel, null);
				builder.setNeutralButton(R.string.use_current_location,
						new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								mListId = listId;
								findLocation();
							}
						});
				builder.setPositiveButton(android.R.string.ok,
						new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								String zipcode = ((EditText) v
										.findViewById(R.id.zipCodeEditText))
										.getText().toString();
								String url = createUrl(listId, zipcode);
								openWebView(url);
							}
						});
				return builder.create();
			}
		};
		df.show(getFragmentManager(), "zip code");
	}

	private String createUrl(long listId, String zipcode) {
		ArrayList<ListItem> items = new ArrayList<ListItem>();
		mSqlAdapter.setListItems(items, listId);
		String url = mUrl;
		url += "zipCode=" + zipcode + "&items=";
		for (int i = 0; i < items.size(); i++) {
			url += getUrlName(items.get(i).getName()) + "|";
		}
		return url.substring(0, url.length() - 1);
	}

	private String getUrlName(String name) {
		String s = name.replaceAll("[^A-Za-z0-9 ]", "");
		return s.replaceAll("\\s", "%20");
	}

	private void openWebView(String url) {
		try {
			Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			startActivity(myIntent);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(
					this,
					"No application can handle this request."
							+ " Please install a webbrowser", Toast.LENGTH_LONG)
					.show();
		}
	}

	private void findLocation() {
		mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		Location location = mLocationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (location != null
				&& location.getTime() > Calendar.getInstance()
						.getTimeInMillis() - 2 * 60 * 1000) {
			Log.d(DrawerActivity.SCANIT, "use recent location");
			String zipcode = getZipFromLocation(location);
			String url = createUrl(mListId, zipcode);
			openWebView(url);
		} else {
			Log.d(DrawerActivity.SCANIT, "set location listener");
			mLocationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, 0, 0,
					new MyLocationListener());
			mLocationProgressDialog = ProgressDialog.show(this, getString(R.string.finding_current_location), null);
		}
	}

	private class MyLocationListener implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			Log.d(DrawerActivity.SCANIT, "found location");
			String zipcode = getZipFromLocation(location);
			String url = createUrl(mListId, zipcode);
			openWebView(url);
			mLocationManager.removeUpdates(this);
			mLocationProgressDialog.dismiss();
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub

		}
	}

	private String getZipFromLocation(Location l) {
		Geocoder geocoder = new Geocoder(this, Locale.getDefault());
		String zip = "";
		try {
			List<Address> addresses = geocoder.getFromLocation(l.getLatitude(),
					l.getLongitude(), 1);
			zip = addresses.get(0).getPostalCode();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return zip;
	}
}
