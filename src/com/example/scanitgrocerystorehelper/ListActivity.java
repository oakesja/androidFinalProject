package com.example.scanitgrocerystorehelper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Locale;

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
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
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
	private final int REQ_CODE_SPEECH_INPUT = 100;

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

		mListView = (ListView) findViewById(R.id.srListView);
		mAdapter = new ListItemArrayAdapter(this, mList);
		mListView.setAdapter(mAdapter);
		
		updateList();

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
		case R.id.sayItem:
			promptSpeechInput();

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case MainActivity.SHOPPING_REQUEST_CODE:
			Intent returnIntent = new Intent();
			if (resultCode == Activity.RESULT_OK) {
				int action = data.getIntExtra(
						MainActivity.DONE_SHOPPING_ACTION, -1);
				if (action != -1) {
					returnIntent.putExtra(MainActivity.DONE_SHOPPING_ACTION,
							action);
					returnIntent.putExtra(MainActivity.KEY_LIST_ID, listId);
					setResult(RESULT_OK, returnIntent);
					this.finish();
				} 
			}
			updateList();
			break;

		case IntentIntegrator.REQUEST_CODE:
			IntentResult scanResult = IntentIntegrator.parseActivityResult(
					requestCode, resultCode, data);
			if (scanResult != null && scanResult.getContents() != null) {
				new BarcodeLookup(this).execute(
						new EnsureLookupAddDialog(this),
						scanResult.getContents());
			}
			break;
		case REQ_CODE_SPEECH_INPUT: {
			if (resultCode == RESULT_OK && null != data) {

				ArrayList<String> result = data
						.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
				if (result.size() == 1) {
					ListItem li = new ListItem(result.get(0), 1,
							mGroceryList.getId());
					addListItem(li);
				} else {
					selectCorrectSpeechResultDialog(result);
				}
			}
			break;
		}
		default:
			break;
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

	private void selectCorrectSpeechResultDialog(final ArrayList<String> options) {
		DialogFragment df = new DialogFragment() {
			@Override
			public Dialog onCreateDialog(Bundle savedInstanceState) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						ListActivity.this);
				builder.setTitle(R.string.select_correct_name);
				builder.setItems(
						options.toArray(new CharSequence[options.size()]),
						new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								ListItem li = new ListItem(options.get(which),
										1, mGroceryList.getId());
								addListItem(li);
							}
						});
				return builder.create();
			}
		};
		df.show(getFragmentManager(), "speech options");
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
						String quant = quantityView.getText().toString();
						int quantity = (quant.length() == 0) ? -1 : Integer
								.parseInt(quant);
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
		newIntent.putExtra(MainActivity.KEY_LIST_ID, mGroceryList.getId());
		startActivityForResult(newIntent, 1);
	}

	public void updateList() {
		mSqlAdapter.setListItems(mList, mGroceryList.getId());
		mAdapter.notifyDataSetChanged();
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

	private void promptSpeechInput() {
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
				RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
		intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
				getString(R.string.say_item));
		try {
			startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
		} catch (ActivityNotFoundException a) {
			Toast.makeText(getApplicationContext(),
					getString(R.string.speech_not_supported),
					Toast.LENGTH_SHORT).show();
		}
	}

}
