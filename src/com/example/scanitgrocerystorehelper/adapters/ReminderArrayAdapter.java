package com.example.scanitgrocerystorehelper.adapters;

import java.util.List;

import com.example.scanitgrocerystorehelper.R;
import com.example.scanitgrocerystorehelper.models.Reminder;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

public class ReminderArrayAdapter extends ArrayAdapter<Reminder> {

	public ReminderArrayAdapter(Context context, List<Reminder> reminders) {
		super(context, R.layout.reminder_list_item, R.id.reminderName,
				reminders);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v =  super.getView(position, convertView, parent);
		ImageView iv = (ImageView)v.findViewById(R.id.reminderAlarm);
		final Reminder r = getItem(position);
		int drawableId = (r.isWillNotify()) ? R.drawable.ic_action_alarm_on : R.drawable.ic_action_alarm_add;
		iv.setImageResource(drawableId);
		final ReminderArrayAdapter arrayAdapter = this;
		iv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				r.setWillNotify(!r.isWillNotify());
				arrayAdapter.notifyDataSetChanged();
			}
		});
		return v;
	}

}
