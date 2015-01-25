package com.example.scanitgrocerystorehelper.adapters;

import java.util.List;

import com.example.scanitgrocerystorehelper.R;
import com.example.scanitgrocerystorehelper.models.Reminder;

import android.content.Context;
import android.widget.ArrayAdapter;

public class ReminderArrayAdapter extends ArrayAdapter<Reminder> {

	public ReminderArrayAdapter(Context context, List<Reminder> reminders) {
		super(context, R.layout.reminder_list_item, R.id.reminderName,
				reminders);
	}

}
