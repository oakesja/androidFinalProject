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
import com.example.scanitgrocerystorehelper.models.ListItem;

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

	public void updateList(GroceryList list) {
		ContentValues row = list.getContentValue();
		mDatabase.update(SqlAdapterKeys.LIST_TABLE, row, SqlAdapterKeys.KEY_ID
				+ " == " + list.getId(), null);
	}

	public void setAllLists(ArrayList<GroceryList> lists) {
		lists.clear();
		Cursor cursor = mDatabase.query(SqlAdapterKeys.LIST_TABLE, null, null,
				null, null, null, null);
		if (cursor == null || !cursor.moveToFirst()) {
			return;
		}
		do {
			GroceryList l = new GroceryList();
			l.getFromCursor(mContext, cursor);
			lists.add(l);
		} while (cursor.moveToNext());
		Collections.sort(lists);
	}

	public void deleteList(GroceryList list) {
		mDatabase.delete(SqlAdapterKeys.LIST_TABLE, SqlAdapterKeys.KEY_ID
				+ " = " + list.getId(), null);
		mDatabase.delete(SqlAdapterKeys.LIST_ITEMS_TABLE,
				SqlAdapterKeys.KEY_LIST_ID + " == " + list.getId(), null);
	}

	public GroceryList getList(long id) {
		Cursor cursor = mDatabase.query(SqlAdapterKeys.LIST_TABLE, null,
				SqlAdapterKeys.KEY_ID + " == " + id, null, null, null, null);
		if (cursor == null || !cursor.moveToFirst()) {
			return null;
		}
		GroceryList l = new GroceryList();
		l.getFromCursor(mContext, cursor);
		return l;
	}

	public void addListItem(ListItem listItem) {
		ContentValues row = listItem.getContentValue();
		long rowId = mDatabase.insert(SqlAdapterKeys.LIST_ITEMS_TABLE, null,
				row);
		listItem.setId(rowId);
	}

	public void setListItems(ArrayList<ListItem> items, long listId) {
		items.clear();
		Cursor cursor = mDatabase.query(SqlAdapterKeys.LIST_ITEMS_TABLE, null,
				SqlAdapterKeys.KEY_LIST_ID + " == " + listId, null, null, null,
				null);
		if (cursor == null || !cursor.moveToFirst()) {
			return;
		}
		do {
			ListItem l = new ListItem();
			l.getFromCursor(mContext, cursor);
			items.add(l);
		} while (cursor.moveToNext());
		Collections.sort(items);
	}

	public void deleteListItem(ListItem listItem) {
		mDatabase.delete(SqlAdapterKeys.LIST_ITEMS_TABLE, SqlAdapterKeys.KEY_ID
				+ " = " + listItem.getId(), null);
	}
}
