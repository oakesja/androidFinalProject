package com.example.scanitgrocerystorehelper;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import com.example.scanitgrocerystorehelper.adapters.ReminderArrayAdapter;
import com.example.scanitgrocerystorehelper.adapters.sql.ReminderSqlAdapter;
import com.example.scanitgrocerystorehelper.models.ExpirationReminder;
import com.example.scanitgrocerystorehelper.models.GeneralReminder;
import com.example.scanitgrocerystorehelper.models.GroceryList;
import com.example.scanitgrocerystorehelper.models.Reminder;
import com.example.scanitgrocerystorehelper.receivers.AlarmReceiver;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.app.Service;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;

public class ReminderActivity extends DrawerActivity {

	private ArrayList<Reminder> mReminders;
	private ArrayAdapter<Reminder> mAdapter;
	private ReminderSqlAdapter mSqlAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reminder);

		mReminders = new ArrayList<Reminder>();

		mSqlAdapter = new ReminderSqlAdapter(this);
		mSqlAdapter.open();
		mSqlAdapter.setAllReminders(mReminders);

		mAdapter = new ReminderArrayAdapter(this, mReminders);
		ListView listview = (ListView) findViewById(R.id.reminders);
		listview.setAdapter(mAdapter);

		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Reminder r = mAdapter.getItem(position);
				if (r.getClass() == ExpirationReminder.class) {
					enterExpirationReminderInfoDialog((ExpirationReminder) r);
				} else {
					enterGeneralReminderInfoDialog((GeneralReminder) r);
				}
			}
		});

		listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
		listview.setMultiChoiceModeListener(new MultiChoiceModeListener() {
			ArrayList<Reminder> mChosenReminders;
			Menu mMenu;

			@Override
			public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
				return false;
			}

			@Override
			public void onDestroyActionMode(ActionMode mode) {

			}

			@Override
			public boolean onCreateActionMode(ActionMode mode, Menu menu) {
				getMenuInflater().inflate(R.menu.edit_delete_contextual_menu,
						menu);
				mChosenReminders = new ArrayList<Reminder>();
				mMenu = menu;
				mode.setTitle(R.string.reminder_contextual_title);
				((MenuItem) mMenu.findItem(R.id.edit)).setVisible(false);
				((MenuItem) mMenu.findItem(R.id.edit)).setEnabled(false);
				return true;
			}

			@Override
			public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
				switch (item.getItemId()) {
				case R.id.delete:
					showDeleteReminderDialog(mChosenReminders);
					return true;
				default:
					return false;
				}
			}

			@Override
			public void onItemCheckedStateChanged(ActionMode mode,
					int position, long id, boolean checked) {
				Reminder item = mAdapter.getItem(position);
				if (checked) {
					mChosenReminders.add(item);
				} else {
					mChosenReminders.remove(item);
				}
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mSqlAdapter.close();
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
							enterExpirationReminderInfoDialog(null);
							break;
						case 1:
							enterGeneralReminderInfoDialog(null);
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

	// TODO abstract these out using template pattern
	@SuppressLint("InflateParams")
	private void enterExpirationReminderInfoDialog(
			final ExpirationReminder reminderToUpdate) {
		DialogFragment df = new DialogFragment() {
			public Dialog onCreateDialog(Bundle savedInstanceState) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());
				final View v = getLayoutInflater().inflate(
						R.layout.dialog_reminder, null);

				final EditText editName = (EditText) v
						.findViewById(R.id.reminderNameEditText);
				String posButtonText = "";
				if (reminderToUpdate != null) {
					builder.setTitle(R.string.edit_reminder);
					editName.setText(reminderToUpdate.getFoodName());
					posButtonText = getString(R.string.update);
				} else {
					builder.setTitle(R.string.create_reminder);
					editName.setHint(R.string.hint_general_reminder);
					posButtonText = getString(R.string.add);
				}

				final TimePicker timePicker = (TimePicker) v
						.findViewById(R.id.reminderTimePicker);
				timePicker.setVisibility(TimePicker.GONE);

				final DatePicker datePicker = (DatePicker) v
						.findViewById(R.id.reminderDatePicker);
				datePicker.setMinDate(System.currentTimeMillis() - 1000);
				if (reminderToUpdate != null) {
					datePicker.updateDate(reminderToUpdate.getYear(),
							reminderToUpdate.getMonth(),
							reminderToUpdate.getDay());
				}

				builder.setView(v);
				builder.setNegativeButton(android.R.string.cancel, null);
				builder.setPositiveButton(posButtonText, null);
				Dialog dialog = builder.create();
				dialog.setOnShowListener(new OnShowListener() {

					@Override
					public void onShow(DialogInterface dialog) {
						final DialogInterface d = dialog;
						Button b = ((AlertDialog) dialog)
								.getButton(AlertDialog.BUTTON_POSITIVE);
						b.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View view) {
								int month = datePicker.getMonth();
								int day = datePicker.getDayOfMonth();
								int year = datePicker.getYear();
								String foodName = editName.getText().toString();
								if (foodName.length() > 0) {
									if (reminderToUpdate != null) {
										reminderToUpdate.setFoodName(foodName);
										reminderToUpdate.setMonth(month);
										reminderToUpdate.setDay(day);
										reminderToUpdate.setYear(year);
										updateReminder(reminderToUpdate);
									} else {
										ExpirationReminder reminder = new ExpirationReminder(
												getActivity(), month, day,
												year, foodName);
										addReminder(reminder);
									}
									d.dismiss();
								} else {
									editName.setError(getString(R.string.reminder_name_error));
								}
							}
						});
					}
				});
				return dialog;
			}
		};
		df.show(getFragmentManager(), "reminder info");
	}

	private void enterGeneralReminderInfoDialog(
			final GeneralReminder reminderToUpdate) {
		DialogFragment df = new DialogFragment() {
			@SuppressLint("InflateParams")
			@Override
			public Dialog onCreateDialog(Bundle savedInstanceState) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());
				final View v = getLayoutInflater().inflate(
						R.layout.dialog_reminder, null);

				final EditText editName = (EditText) v
						.findViewById(R.id.reminderNameEditText);
				
				String posButtonText = "";
				if (reminderToUpdate != null) {
					builder.setTitle(R.string.edit_reminder);
					editName.setText(reminderToUpdate.getName());
					posButtonText = getString(R.string.update);
				} else {
					builder.setTitle(R.string.create_reminder);
					editName.setHint(R.string.hint_general_reminder);
					posButtonText = getString(R.string.add);
				}

				final TimePicker timePicker = (TimePicker) v
						.findViewById(R.id.reminderTimePicker);
				if (reminderToUpdate != null) {
					timePicker.setCurrentHour(reminderToUpdate.getHour());
					timePicker.setCurrentMinute(reminderToUpdate.getMinute());
				}

				final DatePicker datePicker = (DatePicker) v
						.findViewById(R.id.reminderDatePicker);
				datePicker.setMinDate(System.currentTimeMillis() - 1000);
				if (reminderToUpdate != null) {
					datePicker.updateDate(reminderToUpdate.getYear(),
							reminderToUpdate.getMonth(),
							reminderToUpdate.getDay());
				}
				builder.setView(v);
				builder.setNegativeButton(android.R.string.cancel, null);
				builder.setPositiveButton(posButtonText, new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				});
				Dialog dialog = builder.create();
				dialog.setOnShowListener(new OnShowListener() {

					@Override
					public void onShow(DialogInterface dialog) {
						final DialogInterface d = dialog;
						Button b = ((AlertDialog) dialog)
								.getButton(AlertDialog.BUTTON_POSITIVE);
						b.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View view) {
								int month = datePicker.getMonth();
								int day = datePicker.getDayOfMonth();
								int year = datePicker.getYear();
								int hour = timePicker.getCurrentHour();
								int minute = timePicker.getCurrentMinute();
								String name = editName.getText().toString();
								Log.d(DrawerActivity.SCANIT, "" + hour + ":"
										+ minute);
								if (name.length() > 0) {
									if (reminderToUpdate != null) {
										reminderToUpdate.setName(name);
										reminderToUpdate.setMonth(month);
										reminderToUpdate.setDay(day);
										reminderToUpdate.setYear(year);
										reminderToUpdate.setHour(hour);
										reminderToUpdate.setMinute(minute);
										updateReminder(reminderToUpdate);
									} else {
										GeneralReminder reminder = new GeneralReminder(
												getActivity(), month, day,
												year, hour, minute, name);
										addReminder(reminder);
									}
									d.dismiss();
								} else {
									editName.setError(getString(R.string.reminder_name_error));
								}
							}
						});
					}
				});
				return dialog;
			}
		};
		df.show(getFragmentManager(), "reminder info");
	}

	public void showDeleteReminderDialog(
			final ArrayList<Reminder> remindersToDelete) {
		DialogFragment df = new DialogFragment() {
			@Override
			public Dialog onCreateDialog(Bundle savedInstanceState) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());
				builder.setTitle(R.string.confirm_delete_title);
				builder.setMessage(R.string.confirm_delete_message_reminder);
				builder.setNegativeButton(android.R.string.cancel, null);
				builder.setPositiveButton(android.R.string.ok,
						new OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								for (Reminder reminder : remindersToDelete) {
									deleteReminder(reminder);
								}
							}
						});
				return builder.create();
			}
		};
		df.show(getFragmentManager(), "delete friend");
	}

	private void addReminder(Reminder r) {
		mSqlAdapter.addReminder(r);
		mSqlAdapter.setAllReminders(mReminders);
		mAdapter.notifyDataSetChanged();
	}

	private void deleteReminder(Reminder r) {
		mSqlAdapter.deleteReminder(r);
		mSqlAdapter.setAllReminders(mReminders);
		mAdapter.notifyDataSetChanged();
	}

	public void updateReminder(Reminder r) {
		mSqlAdapter.updateReminder(r);
		updateNotifcations(r);
		mSqlAdapter.setAllReminders(mReminders);
		mAdapter.notifyDataSetChanged();
	}

	private void updateNotifcations(Reminder reminderToUpdate) {
		if (reminderToUpdate.isWillNotify()) {
			Log.d(DrawerActivity.SCANIT,
					"update notifcation " + reminderToUpdate.getDay()
							+ reminderToUpdate.getNotifcationText());
			Intent myIntent = new Intent(this, AlarmReceiver.class);
			myIntent.putExtra(AlarmReceiver.REMINDER_KEY, reminderToUpdate);

			PendingIntent pendingIntent = PendingIntent.getBroadcast(this,
					reminderToUpdate.getPendingIntentId(), myIntent,
					PendingIntent.FLAG_UPDATE_CURRENT);
			AlarmManager alarmManager = (AlarmManager) this
					.getSystemService(Service.ALARM_SERVICE);

			GregorianCalendar gc = reminderToUpdate.getCalendar();
			gc.set(GregorianCalendar.SECOND, 0);

			alarmManager.set(AlarmManager.RTC, gc.getTimeInMillis(),
					pendingIntent);
		}
	}
}
