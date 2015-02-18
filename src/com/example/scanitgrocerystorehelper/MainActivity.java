package com.example.scanitgrocerystorehelper;

import java.util.ArrayList;

import com.example.scanitgrocerystorehelper.adapters.ListArrayAdapter;
import com.example.scanitgrocerystorehelper.adapters.sql.ListSqlAdapter;
import com.example.scanitgrocerystorehelper.models.GroceryList;
import com.example.scanitgrocerystorehelper.models.ListItem;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends DrawerActivity {

	private ArrayList<GroceryList> mGroceryLists;
	private ListView mListView;
	private ListArrayAdapter mAdapter;
	private ListSqlAdapter mSqlAdapter;
	public static final String KEY_LIST_ID = "KEY_LIST_ID";
	static final String DONE_SHOPPING_ACTION = "DONE_SHOPPING_ACTION";
	static final int SHOPPING_REQUEST_CODE = 1;

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
				Log.d(DrawerActivity.SCANIT, "regular on click");
				GroceryList gl = mAdapter.getItem(position);

				Intent newIntent = new Intent(MainActivity.this,
						ListActivity.class);
				newIntent.putExtra(KEY_LIST_ID, gl.getId());
				startActivityForResult(newIntent, 1);
			}
		});

		mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
		mListView.setMultiChoiceModeListener(new MultiChoiceModeListener() {
			ArrayList<GroceryList> mChosenLists;
			Menu mMenu;

			@Override
			public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
				return false;
			}

			@Override
			public void onDestroyActionMode(ActionMode mode) {

			}

			@Override
			public boolean onCreateActionMode(ActionMode mode, Menu menu) {
				getMenuInflater().inflate(R.menu.edit_delete_contextual_menu,
						menu);
				mChosenLists = new ArrayList<GroceryList>();
				mMenu = menu;
				mode.setTitle(R.string.main_contextual_title);
				return true;
			}

			@Override
			public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
				switch (item.getItemId()) {
				case R.id.edit:
					showUpdateListDialog(mChosenLists.get(0));
					return true;

				case R.id.delete:
					showDeleteListDialog(mChosenLists);
					return true;
				default:
					return false;
				}
			}

			@Override
			public void onItemCheckedStateChanged(ActionMode mode,
					int position, long id, boolean checked) {
				GroceryList item = mAdapter.getItem(position);
				if (checked) {
					mChosenLists.add(item);
				} else {
					mChosenLists.remove(item);
				}
				boolean vis = (mChosenLists.size() > 1) ? false : true;
				((MenuItem) mMenu.findItem(R.id.edit)).setVisible(vis);
				((MenuItem) mMenu.findItem(R.id.edit)).setEnabled(vis);

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

		builder.setPositiveButton(R.string.add, null);
		builder.setNegativeButton(android.R.string.cancel, null);
		AlertDialog dialog = builder.create();
		dialog.setOnShowListener(new OnShowListener() {

			@Override
			public void onShow(DialogInterface dialog) {
				final DialogInterface d = dialog;
				Button b = ((AlertDialog) dialog)
						.getButton(AlertDialog.BUTTON_POSITIVE);
				b.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						EditText nameView = (EditText) mView
								.findViewById(R.id.dialogAddName);
						EditText courseView = (EditText) mView
								.findViewById(R.id.dialogAddDescription);
						String name = nameView.getText().toString();
						if (name.length() == 0) {
							nameView.setError(getString(R.string.list_name_error));
						} else {
							GroceryList newList = new GroceryList(name,
									courseView.getText().toString());
							addGroceryList(newList);
							d.dismiss();
						}

					}
				});
			}
		});

		dialog.show();
	}

	@SuppressLint("InflateParams")
	public void showUpdateListDialog(final GroceryList list) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		LayoutInflater inflater = this.getLayoutInflater();
		final View mView = inflater.inflate(R.layout.dialog_add_list, null);
		builder.setView(mView);
		builder.setTitle(R.string.edit_list_dialog_title);
		final EditText nameView = (EditText) mView
				.findViewById(R.id.dialogAddName);
		final EditText descriptionView = (EditText) mView
				.findViewById(R.id.dialogAddDescription);
		nameView.setText(list.getName());
		if (list.getDescription().length() > 0) {
			descriptionView.setText(list.getDescription());
		}

		builder.setPositiveButton(R.string.add, null);
		builder.setNegativeButton(android.R.string.cancel, null);

		AlertDialog dialog = builder.create();
		dialog.setOnShowListener(new OnShowListener() {

			@Override
			public void onShow(DialogInterface dialog) {
				final DialogInterface d = dialog;
				Button b = ((AlertDialog) dialog)
						.getButton(AlertDialog.BUTTON_POSITIVE);
				b.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						String name = nameView.getText().toString();
						if (name.length() == 0) {
							nameView.setError(getString(R.string.list_name_error));
						} else {
							list.setName(name);
							list.setDescription(descriptionView.getText()
									.toString());
							updateGroceryList(list);
							d.dismiss();
						}

					}
				});
			}
		});
		dialog.show();
	}

	public void showDeleteListDialog(final ArrayList<GroceryList> listsToDelete) {
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
								for (GroceryList groceryList : listsToDelete) {
									deleteList(groceryList);
								}
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

	private void updateGroceryList(GroceryList gl) {
		Log.d(DrawerActivity.SCANIT, "update list");
		mSqlAdapter.updateList(gl);
		mSqlAdapter.setAllLists(mGroceryLists);
		mAdapter.notifyDataSetChanged();
	}

	private void deleteList(GroceryList gl) {
		mSqlAdapter.deleteList(gl);
		mSqlAdapter.setAllLists(mGroceryLists);
		mAdapter.notifyDataSetChanged();
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case SHOPPING_REQUEST_CODE:
			if (resultCode == Activity.RESULT_OK) {
				int switchNum = data.getIntExtra(DONE_SHOPPING_ACTION, 0);
				if (switchNum != 0) {
					long newNum = data.getLongExtra(KEY_LIST_ID, 0);
					if (newNum != 0)
						deleteList(mSqlAdapter.getList(newNum));
				}
			}
		}
	}
}
