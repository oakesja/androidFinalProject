package com.example.scanitgrocerystorehelper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent myIntent = new Intent(context, NotificationService.class);
		context.startService(myIntent);
		Log.d(DrawerActivity.SCANIT, "recieved from alarm");
	}

}
