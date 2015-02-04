package com.example.scanitgrocerystorehelper.services;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import com.example.scanitgrocerystorehelper.DrawerActivity;
import com.example.scanitgrocerystorehelper.adapters.sql.ReminderSqlAdapter;
import com.example.scanitgrocerystorehelper.models.Reminder;
import com.example.scanitgrocerystorehelper.receivers.AlarmReceiver;
import com.example.scanitgrocerystorehelper.utils.SharedPreferencesManager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class BootService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(DrawerActivity.SCANIT, "started boot service");
		super.onStartCommand(intent, flags, startId);
		SharedPreferencesManager.resetIds(getApplicationContext());
		ReminderSqlAdapter mDatabaseAdapter = new ReminderSqlAdapter(
				getApplicationContext());
		mDatabaseAdapter.open();
		ArrayList<Reminder> reminders = mDatabaseAdapter
				.getAllRemindersToNotify();
		for (Reminder reminder : reminders) {
			Log.d(DrawerActivity.SCANIT,
					"creating notification " + reminder.getNotifcationText());
			createPendingNotification(reminder);
		}
		mDatabaseAdapter.close();
		return START_NOT_STICKY;
	}

	private void createPendingNotification(Reminder reminder) {
		Log.d(DrawerActivity.SCANIT,
				"Boot service create PI " + reminder.getNotifcationText());
		reminder.resetPendintIntentId();

		Intent myIntent = new Intent(getApplicationContext(),
				AlarmReceiver.class);
		myIntent.putExtra(AlarmReceiver.REMINDER_KEY, reminder);

		PendingIntent pendingIntent = PendingIntent.getBroadcast(
				getApplicationContext(), reminder.getPendingIntentId(),
				myIntent, 0);
		Log.d(DrawerActivity.SCANIT,
				"Boot service create PI " + reminder.getNotifcationText()
						+ reminder.getPendingIntentId());

		AlarmManager alarmManager = (AlarmManager) getApplication()
				.getSystemService(Service.ALARM_SERVICE);
		GregorianCalendar gc = reminder.getCalendar();
		gc.set(GregorianCalendar.SECOND, 0);

		alarmManager.set(AlarmManager.RTC, gc.getTimeInMillis(), pendingIntent);
	}
}
