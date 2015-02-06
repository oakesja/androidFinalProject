package com.example.scanitgrocerystorehelper.adapters.sql;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import com.example.scanitgrocerystorehelper.DrawerActivity;
import com.example.scanitgrocerystorehelper.models.ExpirationReminder;
import com.example.scanitgrocerystorehelper.models.GeneralReminder;
import com.example.scanitgrocerystorehelper.models.Reminder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ReminderSqlAdapter {
	private SQLiteDatabase mDatabase;
	private SQLiteOpenHelper mHelper;
	private Context mContext;
	private HashMap<Class<?>, String> mTableNameLookup;

	public ReminderSqlAdapter(Context context) {
		mHelper = new TaskDbHelper(context);
		mContext = context;
		mTableNameLookup = new HashMap<Class<?>, String>();
		mTableNameLookup.put(GeneralReminder.class,
				SqlAdapterKeys.GENERAL_TABLE);
		mTableNameLookup
				.put(ExpirationReminder.class, SqlAdapterKeys.EXP_TABLE);
	}

	public void open() {
		mDatabase = mHelper.getReadableDatabase();
	}

	public void close() {
		mDatabase.close();
	}

	public void addReminder(Reminder reminder) {
		ContentValues row = reminder.getContentValue();
		String table = mTableNameLookup.get(reminder.getClass());
		long rowId = mDatabase.insert(table, null, row);
		reminder.setId(rowId);
	}

	public void updateReminder(Reminder reminder) {
		ContentValues row = reminder.getContentValue();
		String table = mTableNameLookup.get(reminder.getClass());
		String selection = SqlAdapterKeys.KEY_ID + " = " + reminder.getId();
		mDatabase.update(table, row, selection, null);
	}

	public void deleteReminder(Reminder reminder) {
		String table = mTableNameLookup.get(reminder.getClass());
		mDatabase.delete(table,
				SqlAdapterKeys.KEY_ID + " = " + reminder.getId(), null);
	}

	public void setAllReminders(ArrayList<Reminder> reminders) {
		String[] columns = null;
		reminders.clear();
		for (Class<?> c : mTableNameLookup.keySet()) {
			String table = mTableNameLookup.get(c);
			Cursor cursor = mDatabase.query(table, columns, null, null, null,
					null, null);
			if (cursor == null) {
				return;
			}
			while (cursor.moveToNext()) {
				Reminder r = getTaskFromCursor(c, cursor);
				reminders.add(r);
			}
		}
		Collections.sort(reminders);
	}

	public ArrayList<Reminder> getAllRemindersToNotify() {
		ArrayList<Reminder> reminders = new ArrayList<Reminder>();
		for (Class<?> c : mTableNameLookup.keySet()) {
			Log.d(DrawerActivity.SCANIT,
					"get all reminders for " + c.toString());
			String table = mTableNameLookup.get(c);
			Cursor cursor = mDatabase.query(table, null,
					SqlAdapterKeys.KEY_NOTIFY + "=1", null, null, null, null);
			if (cursor == null) {
				Log.d(DrawerActivity.SCANIT, "none found");
				continue;
			}
			while (cursor.moveToNext()) {
				Reminder r = getTaskFromCursor(c, cursor);
				Log.d(DrawerActivity.SCANIT, "found " + r);
				reminders.add(r);
			}
		}
		return reminders;
	}

	public Reminder getTaskFromCursor(Class<?> c, Cursor cursor) {
		Object t;
		try {
			t = c.newInstance();
			Method method = c.getDeclaredMethod("getFromCursor", Context.class,
					Cursor.class);
			return (Reminder) method.invoke(t, mContext, cursor);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
}
