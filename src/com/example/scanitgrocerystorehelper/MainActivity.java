package com.example.scanitgrocerystorehelper;

import java.util.ArrayList;

import com.example.scanitgrocerystorehelper.adapters.ListArrayAdapter;
import com.example.scanitgrocerystorehelper.adapters.sql.ListSqlAdapter;
import com.example.scanitgrocerystorehelper.models.GroceryList;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends DrawerActivity {

	private ArrayList<GroceryList> mGroceryLists;
	private ListView mListView;
	private ListArrayAdapter mAdapter;
	private ListSqlAdapter mSqlAdapter;
	public static final String KEY_LIST_ID = "KEY_LIST_ID";

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
				newIntent.putExtra(KEY_LIST_ID, gl.getId());
				startActivity(newIntent);
			}
		});

		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> a, View v,
					int position, long id) {
				showDeleteListDialog(position);
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

	public void showDeleteListDialog(final int position) {
		DialogFragment df = new DialogFragment() {
			@Override
			public Dialog onCreateDialog(Bundle savedInstanceState) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());
				builder.setTitle(R.string.confirm_delete_title);
				builder.setMessage(R.string.confirm_delete_message_list);
				builder.setNegativeButton(android.R.string.cancel, null);
				builder.setPositiveButton(android.R.string.ok,
						new OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								deleteList(mAdapter.getItem(position));
							}
						});
				return builder.create();
			}
		};
		df.show(getFragmentManager(), "delete list");
	}

	private void addGroceryList(GroceryList gl) {
		mSqlAdapter.addList(gl);
		mSqlAdapter.setAllLists(mGroceryLists);
		mAdapter.notifyDataSetChanged();
	}

	private void deleteList(GroceryList gl) {
		mSqlAdapter.deleteList(gl);
		mSqlAdapter.setAllLists(mGroceryLists);
		mAdapter.notifyDataSetChanged();
	}
}
