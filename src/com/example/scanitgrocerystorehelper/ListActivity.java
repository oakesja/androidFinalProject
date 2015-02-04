package com.example.scanitgrocerystorehelper;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.example.scanitgrocerystorehelper.adapters.ListItemArrayAdapter;
import com.example.scanitgrocerystorehelper.adapters.sql.ListSqlAdapter;
import com.example.scanitgrocerystorehelper.adapters.sql.SqlAdapterKeys;
import com.example.scanitgrocerystorehelper.models.GroceryList;
import com.example.scanitgrocerystorehelper.models.ListItem;

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
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class ListActivity extends DrawerActivity {

	private ArrayList<ListItem> mList;
	private ListItemArrayAdapter mAdapter;
	private ListView mListView;
	private GroceryList mGroceryList;
	private TextView totalView;
	private ListSqlAdapter mSqlAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);

		mSqlAdapter = new ListSqlAdapter(this);
		mSqlAdapter.open();

		Intent data = this.getIntent();
		long listId = data.getLongExtra(MainActivity.KEY_LIST_ID, -1);
		mGroceryList = mSqlAdapter.getList(listId);

		mList = new ArrayList<ListItem>();
		mSqlAdapter.setListItems(mList, mGroceryList.getId());

		// totalView = (TextView) findViewById(R.id.total_price);
		TextView mHeader = (TextView) findViewById(R.id.list_header);
		mHeader.setText(mGroceryList.getName());

		// updateTotal();

		mListView = (ListView) findViewById(R.id.srListView);
		mAdapter = new ListItemArrayAdapter(this, mList);
		mListView.setAdapter(mAdapter);

		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> a, View v, int position,
					long id) {
				Object o = mListView.getItemAtPosition(position);
				ListItem fullObject = (ListItem) o;
				Toast.makeText(ListActivity.this,
						"You have chosen: " + " " + fullObject.getName(),
						Toast.LENGTH_LONG).show();
			}
		});

		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> a, View v,
					int position, long id) {
				Object o = mListView.getItemAtPosition(position);
				ListItem fullObject = (ListItem) o;
				mList.remove(position);
				((BaseAdapter) mListView.getAdapter()).notifyDataSetChanged();
				Toast.makeText(ListActivity.this,
						"You have deleted: " + " " + fullObject.getName(),
						Toast.LENGTH_LONG).show();
				updateTotal();
				return true;
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.list_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.addListItem:
			showAddListItemDialog();
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
	public void showAddListItemDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		LayoutInflater inflater = this.getLayoutInflater();
		final View mView = inflater
				.inflate(R.layout.dialog_add_list_item, null);
		builder.setView(mView);

		builder.setPositiveButton(R.string.add,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						EditText nameView = (EditText) mView
								.findViewById(R.id.dialogAddItemName);
						EditText quantityView = (EditText) mView
								.findViewById(R.id.dialogAddQuantity);
						EditText priceView = (EditText) mView
								.findViewById(R.id.dialogAddPrice);

						ListItem newListItem;
						String name = nameView.getText().toString();
						int quantity = Integer.parseInt(quantityView.getText()
								.toString());
						if (priceView.getText().length() != 0) {
							BigDecimal price = new BigDecimal(priceView
									.getText().toString());
							newListItem = new ListItem(name, quantity, price,
									mGroceryList.getId());
							updateTotal();
						} else {
							newListItem = new ListItem(name, quantity,
									mGroceryList.getId());
						}

						addListItem(newListItem);
					}
				});
		builder.setNegativeButton(android.R.string.cancel, null);

		AlertDialog dialog = builder.create();
		dialog.show();
	}

	public void startShopping(View v) {
		Intent newIntent = new Intent(ListActivity.this, ShoppingActivity.class);
		// newIntent.putExtra(KEY_LIST_NAME, listName);
		// newIntent.putExtra(KEY_LIST_TOSTRING, mList.toString());
		startActivity(newIntent);
	}

	public void updateTotal() {
		BigDecimal total = new BigDecimal("0.00");
		for (ListItem l : mList) {
			total.add(l.getPrice().multiply(new BigDecimal(l.getQuantity())));
		}
		// totalView.setText("$" + total.toString());
	}

	private void addListItem(ListItem listItem) {
		mSqlAdapter.addListItem(listItem);
		mSqlAdapter.setListItems(mList, mGroceryList.getId());
		mAdapter.notifyDataSetChanged();
	}

}
