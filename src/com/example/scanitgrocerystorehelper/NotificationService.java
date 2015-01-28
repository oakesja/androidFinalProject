package com.example.scanitgrocerystorehelper;

import com.example.scanitgrocerystorehelper.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class NotificationService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		Log.d(DrawerActivity.SCANIT, "started service");
		super.onStart(intent, startId);
		NotificationManager notManager = (NotificationManager) this
				.getApplicationContext().getSystemService(
						this.getApplicationContext().NOTIFICATION_SERVICE);

		Intent intent1 = new Intent(this.getApplicationContext(),
				ReminderActivity.class);

		Notification notification = new Notification(R.drawable.ic_launcher,
				"See My App something for you", System.currentTimeMillis());

		intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
				| Intent.FLAG_ACTIVITY_CLEAR_TOP);

		PendingIntent pendingNotificationIntent = PendingIntent.getActivity(
				this.getApplicationContext(), 0, intent1,
				PendingIntent.FLAG_UPDATE_CURRENT);

		notification.flags |= Notification.FLAG_AUTO_CANCEL;

		notification.setLatestEventInfo(this.getApplicationContext(),
				"SANBOOK", "See My App something for you",
				pendingNotificationIntent);

		notManager.notify(0, notification);
	}

}
