package com.example.scanitgrocerystorehelper;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.example.scanitgrocerystorehelper.adapters.ListItemArrayAdapter;
import com.example.scanitgrocerystorehelper.models.ListItem;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
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
	private final String KEY_LIST_NAME = "name";
	private final String KEY_LIST_TOSTRING = "data";
	private String listName;
	private TextView totalView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);

		Intent data = this.getIntent();
		listName = data.getStringExtra("name");

		totalView = (TextView) findViewById(R.id.total_price);
		TextView mHeader = (TextView) findViewById(R.id.list_header);
		mHeader.setText(listName);

		mList = generateTestItems();

		updateTotal();

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

	@SuppressLint("InflateParams")
	public void showAddListItemDialog(View v) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		LayoutInflater inflater = this.getLayoutInflater();
		final View mView = inflater
				.inflate(R.layout.dialog_add_list_item, null);
		builder.setView(mView);

		builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				EditText nameView = (EditText) mView
						.findViewById(R.id.dialogAddItemName);
				EditText quantityView = (EditText) mView
						.findViewById(R.id.dialogAddQuantity);
				EditText priceView = (EditText) mView
						.findViewById(R.id.dialogAddPrice);

				ListItem newListItem;
				if (priceView.getText().length() != 0) {
					newListItem = new ListItem(
							nameView.getText().toString(),
							Integer.parseInt(quantityView.getText().toString()),
							new BigDecimal(priceView.getText().toString()));
					updateTotal();
				} else {
					newListItem = new ListItem(nameView.getText().toString(),
							Integer.parseInt(quantityView.getText().toString()));
				}

				mList.add(newListItem);
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

	private ArrayList<ListItem> generateTestItems() {
		ArrayList<ListItem> mNewList = new ArrayList<ListItem>();
		mNewList.add(new ListItem("Milk", 1, new BigDecimal("2.00")));
		mNewList.add(new ListItem("Bread", 2, new BigDecimal("3.00")));
		mNewList.add(new ListItem("Eggs", 1, new BigDecimal("50.00")));
		mNewList.add(new ListItem("Steak", 1));
		mNewList.add(new ListItem("Cheese", 5));
		mNewList.add(new ListItem("Chips", 10, new BigDecimal("10.00")));

		return mNewList;
	}

	public void startShopping(View v) {
		Intent newIntent = new Intent(ListActivity.this, ShoppingActivity.class);
		newIntent.putExtra(KEY_LIST_NAME, listName);
		newIntent.putExtra(KEY_LIST_TOSTRING, mList.toString());
		startActivity(newIntent);
	}

	public void updateTotal() {
		BigDecimal total = new BigDecimal("0.00");
		for(ListItem l : mList){
			total.add(l.getPrice().multiply(new BigDecimal(l.getQuantity())));
		}
		totalView.setText("$" + total.toString());
	}

}
