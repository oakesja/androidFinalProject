package com.example.scanitgrocerystorehelper.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public interface IContentValueizer {
	public ContentValues getContentValue();
	// TODO consider removing context here
	public Object getFromCursor(Context context, Cursor cursor);
}
