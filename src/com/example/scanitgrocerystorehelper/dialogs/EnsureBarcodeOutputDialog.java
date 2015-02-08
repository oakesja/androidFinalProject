package com.example.scanitgrocerystorehelper.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.scanitgrocerystorehelper.DrawerActivity;
import com.example.scanitgrocerystorehelper.R;

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
		if (getProductName() != "") {
			editText.setText(getProductName());
		}
		builder.setNegativeButton(android.R.string.cancel, null);
		builder.setPositiveButton(R.string.add_to_list, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				handleClick(dialog, which, editText.getText().toString());
			}
		});
		builder.setView(v);
		return builder.create();
	}

	public abstract void handleClick(DialogInterface dialog, int which, String productName);
}
