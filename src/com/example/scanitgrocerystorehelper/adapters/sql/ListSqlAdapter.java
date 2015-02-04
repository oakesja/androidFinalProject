package com.example.scanitgrocerystorehelper.adapters.sql;

import java.util.ArrayList;
import java.util.Collections;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.scanitgrocerystorehelper.DrawerActivity;
import com.example.scanitgrocerystorehelper.models.GroceryList;

public class ListSqlAdapter {
	private SQLiteDatabase mDatabase;
	private SQLiteOpenHelper mHelper;
	private Context mContext;

	public ListSqlAdapter(Context context) {
		mHelper = new TaskDbHelper(context);
		mContext = context;
	}

	public void open() {
		mDatabase = mHelper.getReadableDatabase();
	}

	public void close() {
		mDatabase.close();
	}

	public void addList(GroceryList list) {
		ContentValues row = list.getContentValue();
		long rowId = mDatabase.insert(SqlAdapterKeys.LIST_TABLE, null, row);
		list.setId(rowId);
	}

	public void setAllLists(ArrayList<GroceryList> lists) {
		lists.clear();
		Cursor cursor = mDatabase.query(SqlAdapterKeys.LIST_TABLE, null, null,
				null, null, null, null);
		if (cursor == null) {
			return;
		}
		while (cursor.moveToNext()) {
			GroceryList l = new GroceryList();
			l.getFromCursor(mContext, cursor);
			lists.add(l);
		}
		Collections.sort(lists);
	}

	public void deleteList(GroceryList list) {
		int x = mDatabase.delete(SqlAdapterKeys.LIST_TABLE, SqlAdapterKeys.KEY_ID
				+ " = " + list.getId(), null);
		
		Log.d(DrawerActivity.SCANIT, " " + x + " " + list.getId());
		//TODO delete all items too
	}
}
