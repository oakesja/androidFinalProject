package com.example.scanitgrocerystorehelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import com.example.scanitgrocerystorehelper.adapters.ShoppingActivityArrayAdapter;
import com.example.scanitgrocerystorehelper.adapters.sql.ListSqlAdapter;
import com.example.scanitgrocerystorehelper.models.ListItem;
import com.example.scanitgrocerystorehelper.utils.Speaker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ShoppingActivity extends Activity {
	private ListSqlAdapter mSqlAdapter;
	private ArrayList<ListItem> mList;
	private ShoppingActivityArrayAdapter mArrayAdapter;
	private final int REQ_CODE_SPEECH_INPUT = 100;
	private Speaker mSpeaker;

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
				markOffItem(li);
			}
		});
		mSpeaker = new Speaker(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mSqlAdapter.close();
		mSpeaker.destroy();
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
		case R.id.sayItem:
			promptSpeechInput();

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public void showDoneShoppingDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.done_shopping_dialog_title));
		final boolean allMarked = itemsLeft() == 0;
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

	private int itemsLeft() {
		int count = 0;
		for (int i = 0; i < mList.size(); i++) {
			if (!mList.get(i).isCheckedOff()) {
				count++;
			}
		}
		return count;
	}

	private void deleteMarkedOff() {
		for (int i = 0; i < mList.size(); i++) {
			ListItem item = mList.get(i);
			if (item.isCheckedOff()) {
				mSqlAdapter.deleteListItem(item);
			}
		}
	}

	private void promptSpeechInput() {
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
				RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
		intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
				getString(R.string.speech_remove_item_title));
		try {
			startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
		} catch (ActivityNotFoundException a) {
			Toast.makeText(getApplicationContext(),
					getString(R.string.speech_not_supported),
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case REQ_CODE_SPEECH_INPUT: {
			if (resultCode == RESULT_OK && null != data) {

				ArrayList<String> results = data
						.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
				Log.d(DrawerActivity.SCANIT, results.toString());
				ListItem bestMatch = null;
				boolean tooMany = false;
				for (ListItem item : mList) {
					if (item.isCheckedOff()) {
						break;
					}
					float localBestPerc = 0;
					for (String possibleMatch : results) {
						float p = getPercentageMatch(item, possibleMatch);
						if (p > localBestPerc) {
							localBestPerc = p;
						}
					}
					if (localBestPerc == 1.0) {
						bestMatch = item;
						break;
					} else if (localBestPerc > 0.5 && bestMatch == null) {
						bestMatch = item;
					} else if (localBestPerc > 0.5) {
						// too many matches
						bestMatch = null;
						tooMany = true;
						break;
					}
				}
				if (bestMatch != null) {
					markOffItem(bestMatch);
					mSpeaker.speak(getString(R.string.speech_item_removed,
							bestMatch.getName()));
				} else if (tooMany) {
					mSpeaker.speak(getString(R.string.speech_too_many_matches));
				} else {
					mSpeaker.speak(getString(R.string.speech_could_not_find,
							results.get(0)));
				}
			}
			break;
		}
		default:
			break;
		}
	}

	private float getPercentageMatch(ListItem item, String proposedName) {
		String[] itemParts = item.getName().split("\\s+");
		String[] proposedNameParts = proposedName.split("\\s+");
		int count = 0;
		for (int i = 0; i < proposedNameParts.length; i++) {
			if (i > itemParts.length - 1) {
				break;
			}
			Log.d(DrawerActivity.SCANIT, itemParts[i] + " : "
					+ proposedNameParts[i]);
			if (itemParts[i].equalsIgnoreCase(proposedNameParts[i])) {
				count++;
			}
		}
		Log.d(DrawerActivity.SCANIT, "" + count + " : " + itemParts.length);
		int denom = Math.max(itemParts.length, proposedNameParts.length);
		Log.d(DrawerActivity.SCANIT, "" + denom);
		return (float) count / denom;
	}

	private void markOffItem(ListItem li) {
		li.setCheckedOff(!li.isCheckedOff());
		Collections.sort(mList);
		mArrayAdapter.notifyDataSetChanged();
	}
}
