package com.example.scanitgrocerystorehelper.models;

public abstract class Reminder {

	private int month;
	private int day;
	private int year;
	private boolean willNotify;

	public Reminder(int month, int day, int year) {
		this.month = month;
		this.day = day;
		this.year = year;
		this.willNotify = false;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public boolean isWillNotify() {
		return willNotify;
	}

	public void setWillNotify(boolean willNotify) {
		this.willNotify = willNotify;
	}
}
