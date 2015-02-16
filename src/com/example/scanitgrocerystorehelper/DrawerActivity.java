package com.example.scanitgrocerystorehelper;

import java.util.ArrayList;

import com.example.scanitgrocerystorehelper.adapters.DrawerArrayAdapter;
import com.example.scanitgrocerystorehelper.adapters.sql.ListSqlAdapter;
import com.example.scanitgrocerystorehelper.dialogs.EnsureBarcodeOutputDialog;
import com.example.scanitgrocerystorehelper.models.GroceryList;
import com.example.scanitgrocerystorehelper.models.ListItem;
import com.example.scanitgrocerystorehelper.utils.BarcodeLookup;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

@SuppressWarnings("deprecation")
public abstract class DrawerActivity extends Activity {
	public static final String SCANIT = "SCANIT";

	// TODO need to update to v7 version
	private ActionBarDrawerToggle mDrawerToggle;
	protected DrawerLayout mDrawerLayout;
	protected ListView mDrawerList;
	private SparseIntArray mLayoutTitleLookup;
	private SparseArray<Class<?>> mIntentClassLookup;
	private TypedArray mDrawableIds;
	private ListSqlAdapter mSqlAdapter;

	@SuppressLint("InflateParams")
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
				if (position != 3) {
					intent.setClass(getBaseContext(),
							mIntentClassLookup.get(position));
					startActivity(intent);
				} else {
					mSqlAdapter = new ListSqlAdapter(DrawerActivity.this);
					mSqlAdapter.open();
					IntentIntegrator it = new IntentIntegrator(
							DrawerActivity.this);
					it.initiateScan();
					Log.d(SCANIT, "start scan drawer activity");
					mDrawerLayout.closeDrawer(Gravity.START);
				}
			}
		});

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.open_drawer,
				R.string.close_drawer) {

			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				invalidateOptionsMenu();
			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		intializeIntentClassLookup();
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
	}

	private void intializeIntentClassLookup() {
		mIntentClassLookup = new SparseArray<Class<?>>();
		mIntentClassLookup.put(0, MainActivity.class);
		mIntentClassLookup.put(1, CouponActivity.class);
		mIntentClassLookup.put(2, ReminderActivity.class);
		mIntentClassLookup.put(4, AboutActivity.class);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d(SCANIT, "request code is " + requestCode);
		if (requestCode == IntentIntegrator.REQUEST_CODE) {
			super.onActivityResult(requestCode, resultCode, data);
			IntentResult scanResult = IntentIntegrator.parseActivityResult(
					requestCode, resultCode, data);
			if (scanResult != null && scanResult.getContents() != null) {
				new BarcodeLookup(this).execute(
						new EnsureLookupSelectListDialog(this),
						scanResult.getContents());
			}
		}
	}

	private class EnsureLookupSelectListDialog extends
			EnsureBarcodeOutputDialog {

		public EnsureLookupSelectListDialog(Context context) {
			super(context);
		}

		public void handleClick(DialogInterface dialog, int which,
				String productName) {
			showSelectListDialog(productName);
		}

	}

	private void showSelectListDialog(final String productNameToAdd) {
		DialogFragment df = new DialogFragment() {
			@Override
			public Dialog onCreateDialog(Bundle savedInstanceState) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						DrawerActivity.this);
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
						mSqlAdapter.close();
					}
				});
				return builder.create();
			}
		};
		df.show(getFragmentManager(), "select list");
	}
}
