package com.example.scanitgrocerystorehelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class ShoppingActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shopping);
	}

	public void showAddDoneScanningDialog(View v) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.reuse_dialog_title));
		builder.setMessage(getString(R.string.reuse_dialog_message));

		builder.setPositiveButton("Reuse",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Toast.makeText(ShoppingActivity.this,
								"This one's a keeper",
								Toast.LENGTH_LONG).show();
					}
				});
		builder.setNegativeButton("Delete",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Toast.makeText(ShoppingActivity.this,
								"I'm gonna wreck it!",
								Toast.LENGTH_LONG).show();
					}
				});
		
		AlertDialog dialog = builder.create();
		dialog.show();
	}
}
