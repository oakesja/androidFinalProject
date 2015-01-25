package com.example.scanitgrocerystorehelper.models;

import java.util.GregorianCalendar;

import android.content.Context;

public class ExpirationReminder extends Reminder {

	private String foodName;

	public ExpirationReminder(Context context, int month, int day, int year, String foodName) {
		super(context, new GregorianCalendar(year, month, day));
		this.foodName = foodName;
	}

	@Override
	public String toString() {
		return "Your " + this.foodName + " expires on " + super.getFormmatedDate();
	}
}
