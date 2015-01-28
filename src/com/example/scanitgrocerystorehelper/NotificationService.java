package com.example.scanitgrocerystorehelper;

import com.example.scanitgrocerystorehelper.R;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class NotificationService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		String reminderName = intent
				.getStringExtra(AlarmReceiver.REMINDER_INTENT_KEY);
		Log.d(DrawerActivity.SCANIT, "started service " + reminderName);
		super.onStartCommand(intent, flags, startId);
		NotificationManager notManager = (NotificationManager) this
				.getApplicationContext().getSystemService(
						Activity.NOTIFICATION_SERVICE);

		Intent resultIntent = new Intent(this.getApplicationContext(),
				ReminderActivity.class);

		Notification.Builder builder = new Notification.Builder(
				getApplicationContext());
		builder.setSmallIcon(R.drawable.ic_launcher);
		builder.setContentTitle(getApplicationContext().getString(
				R.string.notifcation_title));
		builder.setContentText(reminderName);
		builder.setAutoCancel(true);

		TaskStackBuilder stackBuilder = TaskStackBuilder
				.create(getApplicationContext());
		stackBuilder.addParentStack(ReminderActivity.class);
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
				PendingIntent.FLAG_UPDATE_CURRENT);
		builder.setContentIntent(resultPendingIntent);
		NotificationManager notifcationManger = (NotificationManager) getApplicationContext()
				.getSystemService(Context.NOTIFICATION_SERVICE);
		notifcationManger.notify(0, builder.build());
		return START_NOT_STICKY;
	}
}
