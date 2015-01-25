package com.example.scanitgrocerystorehelper.models;

public class GeneralReminder extends Reminder {

	private String name;
	private int hour;
	private int minute;

	public GeneralReminder(int month, int day, int year, int hour, int minute,
			String name) {
		super(month, day, year);
		this.name = name;
		this.minute = minute;
		this.hour = hour;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}

	@Override
	public String toString() {
		return this.name;
	}
}
