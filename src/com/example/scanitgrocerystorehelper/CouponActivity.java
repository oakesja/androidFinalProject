package com.example.scanitgrocerystorehelper;

import android.os.Bundle;

public class CouponActivity extends DrawerActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_coupon);
		getActionBar().setTitle(R.string.title_activity_coupon);

	}
}
