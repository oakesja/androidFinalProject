package com.example.scanitgrocerystorehelper.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.scanitgrocerystorehelper.DrawerActivity;
import com.example.scanitgrocerystorehelper.R;
import com.example.scanitgrocerystorehelper.ReminderActivity;

public class NotificationCreator {
	Context mContext;
	public NotificationCreator(Context context) {
		mContext = context;
	}
	
	private final long[] vibrationPattern = { 0, 1000 };

	public void createNotifcation(String contentText) {
		Intent resultIntent = new Intent(mContext, ReminderActivity.class);

		Notification.Builder builder = new Notification.Builder(mContext);
		builder.setSmallIcon(R.drawable.ic_launcher);
		builder.setContentTitle(mContext.getString(R.string.notifcation_title));
		builder.setContentText(contentText);
		builder.setAutoCancel(true);
		builder.setVibrate(vibrationPattern);
		
		Log.d(DrawerActivity.SCANIT, "notification builder " + contentText);

		TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);
		stackBuilder.addParentStack(ReminderActivity.class);
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
				PendingIntent.FLAG_UPDATE_CURRENT);
		builder.setContentIntent(resultPendingIntent);
		NotificationManager notifcationManger = (NotificationManager) mContext
				.getSystemService(Context.NOTIFICATION_SERVICE);
		notifcationManger.notify(SharedPreferencesManager.getNextNotificationId(mContext), builder.build());
	}
}
