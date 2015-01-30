package com.example.scanitgrocerystorehelper.services;

import com.example.scanitgrocerystorehelper.DrawerActivity;
import com.example.scanitgrocerystorehelper.adapters.sql.ReminderSqlAdapter;
import com.example.scanitgrocerystorehelper.models.Reminder;
import com.example.scanitgrocerystorehelper.receivers.AlarmReceiver;
import com.example.scanitgrocerystorehelper.utils.NotificationCreator;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;

public class NotificationService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		Reminder r = intent.getParcelableExtra(AlarmReceiver.REMINDER_KEY);
		r.setContext(getApplicationContext());
		Log.d(DrawerActivity.SCANIT, "Notification serverice on start command " + r.getNotifcationText());
		new NotificationCreator(getApplicationContext()).createNotifcation(r
				.getNotifcationText());
		ReminderSqlAdapter sqlAdapter = new ReminderSqlAdapter(
				getApplicationContext());
		sqlAdapter.open();
		r.setWillNotify(false);
		sqlAdapter.updateReminder(r);
		sqlAdapter.close();
		return START_NOT_STICKY;
	}
}
