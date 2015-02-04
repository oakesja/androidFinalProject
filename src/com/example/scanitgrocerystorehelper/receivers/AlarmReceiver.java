package com.example.scanitgrocerystorehelper.receivers;

import com.example.scanitgrocerystorehelper.DrawerActivity;
import com.example.scanitgrocerystorehelper.models.Reminder;
import com.example.scanitgrocerystorehelper.services.NotificationService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
	public static final String REMINDER_KEY = "REMINDER_KEY";

	@Override
	public void onReceive(Context context, Intent intent) {
		Reminder r = intent.getParcelableExtra(REMINDER_KEY);
		Intent myIntent = new Intent(context, NotificationService.class);
		myIntent.putExtra(REMINDER_KEY, r);
		context.startService(myIntent);
	}

}
