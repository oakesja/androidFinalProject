package com.example.scanitgrocerystorehelper;

import java.util.ArrayList;

import com.example.scanitgrocerystorehelper.adapters.sql.ListSqlAdapter;
import com.example.scanitgrocerystorehelper.models.GroceryList;
import com.example.scanitgrocerystorehelper.models.ListItem;
import com.example.scanitgrocerystorehelper.utils.BarcodeDialog;
import com.example.scanitgrocerystorehelper.utils.BarcodeLookup;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

public class ScannerActivity extends DrawerActivity {
	private ListSqlAdapter mSqlAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scanner);
		mSqlAdapter = new ListSqlAdapter(this);
		mSqlAdapter.open();
		IntentIntegrator it = new IntentIntegrator(this);
		it.initiateScan();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mSqlAdapter.close();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		IntentResult scanResult = IntentIntegrator.parseActivityResult(
				requestCode, resultCode, data);
		if (scanResult != null) {
			Log.d(DrawerActivity.SCANIT, scanResult.getContents());
			new BarcodeLookup(this).execute(new EnsureLookupDialog(),
					scanResult.getContents());
		}
	}

	private class EnsureLookupDialog extends BarcodeDialog {
		private EditText editText;

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					ScannerActivity.this);
			View v = getLayoutInflater().inflate(R.layout.dialog_ensure_lookup,
					null);
			editText = (EditText) v.findViewById(R.id.editBarcodeLookupName);
			if (getProductName() != "") {
				editText.setText(getProductName());
			}
			builder.setNegativeButton(android.R.string.cancel, null);
			builder.setPositiveButton(R.string.add_to_list,
					new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							showSelectListDialog(getProductName());
						}
					});
			builder.setView(v);
			return builder.create();
		}
	}

	private void showSelectListDialog(final String productNameToAdd) {
		DialogFragment df = new DialogFragment() {
			@Override
			public Dialog onCreateDialog(Bundle savedInstanceState) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						ScannerActivity.this);
				builder.setTitle(R.string.select_list_dialog_title);
				final ArrayList<GroceryList> lists = new ArrayList<GroceryList>();
				mSqlAdapter.setAllLists(lists);
				String[] names = new String[lists.size()];
				for (int i = 0; i < lists.size(); i++) {
					names[i] = lists.get(i).getName();
				}
				builder.setItems(names, new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						long id = lists.get(which).getId();
						ListItem li = new ListItem(productNameToAdd, 1, id);
						mSqlAdapter.addListItem(li);
					}
				});
				return builder.create();
			}
		};
		df.show(getFragmentManager(), "select list");
	}
}
