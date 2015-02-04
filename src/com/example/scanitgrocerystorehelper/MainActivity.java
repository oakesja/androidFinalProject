package com.example.scanitgrocerystorehelper;

import java.util.ArrayList;

import com.example.scanitgrocerystorehelper.adapters.ListArrayAdapter;
import com.example.scanitgrocerystorehelper.adapters.sql.ListSqlAdapter;
import com.example.scanitgrocerystorehelper.models.GroceryList;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends DrawerActivity {

	private ArrayList<GroceryList> mGroceryLists;
	private ListView mListView;
	private ListArrayAdapter mAdapter;
	private ListSqlAdapter mSqlAdapter;
	private final String KEY_LIST_NAME = "name";
	private final String KEY_LIST_TOSTRING = "data";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mGroceryLists = new ArrayList<GroceryList>();
		mSqlAdapter = new ListSqlAdapter(this);
		mSqlAdapter.open();
		mSqlAdapter.setAllLists(mGroceryLists);
		mAdapter = new ListArrayAdapter(this, mGroceryLists);
		
		
		mListView = (ListView) findViewById(R.id.srListsView);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> a, View v, int position,
					long id) {
				GroceryList gl = mAdapter.getItem(position);

				Intent newIntent = new Intent(MainActivity.this,
						ListActivity.class);
				newIntent.putExtra(KEY_LIST_NAME, gl.getName());
				newIntent.putExtra(KEY_LIST_TOSTRING, gl.getList().toString());
				startActivity(newIntent);

			}
		});

		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> a, View v,
					int position, long id) {
				Object o = mListView.getItemAtPosition(position);
				GroceryList fullObject = (GroceryList) o;
				mGroceryLists.remove(position);
				((BaseAdapter) mListView.getAdapter()).notifyDataSetChanged();
				Toast.makeText(MainActivity.this,
						"You have deleted: " + " " + fullObject.getName(),
						Toast.LENGTH_LONG).show();
				return true;
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.addList:
			showAddListDialog();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mSqlAdapter.close();
	}

	@SuppressLint("InflateParams")
	public void showAddListDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		LayoutInflater inflater = this.getLayoutInflater();
		final View mView = inflater.inflate(R.layout.dialog_add_list, null);
		builder.setView(mView);
		builder.setTitle(R.string.create_dialog_title);

		builder.setPositiveButton(R.string.add,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						EditText nameView = (EditText) mView
								.findViewById(R.id.dialogAddName);
						EditText courseView = (EditText) mView
								.findViewById(R.id.dialogAddDescription);
						GroceryList newList = new GroceryList(nameView
								.getText().toString(), courseView.getText()
								.toString());
						addGroceryList(newList);
					}
				});
		builder.setNegativeButton(android.R.string.cancel, null);

		AlertDialog dialog = builder.create();
		dialog.show();
	}

	private void addGroceryList(GroceryList gl) {
		mSqlAdapter.addList(gl);
		mSqlAdapter.setAllLists(mGroceryLists);
		mAdapter.notifyDataSetChanged();
	}
}
