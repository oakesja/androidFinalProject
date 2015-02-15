package com.example.scanitgrocerystorehelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import com.example.scanitgrocerystorehelper.adapters.ShoppingActivityArrayAdapter;
import com.example.scanitgrocerystorehelper.adapters.sql.ListSqlAdapter;
import com.example.scanitgrocerystorehelper.models.GroceryList;
import com.example.scanitgrocerystorehelper.models.ListItem;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ShoppingActivity extends Activity {
	private ListSqlAdapter mSqlAdapter;
	private ArrayList<ListItem> mList;
	private ShoppingActivityArrayAdapter mArrayAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shopping);

		mSqlAdapter = new ListSqlAdapter(this);
		mSqlAdapter.open();

		Intent data = this.getIntent();
		long listId = data.getLongExtra(MainActivity.KEY_LIST_ID, -1);
		mList = new ArrayList<ListItem>();
		mSqlAdapter.setListItems(mList, listId);

		mArrayAdapter = new ShoppingActivityArrayAdapter(this, mList);

		ListView listView = (ListView) findViewById(R.id.shoppingListView);
		listView.setAdapter(mArrayAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ListItem li = mArrayAdapter.getItem(position);
				li.setCheckedOff(!li.isCheckedOff());
				Collections.sort(mList);
				mArrayAdapter.notifyDataSetChanged();
			}
		});
	}

	public void showAddDoneScanningDialog() {
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
