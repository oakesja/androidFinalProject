package com.example.scanitgrocerystorehelper.models;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.GregorianCalendar;
import java.util.Locale;

import com.example.scanitgrocerystorehelper.DrawerActivity;
import com.example.scanitgrocerystorehelper.adapters.ReminderSqlAdapterKeys;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;

public abstract class Reminder implements Comparable<Reminder>, ContentValueizer {

	private Context context;
	private GregorianCalendar calendar;
	private boolean willNotify;
	private long id;

	public Reminder(Context context, GregorianCalendar calendar) {
		this.calendar = calendar;
		this.willNotify = false;
		this.context = context;
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
		return calendar.get(GregorianCalendar.DAY_OF_WEEK);
	}

	public void setDay(int day) {
		calendar.set(GregorianCalendar.DAY_OF_WEEK, day);
	}

	public int getYear() {
		return calendar.get(GregorianCalendar.YEAR);
	}

	public void setYear(int year) {
		calendar.set(GregorianCalendar.YEAR, year);
	}

	public int getHour() {
		return calendar.get(GregorianCalendar.HOUR);
	}

	public void setHour(int hour) {
		calendar.set(GregorianCalendar.HOUR, hour);
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

	public String getFormmatedDate() {
		Locale l = context.getResources().getConfiguration().locale;
		StringBuilder sb = new StringBuilder();
		sb.append(calendar.getDisplayName(GregorianCalendar.MONTH,
				GregorianCalendar.SHORT, l));
		sb.append(" ");
		sb.append(getDay());
		sb.append(",");
		sb.append(getYear());
		return sb.toString();
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
	
	public static final Reminder getFromCursor(Context context, Cursor cursor) {
		long id = cursor.getLong(cursor.getColumnIndexOrThrow(ReminderSqlAdapterKeys.KEY_ID));
		int year = cursor.getInt(cursor.getColumnIndexOrThrow(ReminderSqlAdapterKeys.KEY_YEAR));
		int month = cursor.getInt(cursor.getColumnIndexOrThrow(ReminderSqlAdapterKeys.KEY_MONTH));
		int date = cursor.getInt(cursor.getColumnIndexOrThrow(ReminderSqlAdapterKeys.KEY_DAY));
		int hour = cursor.getInt(cursor.getColumnIndexOrThrow(ReminderSqlAdapterKeys.KEY_HOUR));
		int minute = cursor.getInt(cursor.getColumnIndexOrThrow(ReminderSqlAdapterKeys.KEY_MINUTE));
		boolean notify = (cursor.getInt(cursor.getColumnIndexOrThrow(ReminderSqlAdapterKeys.KEY_NOTIFY)) != 0);
		String name = cursor.getString(cursor.getColumnIndexOrThrow(ReminderSqlAdapterKeys.KEY_NAME));
		String type = cursor.getString(cursor.getColumnIndexOrThrow(ReminderSqlAdapterKeys.KEY_TYPE));
		try {
			Class<?> reminderType = Class.forName(type);
			Log.d(DrawerActivity.SCANIT, reminderType.toString());
			Constructor<?> ctor = reminderType.getConstructor(String.class);
			Object object = ctor.newInstance(name);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
//		reminderType.
//		reminder.setId(id);
		return null;
	}
}
