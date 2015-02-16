package com.example.scanitgrocerystorehelper;

import java.util.ArrayList;
import java.util.Collections;

import com.example.scanitgrocerystorehelper.adapters.ShoppingActivityArrayAdapter;
import com.example.scanitgrocerystorehelper.adapters.sql.ListSqlAdapter;
import com.example.scanitgrocerystorehelper.models.ListItem;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ShoppingActivity extends Activity {
	private ListSqlAdapter mSqlAdapter;
	private ArrayList<ListItem> mList;
	private ShoppingActivityArrayAdapter mArrayAdapter;

	public static final int SHOPPING_ACTIVITY_DELETE_LIST = 1;

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

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mSqlAdapter.close();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.shopping_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.doneShopping:
			showDoneShoppingDialog();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public void showDoneShoppingDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.done_shopping_dialog_title));
		final boolean allMarked = allMarkedOff();
		String[] options;
		if (allMarked) {
			options = new String[2];
			options[0] = getString(R.string.reuse_all);
			options[1] = getString(R.string.delete_all);
		} else {
			options = new String[3];
			options[0] = getString(R.string.reuse_all);
			options[1] = getString(R.string.reuse_part);
			options[2] = getString(R.string.delete_all);
		}
		builder.setItems(options, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (which == 0) {
					ShoppingActivity.this.finish();
				} else if (which == 1 && !allMarked) {
					deleteMarkedOff();
					ShoppingActivity.this.finish();
				} else {
					Intent returnIntent = new Intent();
					returnIntent.putExtra(MainActivity.DONE_SHOPPING_ACTION,
							SHOPPING_ACTIVITY_DELETE_LIST);
					setResult(RESULT_OK, returnIntent);
					ShoppingActivity.this.finish();
				}
			}
		});

		AlertDialog dialog = builder.create();
		dialog.show();
	}

	private boolean allMarkedOff() {
		for (int i = 0; i < mList.size(); i++) {
			if (!mList.get(i).isCheckedOff()) {
				return false;
			}
		}
		return true;
	}

	private void deleteMarkedOff() {
		for (int i = 0; i < mList.size(); i++) {
			ListItem item = mList.get(i);
			if (item.isCheckedOff()) {
				mSqlAdapter.deleteListItem(item);
			}
		}
	}
}
