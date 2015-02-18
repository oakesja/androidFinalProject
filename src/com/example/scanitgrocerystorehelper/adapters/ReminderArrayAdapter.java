package com.example.scanitgrocerystorehelper.adapters;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ReminderArrayAdapter extends ArrayAdapter<Reminder> {
	private Context mContext;

	public ReminderArrayAdapter(Context context, List<Reminder> reminders) {
		super(context, R.layout.reminder_list_item, R.id.reminderName,
				reminders);
		mContext = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Reminder r = getItem(position);
		View row = null;
		if (r.hasReminderTime()) {
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.reminder_with_time_list_item, null);
		} else {
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.reminder_list_item, null);
		}
		if (r.hasReminderTime()) {
			TextView timeTv = (TextView) row.findViewById(R.id.reminderTime);
			timeTv.setText(r.getReminderTime());
		}
		((TextView)row.findViewById(R.id.reminderName)).setText(r.toString());
		ImageView iv = (ImageView) row.findViewById(R.id.reminderAlarm);

		int drawableId = (r.isWillNotify()) ? R.drawable.ic_action_alarm_on
				: R.drawable.ic_action_alarm_add;
		iv.setImageResource(drawableId);
		iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				r.setWillNotify(!r.isWillNotify());
				checkCancelReminder(r);
				ReminderActivity ra = (ReminderActivity) mContext;
				ra.updateReminder(r);
			}
		});
		return row;
	}

	private void checkCancelReminder(Reminder r) {
		Log.d(DrawerActivity.SCANIT, "handle notification");
		Intent myIntent = new Intent(mContext, AlarmReceiver.class);
		myIntent.putExtra(AlarmReceiver.REMINDER_KEY, r);

		PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext,
				r.getPendingIntentId(), myIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager alarmManager = (AlarmManager) mContext
				.getSystemService(Service.ALARM_SERVICE);

		if (pendingIntent != null && !r.isWillNotify()) {
			Log.d(DrawerActivity.SCANIT, "deleting alarm");
			alarmManager.cancel(pendingIntent);
			pendingIntent.cancel();
		}
	}

}
