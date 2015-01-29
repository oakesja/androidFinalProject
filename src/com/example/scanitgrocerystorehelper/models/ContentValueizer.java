package com.example.scanitgrocerystorehelper.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public interface ContentValueizer {
	public ContentValues getContentValue();
	public Reminder getFromCursor(Context context, Cursor cursor);
}
