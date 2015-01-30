package com.example.scanitgrocerystorehelper.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {
	private static final String SHARED_PREF_KEY = "com.example.com.scanitgrocerystoreheper.sharedprefs";
	private static final String NOTIFICATION_ID_KEY = "NOTIFICATION_ID_KEY";
	private static final String PENDINGINTENT_ID_KEY = "PENDINGINTENT_ID_KEY";

	public static final void resetIds(Context context){
		SharedPreferences sharedPref = context.getSharedPreferences(
				SHARED_PREF_KEY, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putInt(NOTIFICATION_ID_KEY, -1);
		editor.putInt(PENDINGINTENT_ID_KEY, -1);
		editor.commit();
	}

	public static int getNextNotificationId(Context context) {
		SharedPreferences sharedPref = context.getSharedPreferences(
				SHARED_PREF_KEY, Context.MODE_PRIVATE);
		int id = sharedPref.getInt(NOTIFICATION_ID_KEY, -1);
		id += 1;
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putInt(NOTIFICATION_ID_KEY, id);
		editor.commit();
		return id;
	}
	
	public static int getNextPendingIntentId(Context context) {
		SharedPreferences sharedPref = context.getSharedPreferences(
				SHARED_PREF_KEY, Context.MODE_PRIVATE);
		int id = sharedPref.getInt(PENDINGINTENT_ID_KEY, -1);
		id += 1;
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putInt(PENDINGINTENT_ID_KEY, id);
		editor.commit();
		return id;
	}
}
