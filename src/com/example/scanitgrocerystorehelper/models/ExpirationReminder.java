package com.example.scanitgrocerystorehelper.models;

import java.util.GregorianCalendar;

import com.example.scanitgrocerystorehelper.adapters.ReminderSqlAdapterKeys;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class ExpirationReminder extends Reminder {

	private String foodName;

	public ExpirationReminder() {
		super();
	}

	public ExpirationReminder(Context context, int month, int day, int year,
			String foodName) {
		super(context, new GregorianCalendar(year, month, day));
		this.foodName = foodName;
	}

	public String getFoodName() {
		return foodName;
	}

	public void setFoodName(String foodName) {
		this.foodName = foodName;
	}

	@Override
	public String toString() {
		return "Your " + this.foodName + " expires on "
				+ super.getFormmatedDate();
	}

	@Override
	public ContentValues getContentValue() {
		ContentValues row = super.getContentValue();
		row.put(ReminderSqlAdapterKeys.KEY_NAME, this.foodName);
		return row;
	}

	@Override
	public Reminder getFromCursor(Context context, Cursor cursor) {
		super.setFromCursor(context, cursor);
		String name = cursor.getString(cursor
				.getColumnIndexOrThrow(ReminderSqlAdapterKeys.KEY_NAME));
		this.foodName = name;
		return this;
	}
}
