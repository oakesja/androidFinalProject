package com.example.scanitgrocerystorehelper.adapters;

import java.util.GregorianCalendar;
import java.util.List;

import com.example.scanitgrocerystorehelper.DrawerActivity;
import com.example.scanitgrocerystorehelper.R;
import com.example.scanitgrocerystorehelper.ReminderActivity;
import com.example.scanitgrocerystorehelper.models.Reminder;
import com.example.scanitgrocerystorehelper.receivers.AlarmReceiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

public class ReminderArrayAdapter extends ArrayAdapter<Reminder> {
	private Context mContext;

	public ReminderArrayAdapter(Context context, List<Reminder> reminders) {
		super(context, R.layout.reminder_list_item, R.id.reminderName,
				reminders);
		mContext = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = super.getView(position, convertView, parent);
		ImageView iv = (ImageView) v.findViewById(R.id.reminderAlarm);
		final Reminder r = getItem(position);
		int drawableId = (r.isWillNotify()) ? R.drawable.ic_action_alarm_on
				: R.drawable.ic_action_alarm_add;
		iv.setImageResource(drawableId);
		iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				r.setWillNotify(!r.isWillNotify());
				handleNotification(r);
				ReminderActivity ra = (ReminderActivity) mContext;
				ra.updateReminder(r);
			}
		});
		return v;
	}

	private void handleNotification(Reminder r) {
		Log.d(DrawerActivity.SCANIT, "handle notification");
		Intent myIntent = new Intent(mContext, AlarmReceiver.class);
		myIntent.putExtra(AlarmReceiver.REMINDER_KEY, r);

		PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext,
				r.getPendingIntentId(), myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager alarmManager = (AlarmManager) mContext
				.getSystemService(Service.ALARM_SERVICE);

		if (pendingIntent != null && !r.isWillNotify()) {
			Log.d(DrawerActivity.SCANIT, "deleting alarm");

			alarmManager.cancel(pendingIntent);
			pendingIntent.cancel();
		} else if (r.isWillNotify()) {
			Log.d(DrawerActivity.SCANIT, "creating alarm");

			GregorianCalendar gc = r.getCalendar();
			gc.set(GregorianCalendar.SECOND, 0);

			alarmManager.set(AlarmManager.RTC, gc.getTimeInMillis(),
					pendingIntent);
		}
	}

}
