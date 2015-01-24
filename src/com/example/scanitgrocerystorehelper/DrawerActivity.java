package com.example.scanitgrocerystorehelper;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

public abstract class DrawerActivity extends Activity {
	public static final String SCANIT = "SCANIT";

	// need to update to v7 version
	private ActionBarDrawerToggle mDrawerToggle;
	private DrawerLayout mDrawerLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mDrawerLayout = (DrawerLayout) getLayoutInflater().inflate(
				R.layout.activity_drawer, null);

		ListView drawerList = (ListView) mDrawerLayout
				.findViewById(R.id.left_drawer);

		String[] options = getResources().getStringArray(R.array.drawer_names);
		TypedArray drawableIds = getResources().obtainTypedArray(
				R.array.drawer_images);
		drawerList
				.setAdapter(new DrawerArrayAdapter(this, options, drawableIds));
		drawerList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// consider using fragments instead of intents
				Intent intent = new Intent();
				switch (position) {
				case 0:
					intent.setClass(getBaseContext(), MainActivity.class);
					break;
				case 1:
					intent.setClass(getBaseContext(), CouponActivity.class);
					break;
				case 2:
					intent.setClass(getBaseContext(), ReminderActivity.class);
					break;
				case 3:
					intent.setClass(getBaseContext(), ScannerActivity.class);
					break;
				case 4:
					intent.setClass(getBaseContext(), AboutActivity.class);
					break;
				default:
					break;
				}
				startActivity(intent);
			}
		});

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.open_drawer,
				R.string.close_drawer) {

			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
				getActionBar().setTitle(R.string.app_name);
			}

			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				getActionBar().setTitle(R.string.app_name);
			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void setContentView(final int layoutResID) {
		RelativeLayout activityContent = (RelativeLayout) mDrawerLayout
				.findViewById(R.id.content_frame);
		getLayoutInflater().inflate(layoutResID, activityContent, true);
		super.setContentView(mDrawerLayout);
	}
}
