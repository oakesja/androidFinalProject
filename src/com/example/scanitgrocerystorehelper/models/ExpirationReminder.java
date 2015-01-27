package com.example.scanitgrocerystorehelper.models;

import java.util.GregorianCalendar;

import com.example.scanitgrocerystorehelper.DrawerActivity;
import com.example.scanitgrocerystorehelper.R;
import com.example.scanitgrocerystorehelper.adapters.ReminderSqlAdapterKeys;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class ExpirationReminder extends Reminder {

	private String foodName;
	private static final double MILLISECONDS_IN_A_DAY = 86400000;

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
		long now = new GregorianCalendar().getTimeInMillis();
		long exp = super.getCalendar().getTimeInMillis();
		int daysDiff = (int) Math.ceil((exp - now) / MILLISECONDS_IN_A_DAY);
		if (daysDiff < 0) {
			return super.getContext().getResources()
					.getString(R.string.expired_formated, this.foodName);
		} else if (daysDiff == 0) {
			return super.getContext().getResources()
					.getString(R.string.expires_today, this.foodName);
		} else if (daysDiff == 1) {
			return super.getContext().getResources()
					.getString(R.string.expires_tomorrow, this.foodName);
		}
		return super.getContext().getResources()
				.getString(R.string.expires_formated, this.foodName, daysDiff);
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
