package com.example.scanitgrocerystorehelper;

import android.os.Bundle;

public class ScannerActivity extends DrawerActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scanner);
		getActionBar().setTitle(R.string.title_activity_scanner);
	}
}
