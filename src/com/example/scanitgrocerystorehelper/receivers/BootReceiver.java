package com.example.scanitgrocerystorehelper.receivers;

import com.example.scanitgrocerystorehelper.DrawerActivity;
import com.example.scanitgrocerystorehelper.services.BootService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
			Log.d(DrawerActivity.SCANIT, "starting boot service");
			Intent mServiceIntent = new Intent(context, BootService.class);
			context.startService(mServiceIntent);
		} else {
			Log.e(DrawerActivity.SCANIT,
					"Received unexpected intent " + intent.toString());
		}
	}

}
