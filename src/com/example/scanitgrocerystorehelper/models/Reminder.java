package com.example.scanitgrocerystorehelper.models;

import java.util.GregorianCalendar;

import com.example.scanitgrocerystorehelper.DrawerActivity;
import com.example.scanitgrocerystorehelper.adapters.sql.SqlAdapterKeys;
import com.example.scanitgrocerystorehelper.utils.SharedPreferencesManager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public abstract class Reminder implements Comparable<Reminder>,
		IContentValueizer, INotifcationizer, Parcelable {

	private Context context;
	private GregorianCalendar calendar;
	private boolean willNotify;
	private long id;
	private int pendingIntentId;

	public Reminder(Context context, GregorianCalendar calendar) {
		this.calendar = calendar;
		this.willNotify = false;
		this.context = context;
		this.pendingIntentId = -1;
	}

	public Reminder() {
	}

	public Reminder(Parcel in) {
		Log.d(DrawerActivity.SCANIT, "reminder create from parcel");
		this.id = in.readInt();
		int year = in.readInt();
		int month = in.readInt();
		int day = in.readInt();
		int hour = in.readInt();
		int minute = in.readInt();
		this.calendar = new GregorianCalendar(year, month, day, hour, minute);
		this.willNotify = in.readInt() != 0;
		this.pendingIntentId = in.readInt();
	}

	public void writeToParcel(Parcel out, int flags) {
		Log.d(DrawerActivity.SCANIT, "reminder write to parcel");
		out.writeInt((int) this.id);
		out.writeInt(getYear());
		out.writeInt(getMonth());
		out.writeInt(getDay());
		out.writeInt(getHour());
		out.writeInt(getMinute());
		int bool = isWillNotify() ? 1 : 0;
		out.writeInt(bool);
		out.writeInt(this.pendingIntentId);
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

	public void resetPendintIntentId(){
		pendingIntentId = -1;
	}
	
	public int getPendingIntentId() {
		if (pendingIntentId == -1) {
			pendingIntentId = SharedPreferencesManager
					.getNextPendingIntentId(getContext());
		}
		return pendingIntentId;
	}

	@Override
	public int compareTo(Reminder another) {
		GregorianCalendar anotherDueDate = another.getCalendar();
		return calendar.compareTo(anotherDueDate);
	}

	public ContentValues getContentValue() {
		ContentValues row = new ContentValues();
		row.put(SqlAdapterKeys.KEY_YEAR, getYear());
		row.put(SqlAdapterKeys.KEY_MONTH, getMonth());
		row.put(SqlAdapterKeys.KEY_DAY, getDay());
		row.put(SqlAdapterKeys.KEY_HOUR, getHour());
		row.put(SqlAdapterKeys.KEY_MINUTE, getMinute());
		row.put(SqlAdapterKeys.KEY_NOTIFY, isWillNotify());
		row.put(SqlAdapterKeys.KEY_PENDINGINTENTID, this.pendingIntentId);
		return row;
	}

	public void setFromCursor(Context context, Cursor cursor) {
		this.context = context;
		this.id = cursor.getLong(cursor
				.getColumnIndexOrThrow(SqlAdapterKeys.KEY_ID));
		int year = cursor.getInt(cursor
				.getColumnIndexOrThrow(SqlAdapterKeys.KEY_YEAR));
		int month = cursor.getInt(cursor
				.getColumnIndexOrThrow(SqlAdapterKeys.KEY_MONTH));
		int day = cursor.getInt(cursor
				.getColumnIndexOrThrow(SqlAdapterKeys.KEY_DAY));
		int hour = cursor.getInt(cursor
				.getColumnIndexOrThrow(SqlAdapterKeys.KEY_HOUR));
		int minute = cursor.getInt(cursor
				.getColumnIndexOrThrow(SqlAdapterKeys.KEY_MINUTE));
		this.willNotify = (cursor.getInt(cursor
				.getColumnIndexOrThrow(SqlAdapterKeys.KEY_NOTIFY)) != 0);
		this.calendar = new GregorianCalendar(year, month, day, hour, minute);
		this.pendingIntentId = cursor.getInt(cursor
				.getColumnIndexOrThrow(SqlAdapterKeys.KEY_PENDINGINTENTID));
	}
}
