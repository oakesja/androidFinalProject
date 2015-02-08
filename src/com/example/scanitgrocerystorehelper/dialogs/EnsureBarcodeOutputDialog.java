package com.example.scanitgrocerystorehelper.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.scanitgrocerystorehelper.DrawerActivity;
import com.example.scanitgrocerystorehelper.R;
import com.example.scanitgrocerystorehelper.utils.ShareProductNameTask;

public abstract class EnsureBarcodeOutputDialog extends BarcodeDialog {
	private Context mContext;
	private EditText editText;

	public EnsureBarcodeOutputDialog(Context context) {
		mContext = context;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		View v = ((Activity) mContext).getLayoutInflater().inflate(
				R.layout.dialog_ensure_lookup, null);
		editText = (EditText) v.findViewById(R.id.editBarcodeLookupName);
		Log.d(DrawerActivity.SCANIT, "ensure barcode dialog " + getProductName());
		if (getProductName().length() != 0) {
			editText.setText(getProductName());
		}
		builder.setNegativeButton(android.R.string.cancel, null);
		builder.setPositiveButton(R.string.add_to_list, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Log.d(DrawerActivity.SCANIT, "add to list on click listener "
						+ (getProductName() != ""));
				String productName = editText.getText().toString();
				if (getProductName().length() == 0) {
					showShareDialog(productName);
				} else {
					handleClick(dialog, which, productName);
				}
			}
		});
		builder.setView(v);
		return builder.create();
	}

	private void showShareDialog(final String productName) {
		DialogFragment df = new DialogFragment() {
			@Override
			public Dialog onCreateDialog(Bundle savedInstanceState) {
				AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
				builder.setTitle(R.string.share_lookup_title);
				builder.setMessage(R.string.share_lookup_message);
				builder.setNegativeButton(android.R.string.no,
						new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								handleClick(dialog, which, productName);
							}
						});
				builder.setPositiveButton(android.R.string.yes,
						new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								String[] params = new String[2];
								params[0] = getBarcode();
								params[1] = productName;
								new ShareProductNameTask().execute(params);
								handleClick(dialog, which, productName);
							}
						});
				return builder.create();
			}
		};
		df.show(getFragmentManager(), "share lookup");
	}

	public abstract void handleClick(DialogInterface dialog, int which,
			String productName);
}
