package com.example.scanitgrocerystorehelper.models;

import java.util.GregorianCalendar;

import com.example.scanitgrocerystorehelper.adapters.ReminderSqlAdapterKeys;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public abstract class Reminder implements Comparable<Reminder>,
		ContentValueizer, Notifcationizer {

	private Context context;
	private GregorianCalendar calendar;
	private boolean willNotify;
	private long id;

	public Reminder(Context context, GregorianCalendar calendar) {
		this.calendar = calendar;
		this.willNotify = false;
		this.context = context;
	}

	public Reminder() {
	}

	public GregorianCalendar getCalendar() {
		return calendar;
	}

	public void setCalendar(GregorianCalendar calendar) {
		this.calendar = calendar;
	}

	public int getMonth() {
		return calendar.get(GregorianCalendar.MONTH);
	}

	public void setMonth(int month) {
		calendar.set(GregorianCalendar.MONTH, month);
	}

	public int getDay() {
		return calendar.get(GregorianCalendar.DATE);
	}

	public void setDay(int day) {
		calendar.set(GregorianCalendar.DATE, day);
	}

	public int getYear() {
		return calendar.get(GregorianCalendar.YEAR);
	}

	public void setYear(int year) {
		calendar.set(GregorianCalendar.YEAR, year);
	}

	public int getHour() {
		return calendar.get(GregorianCalendar.HOUR_OF_DAY);
	}

	public void setHour(int hour) {
		calendar.set(GregorianCalendar.HOUR_OF_DAY, hour);
	}

	public int getMinute() {
		return calendar.get(GregorianCalendar.MINUTE);
	}

	public void setMinute(int minute) {
		calendar.set(GregorianCalendar.MINUTE, minute);
	}

	public boolean isWillNotify() {
		return willNotify;
	}

	public void setWillNotify(boolean willNotify) {
		this.willNotify = willNotify;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	@Override
	public int compareTo(Reminder another) {
		GregorianCalendar anotherDueDate = another.getCalendar();
		return calendar.compareTo(anotherDueDate);
	}

	public ContentValues getContentValue() {
		ContentValues row = new ContentValues();
		row.put(ReminderSqlAdapterKeys.KEY_YEAR, getYear());
		row.put(ReminderSqlAdapterKeys.KEY_MONTH, getMonth());
		row.put(ReminderSqlAdapterKeys.KEY_DAY, getDay());
		row.put(ReminderSqlAdapterKeys.KEY_HOUR, getHour());
		row.put(ReminderSqlAdapterKeys.KEY_MINUTE, getMinute());
		row.put(ReminderSqlAdapterKeys.KEY_NOTIFY, isWillNotify());
		return row;
	}

	public void setFromCursor(Context context, Cursor cursor) {
		this.context = context;
		this.id = cursor.getLong(cursor
				.getColumnIndexOrThrow(ReminderSqlAdapterKeys.KEY_ID));
		int year = cursor.getInt(cursor
				.getColumnIndexOrThrow(ReminderSqlAdapterKeys.KEY_YEAR));
		int month = cursor.getInt(cursor
				.getColumnIndexOrThrow(ReminderSqlAdapterKeys.KEY_MONTH));
		int day = cursor.getInt(cursor
				.getColumnIndexOrThrow(ReminderSqlAdapterKeys.KEY_DAY));
		int hour = cursor.getInt(cursor
				.getColumnIndexOrThrow(ReminderSqlAdapterKeys.KEY_HOUR));
		int minute = cursor.getInt(cursor
				.getColumnIndexOrThrow(ReminderSqlAdapterKeys.KEY_MINUTE));
		this.willNotify = (cursor.getInt(cursor
				.getColumnIndexOrThrow(ReminderSqlAdapterKeys.KEY_NOTIFY)) != 0);
		this.calendar = new GregorianCalendar(year, month, day, hour, minute);
	}
}
