package com.example.scanitgrocerystorehelper;

import android.os.Bundle;

public class ReminderActivity extends DrawerActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reminder);
		getActionBar().setTitle(R.string.title_activity_reminder);
	}
}
