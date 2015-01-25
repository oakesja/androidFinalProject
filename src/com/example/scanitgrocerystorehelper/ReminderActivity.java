package com.example.scanitgrocerystorehelper;

import java.util.ArrayList;

import com.example.scanitgrocerystorehelper.adapters.ReminderArrayAdapter;
import com.example.scanitgrocerystorehelper.models.ExpirationReminder;
import com.example.scanitgrocerystorehelper.models.GeneralReminder;
import com.example.scanitgrocerystorehelper.models.Reminder;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;

public class ReminderActivity extends DrawerActivity {

	private ArrayList<Reminder> mReminders;
	private ArrayAdapter<Reminder> mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reminder);
		getActionBar().setTitle(R.string.title_activity_reminder);

		mReminders = new ArrayList<Reminder>();

		mAdapter = new ReminderArrayAdapter(this, mReminders);
		ListView listview = (ListView) findViewById(R.id.reminders);
		listview.setAdapter(mAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.reminder_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		Log.d(DrawerActivity.SCANIT,
				"" + super.mDrawerLayout.isDrawerOpen(super.mDrawerList));
		boolean drawerOpen = super.mDrawerLayout
				.isDrawerOpen(super.mDrawerList);
		menu.findItem(R.id.addReminder).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.addReminder:
			selectReminderTypeDialog();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void selectReminderTypeDialog() {
		DialogFragment df = new DialogFragment() {
			@Override
			public Dialog onCreateDialog(Bundle savedInstanceState) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());
				builder.setTitle(R.string.select_reminder_type);
				builder.setItems(R.array.reminder_types, new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							enterExpirationReminderInfoDialog();
							break;
						case 1:
							enterGeneralReminderInfoDialog();
						default:
							break;
						}
						;
					}
				});
				return builder.create();
			}
		};
		df.show(getFragmentManager(), "reminder type");
	}

	// try to abstract common parts between the two dialogs
	private void enterExpirationReminderInfoDialog() {
		DialogFragment df = new DialogFragment() {
			@Override
			public Dialog onCreateDialog(Bundle savedInstanceState) {
				final View v = getLayoutInflater().inflate(
						R.layout.dialog_reminder, null);

				final EditText editName = (EditText) v
						.findViewById(R.id.reminderNameEditText);
				editName.setHint(R.string.hint_food_reminder);

				final TimePicker timePicker = (TimePicker) v
						.findViewById(R.id.reminderTimePicker);
				timePicker.setVisibility(TimePicker.INVISIBLE);

				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());
				builder.setView(v);
				builder.setTitle(R.string.create_reminder);
				builder.setNegativeButton(android.R.string.cancel, null);
				builder.setPositiveButton(R.string.add, new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						DatePicker datePicker = (DatePicker) v
								.findViewById(R.id.reminderDatePicker);
						int month = datePicker.getMonth();
						int day = datePicker.getDayOfMonth();
						int year = datePicker.getYear();
						String foodName = editName.getText().toString();
						ExpirationReminder reminder = new ExpirationReminder(
								month, day, year, foodName);
						addReminder(reminder);
					}
				});
				return builder.create();
			}
		};
		df.show(getFragmentManager(), "reminder info");
	}

	private void enterGeneralReminderInfoDialog() {
		DialogFragment df = new DialogFragment() {
			@Override
			public Dialog onCreateDialog(Bundle savedInstanceState) {
				final View v = getLayoutInflater().inflate(
						R.layout.dialog_reminder, null);

				final EditText editName = (EditText) v
						.findViewById(R.id.reminderNameEditText);
				editName.setHint(R.string.hint_general_reminder);

				final TimePicker timePicker = (TimePicker) v
						.findViewById(R.id.reminderTimePicker);

				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());
				builder.setView(v);
				builder.setTitle(R.string.create_reminder);
				builder.setNegativeButton(android.R.string.cancel, null);
				builder.setPositiveButton(R.string.add, new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						DatePicker datePicker = (DatePicker) v
								.findViewById(R.id.reminderDatePicker);
						int month = datePicker.getMonth();
						int day = datePicker.getDayOfMonth();
						int year = datePicker.getYear();
						int hour = timePicker.getCurrentHour();
						int minute = timePicker.getCurrentMinute();
						String name = editName.getText().toString();
						GeneralReminder reminder = new GeneralReminder(month,
								day, year, hour, minute, name);
						addReminder(reminder);
					}
				});
				return builder.create();
			}
		};
		df.show(getFragmentManager(), "reminder info");
	}

	private void addReminder(Reminder r) {
		mReminders.add(r);
		mAdapter.notifyDataSetChanged();
	}
}
