package com.example.scanitgrocerystorehelper.models;

import java.util.GregorianCalendar;

import android.content.Context;

public class GeneralReminder extends Reminder {

	private String name;
	private GregorianCalendar calendar;

	public GeneralReminder(Context context, int month, int day, int year, int hour, int minute,
			String name) {
		super(context, new GregorianCalendar(year, month, day, hour, minute));
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.name + " - " + super.getFormmatedDate() + " " + getHour() + ":" + getMinute();
	}
}
