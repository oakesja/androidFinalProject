package com.example.scanitgrocerystorehelper;

import java.util.ArrayList;

import com.example.scanitgrocerystorehelper.adapters.sql.ListSqlAdapter;
import com.example.scanitgrocerystorehelper.models.GroceryList;
import com.example.scanitgrocerystorehelper.models.ListItem;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
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
	
	private void selectZipCodeDialog(final long listId){
		DialogFragment df = new DialogFragment(){
			@Override
			public Dialog onCreateDialog(Bundle savedInstanceState) {
				AlertDialog.Builder builder = new AlertDialog.Builder(CouponActivity.this);
				final View v = getLayoutInflater().inflate(R.layout.dialog_enter_zip_code, null);
				builder.setView(v);
				builder.setTitle(R.string.zip_code_dialog_title);
				builder.setNegativeButton(android.R.string.cancel, null);
				builder.setPositiveButton(android.R.string.ok, new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String zipcode = ((EditText)v.findViewById(R.id.zipCodeEditText)).getText().toString();
						String url = createUrl(listId, zipcode);
						Log.d(DrawerActivity.SCANIT, url);
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
}
