package com.example.scanitgrocerystorehelper;

import java.math.BigDecimal;
import java.util.ArrayList;
import com.example.scanitgrocerystorehelper.adapters.ListArrayAdapter;
import com.example.scanitgrocerystorehelper.models.List;
import com.example.scanitgrocerystorehelper.models.ListItem;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends DrawerActivity {

	private ArrayList<List> mList;
	private ListView mListView;
	private ListArrayAdapter mAdapter;
	private final String KEY_LIST_NAME = "name";
	private final String KEY_LIST_TOSTRING = "data";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mList = generateTestItems();

		mListView = (ListView) findViewById(R.id.srListsView);

		mAdapter = new ListArrayAdapter(this, mList);
		mListView.setAdapter(mAdapter);

		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> a, View v, int position,
					long id) {
				Object o = mListView.getItemAtPosition(position);
				List fullObject = (List) o;
//				Toast.makeText(MainActivity.this,
//						"You have chosen: " + " " + fullObject.getName(),
//						Toast.LENGTH_LONG).show();

				Intent newIntent = new Intent(MainActivity.this,
						ListActivity.class);
				newIntent.putExtra(KEY_LIST_NAME, fullObject.getName());
				newIntent.putExtra(KEY_LIST_TOSTRING, fullObject.getList()
						.toString());
				startActivity(newIntent);

			}
		});

		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> a, View v,
					int position, long id) {
				Object o = mListView.getItemAtPosition(position);
				List fullObject = (List) o;
				mList.remove(position);
				((BaseAdapter) mListView.getAdapter()).notifyDataSetChanged();
				Toast.makeText(MainActivity.this,
						"You have deleted: " + " " + fullObject.getName(),
						Toast.LENGTH_LONG).show();
				return true;
			}
		});
	}

	private ArrayList<List> generateTestItems() {
		ArrayList<List> mNewList = new ArrayList<List>();
		ArrayList<ListItem> mNewItemList = new ArrayList<ListItem>();
		mNewItemList.add(new ListItem("Fruit", 3, new BigDecimal("4.00")));

		mNewList.add(new List("TestName", "TestDescription", mNewItemList));
		mNewList.add(new List("TestName2", "TestDescription2"));
		mNewList.add(new List("TestName3", "TestDescription3"));

		return mNewList;
	}

	@SuppressLint("InflateParams")
	public void showAddListDialog(View v) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		LayoutInflater inflater = this.getLayoutInflater();
		final View mView = inflater.inflate(R.layout.dialog_add_list, null);
		builder.setView(mView);

		builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				EditText nameView = (EditText) mView
						.findViewById(R.id.dialogAddName);
				EditText courseView = (EditText) mView
						.findViewById(R.id.dialogAddDescription);
				List newList = new List(nameView.getText().toString(),
						courseView.getText().toString());

				mList.add(newList);
				mAdapter.notifyDataSetChanged();
			}
		});
		builder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// User clicked Cancel button, do nothing
					}
				});

		AlertDialog dialog = builder.create();
		dialog.show();
	}

}
