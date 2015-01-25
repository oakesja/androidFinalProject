package com.example.scanitgrocerystorehelper.models;

import java.util.GregorianCalendar;
import java.util.Locale;

import android.content.Context;

public abstract class Reminder implements Comparable<Reminder> {

	private Context context;
	private GregorianCalendar calendar;
	private boolean willNotify;

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
	
	public String getFormmatedDate(){
		Locale l = context.getResources().getConfiguration().locale;
		StringBuilder sb = new StringBuilder();
		sb.append(calendar.getDisplayName(GregorianCalendar.MONTH, GregorianCalendar.SHORT, l));
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

	
}
