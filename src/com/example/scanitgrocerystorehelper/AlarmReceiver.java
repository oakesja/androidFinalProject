package com.example.scanitgrocerystorehelper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
	public static final String REMINDER_INTENT_KEY = "REMINDER_INTENT_KEY";

	// TODO move alarm classes to a different package, update manifest after,
	// create broadcast receiver that remakes all alarms from shared preferences
	// somehow after boot

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent myIntent = new Intent(context, NotificationService.class);
		String reminderText = intent.getStringExtra(REMINDER_INTENT_KEY);
		myIntent.putExtra(REMINDER_INTENT_KEY, reminderText);
		context.startService(myIntent);
		Log.d(DrawerActivity.SCANIT, "recieved from alarm " + reminderText);
	}

}
