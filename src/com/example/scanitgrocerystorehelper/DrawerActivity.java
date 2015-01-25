package com.example.scanitgrocerystorehelper;

import com.example.scanitgrocerystorehelper.adapters.DrawerArrayAdapter;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.SparseIntArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

public abstract class DrawerActivity extends Activity {
	public static final String SCANIT = "SCANIT";

	// need to update to v7 version
	private ActionBarDrawerToggle mDrawerToggle;
	protected DrawerLayout mDrawerLayout;
	protected ListView mDrawerList;
	private SparseIntArray mLayoutTitleLookup;
	private TypedArray mDrawableIds;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mDrawerLayout = (DrawerLayout) getLayoutInflater().inflate(
				R.layout.activity_drawer, null);

		mDrawerList = (ListView) mDrawerLayout.findViewById(R.id.left_drawer);

		String[] options = getResources().getStringArray(R.array.drawer_names);
		mDrawableIds = getResources().obtainTypedArray(R.array.drawer_images);
		mDrawerList.setAdapter(new DrawerArrayAdapter(this, options,
				mDrawableIds));
		mDrawerList.setOnItemClickListener(new OnItemClickListener() {

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
				// getActionBar().setTitle(R.string.app_name);
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				// getActionBar().setTitle(R.string.app_name);
				invalidateOptionsMenu();
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
		intializeTitleLookup();
		RelativeLayout activityContent = (RelativeLayout) mDrawerLayout
				.findViewById(R.id.content_frame);
		getLayoutInflater().inflate(layoutResID, activityContent, true);
		getActionBar().setTitle(mLayoutTitleLookup.get(layoutResID));
		super.setContentView(mDrawerLayout);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mDrawableIds.recycle();
	}

	private void intializeTitleLookup() {
		mLayoutTitleLookup = new SparseIntArray();
		mLayoutTitleLookup.append(R.layout.activity_main,
				R.string.title_activity_main);
		mLayoutTitleLookup.append(R.layout.activity_about,
				R.string.title_activity_about);
		mLayoutTitleLookup.append(R.layout.activity_coupon,
				R.string.title_activity_coupon);
		mLayoutTitleLookup.append(R.layout.activity_list,
				R.string.title_activity_list);
		mLayoutTitleLookup.append(R.layout.activity_reminder,
				R.string.title_activity_reminder);
		mLayoutTitleLookup.append(R.layout.activity_scanner,
				R.string.title_activity_scanner);
	}
}
