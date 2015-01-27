package com.example.scanitgrocerystorehelper.models;

import android.content.ContentValues;
import android.database.Cursor;

public interface ContentValueizer {
	public ContentValues getContentValue();
	public Reminder getFromCursor(Cursor cursor);
}
