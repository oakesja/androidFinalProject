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
import android.widget.ListView;
import android.widget.Toast;

public class CouponActivity extends DrawerActivity {

	private final String mUrl = "http://www.groceryserver.com/groceryserver/gsmobile/site/index.jsp?zipCode=47803&items=";
	private ListSqlAdapter mSqlAdapter;
	private ArrayList<GroceryList> mLists;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_coupon);

		// TODO need to manipulate url above with Zipcode and specified lists or
		// single item
		// need to validate the url after each item adition to it to make sure
		// it is still valid
		// replace spaces with %20
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
				String url = createUrl(listId);
				Log.d(DrawerActivity.SCANIT, url);
				openWebView(url);
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mSqlAdapter.close();
	}

	private String createUrl(long listId) {
		ArrayList<ListItem> items = new ArrayList<ListItem>();
		mSqlAdapter.setListItems(items, listId);
		String url = mUrl;
		for (int i = 0; i < items.size(); i++) {
			url += getUrlName(items.get(i).getName()) + "|";
		}
		return url.substring(0, url.length() - 1);
	}
	
	private String getUrlName(String name){
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
