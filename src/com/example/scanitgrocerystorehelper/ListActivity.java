package com.example.scanitgrocerystorehelper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import com.example.scanitgrocerystorehelper.adapters.ListItemArrayAdapter;
import com.example.scanitgrocerystorehelper.adapters.sql.ListSqlAdapter;
import com.example.scanitgrocerystorehelper.dialogs.EnsureBarcodeOutputDialog;

import com.example.scanitgrocerystorehelper.models.GroceryList;
import com.example.scanitgrocerystorehelper.models.ListItem;
import com.example.scanitgrocerystorehelper.utils.BarcodeLookup;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ShareActionProvider;
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
	private ShareActionProvider mShareActionProvider;
	private long listId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);

		mSqlAdapter = new ListSqlAdapter(this);
		mSqlAdapter.open();

		Intent data = this.getIntent();
		listId = data.getLongExtra(MainActivity.KEY_LIST_ID, -1);
		mGroceryList = mSqlAdapter.getList(listId);

		mList = new ArrayList<ListItem>();
		mSqlAdapter.setListItems(mList, mGroceryList.getId());

		totalView = (TextView) findViewById(R.id.total_price);
		TextView mHeader = (TextView) findViewById(R.id.list_header);
		mHeader.setText(mGroceryList.getName());

		updateList();

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
				deleteListItem(mAdapter.getItem(position));
				return true;
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.list_menu, menu);

		MenuItem shareItem = menu.findItem(R.id.shareList);
		mShareActionProvider = (ShareActionProvider) shareItem
				.getActionProvider();
		mShareActionProvider.setShareIntent(getDefaultIntent());

		return super.onCreateOptionsMenu(menu);
	}

	private Intent getDefaultIntent() {
		Intent sendIntent = new Intent();
		sendIntent.setAction(Intent.ACTION_SEND);
		sendIntent.putExtra(Intent.EXTRA_TEXT, mGroceryList.toString());
		sendIntent.setType("text/plain");
		return sendIntent;
	}

	private void updateShareIntent() {
		if (mShareActionProvider != null) {
			Intent sendIntent = new Intent();
			sendIntent.setAction(Intent.ACTION_SEND);
			sendIntent.putExtra(Intent.EXTRA_TEXT, mGroceryList.toString());
			sendIntent.setType("text/plain");
			mShareActionProvider.setShareIntent(sendIntent);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.addListItem:
			showAddListItemDialog();
			return true;
		case R.id.scanItem:
			IntentIntegrator it = new IntentIntegrator(this);
			it.initiateScan();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 1:
			Intent returnIntent = new Intent();
			if (resultCode == Activity.RESULT_OK) {
				int switchNum = data.getIntExtra(MainActivity.DELETE_SWITCH, 0);
				if (switchNum != 0) {
					returnIntent.putExtra(MainActivity.DELETE_SWITCH, 1);
					returnIntent.putExtra(MainActivity.KEY_LIST_ID, listId);
				}
			}
			setResult(RESULT_OK, returnIntent);
			this.finish();
			break;
		
		case IntentIntegrator.REQUEST_CODE:
		IntentResult scanResult = IntentIntegrator.parseActivityResult(
				requestCode, resultCode, data);
		if (scanResult != null && scanResult.getContents() != null) {
			new BarcodeLookup(this).execute(new EnsureLookupAddDialog(this),
					scanResult.getContents());
		}
		}
	}

	private class EnsureLookupAddDialog extends EnsureBarcodeOutputDialog {

		public EnsureLookupAddDialog(Context context) {
			super(context);
		}

		@Override
		public void handleClick(DialogInterface dialog, int which,
				String productName) {
			ListItem li = new ListItem(productName, 1, mGroceryList.getId());
			addListItem(li);
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
							updateList();
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
		startActivityForResult(newIntent, 1);
	}

	public void updateList() {
		updateShareIntent();
		BigDecimal total = new BigDecimal("0.00");
		for (ListItem l : mList) {
			total = total.add(
					l.getPrice().multiply(new BigDecimal(l.getQuantity())))
					.setScale(2, RoundingMode.HALF_UP);
		}
		totalView.setText("$" + total.toString());
	}

	private void addListItem(ListItem listItem) {
		mSqlAdapter.addListItem(listItem);
		mSqlAdapter.setListItems(mList, mGroceryList.getId());
		mAdapter.notifyDataSetChanged();
		updateList();
	}

	private void deleteListItem(ListItem listItem) {
		mSqlAdapter.deleteListItem(listItem);
		mSqlAdapter.setListItems(mList, mGroceryList.getId());
		mAdapter.notifyDataSetChanged();
		updateList();
	}

}
