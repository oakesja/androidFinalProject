package com.example.scanitgrocerystorehelper.models;

public class ExpirationReminder extends Reminder {

	private String foodName;

	public ExpirationReminder(int month, int day, int year, String foodName) {
		super(month, day, year);
		this.foodName = foodName;
	}

	@Override
	public String toString() {
		return "Your " + this.foodName;
	}
}
