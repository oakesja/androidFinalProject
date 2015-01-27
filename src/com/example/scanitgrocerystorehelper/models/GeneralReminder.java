package com.example.scanitgrocerystorehelper.models;

import java.util.GregorianCalendar;

import com.example.scanitgrocerystorehelper.adapters.ReminderSqlAdapterKeys;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class GeneralReminder extends Reminder {

	private String name;

	public GeneralReminder(Context context, int month, int day, int year,
			int hour, int minute, String name) {
		super(context, new GregorianCalendar(year, month, day, hour, minute));
		this.name = name;
	}

	public GeneralReminder() {
	};

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.name + " - " + super.getFormmatedDate() + " " + getHour()
				+ ":" + getMinute();
	}

	@Override
	public ContentValues getContentValue() {
		ContentValues row = super.getContentValue();
		row.put(ReminderSqlAdapterKeys.KEY_NAME, this.name);
		return row;
	}

	@Override
	public Reminder getFromCursor(Context context, Cursor cursor) {
		super.setFromCursor(context, cursor);
		String name = cursor.getString(cursor
				.getColumnIndexOrThrow(ReminderSqlAdapterKeys.KEY_NAME));
		this.name = name;
		return this;
	}
}
