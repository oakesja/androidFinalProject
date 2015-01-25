package com.example.scanitgrocerystorehelper;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class ScannerActivity extends DrawerActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scanner);
		getActionBar().setTitle(R.string.title_activity_scanner);
		IntentIntegrator it = new IntentIntegrator(this);
		it.initiateScan();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		IntentResult scanResult = IntentIntegrator.parseActivityResult(
				requestCode, resultCode, data);
		if (scanResult != null) {
			Toast.makeText(this, scanResult.toString(), Toast.LENGTH_LONG).show();;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
