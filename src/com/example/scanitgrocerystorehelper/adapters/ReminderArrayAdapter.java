package com.example.scanitgrocerystorehelper.adapters;

import java.util.List;

import com.example.scanitgrocerystorehelper.R;
import com.example.scanitgrocerystorehelper.ReminderActivity;
import com.example.scanitgrocerystorehelper.models.ExpirationReminder;
import com.example.scanitgrocerystorehelper.models.GeneralReminder;
import com.example.scanitgrocerystorehelper.models.Reminder;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
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
				if (r.isWillNotify()) {
					createNotification(r);
				}
				ReminderActivity ra = (ReminderActivity)mContext;
				ra.updateReminder(r);
			}
		});
		return v;
	}

	private void createNotification(Reminder r) {
		Notification.Builder builder = new Notification.Builder(mContext);
		builder.setSmallIcon(R.drawable.ic_launcher);
		builder.setContentTitle(mContext.getString(R.string.notifcation_title));
		builder.setContentText(r.toString());
		builder.setAutoCancel(true);

		Intent resultIntent = new Intent(mContext, ReminderActivity.class);
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);
		stackBuilder.addParentStack(ReminderActivity.class);
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
				PendingIntent.FLAG_UPDATE_CURRENT);
		builder.setContentIntent(resultPendingIntent);
		NotificationManager notifcationManger = (NotificationManager) mContext
				.getSystemService(Context.NOTIFICATION_SERVICE);
		notifcationManger.notify((int) r.getId(), builder.build());
	}

}
