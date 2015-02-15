package com.example.scanitgrocerystorehelper.models;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import com.example.scanitgrocerystorehelper.DrawerActivity;
import com.example.scanitgrocerystorehelper.adapters.sql.SqlAdapterKeys;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class GeneralReminder extends Reminder {

	private String name;

	public GeneralReminder(Context context, int month, int day, int year,
			int hour, int minute, String name) {
		super(context, new GregorianCalendar(year, month, day, hour, minute));
		this.name = name;
		Log.d(DrawerActivity.SCANIT, "" + hour + ":" + minute);
	}

	public GeneralReminder() {
	};

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, yyy hh:mm a");
		sdf.setCalendar(getCalendar());
		return this.name + " - " + sdf.format(getCalendar().getTime());
	}

	@Override
	public ContentValues getContentValue() {
		ContentValues row = super.getContentValue();
		row.put(SqlAdapterKeys.KEY_NAME, this.name);
		return row;
	}

	@Override
	public Reminder getFromCursor(Context context, Cursor cursor) {
		super.setFromCursor(context, cursor);
		String name = cursor.getString(cursor
				.getColumnIndexOrThrow(SqlAdapterKeys.KEY_NAME));
		this.name = name;
		return this;
	}

	@Override
	public String getNotifcationText() {
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
		sdf.setCalendar(getCalendar());
		return this.name + " @ " + sdf.format(getCalendar().getTime());
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Parcelable.Creator<GeneralReminder> CREATOR = new Parcelable.Creator<GeneralReminder>() {
		public GeneralReminder createFromParcel(Parcel in) {
			return new GeneralReminder(in);
		}

		public GeneralReminder[] newArray(int size) {
			return new GeneralReminder[size];
		}
	};

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeString(name);
	}

	public GeneralReminder(Parcel in) {
		super(in);
		name = in.readString();
	}
}
