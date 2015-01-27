package com.example.scanitgrocerystorehelper.adapters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;
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

	private static final String DATABASE_NAME = "reminders.db";
	private static final String GENERAL_TABLE = "general";
	private static final String EXP_TABLE = "exp";
	private static final int DATABASE_VERSION = 1;

	private SQLiteDatabase mDatabase;
	private SQLiteOpenHelper mHelper;
	private Context mContext;
	private HashMap<Class<?>, String> mTableNameLookup;

	public ReminderSqlAdapter(Context context) {
		mHelper = new TaskDbHelper(context);
		mContext = context;
		mTableNameLookup = new HashMap<Class<?>, String>();
		mTableNameLookup.put(GeneralReminder.class, GENERAL_TABLE);
		mTableNameLookup.put(ExpirationReminder.class, EXP_TABLE);
	}

	public void open() {
		mDatabase = mHelper.getReadableDatabase();
	}

	public void close() {
		mDatabase.close();
	}

	public void addReminder(Reminder reminder) {
		Log.d(DrawerActivity.SCANIT, reminder.getClass().getName());
		ContentValues row = reminder.getContentValue();
		String table = mTableNameLookup.get(reminder.getClass());
		long rowId = mDatabase.insert(table, null, row);
		reminder.setId(rowId);
	}

	public void deleteReminder(Reminder reminder) {
		String table = mTableNameLookup.get(reminder.getClass());
		mDatabase.delete(table, ReminderSqlAdapterKeys.KEY_ID + " = " + reminder.getId(), null);
	}

	public void setAllReminder(ArrayList<Reminder> reminders) {
		String[] columns = null;
		Cursor cursor = mDatabase.query(TABLE_NAME, columns, null, null, null,
				null, null);
		if (cursor == null || !cursor.moveToFirst()) {
			return;
		}
//		reminders.clear();
		while (cursor.moveToNext()) {
			getTaskFromCursor(cursor);
//			reminders.add();
		}
//		Collections.sort(reminders);
	}

	public Reminder getTaskFromCursor(Cursor cursor) {
		return Reminder.getFromCursor(mContext, cursor);
	}

	private static class TaskDbHelper extends SQLiteOpenHelper {
		private static final String DROP_STATEMENT1 = "DROP TABLE IF EXISTS "
				+ GENERAL_TABLE;
		private static final String DROP_STATEMENT2 = "DROP TABLE IF EXISTS "
				+ EXP_TABLE;
		private static final String CREATE_STATEMENT1;
		static {
			StringBuilder sb = new StringBuilder();
			sb.append("CREATE TABLE " + GENERAL_TABLE + "(");
			sb.append(ReminderSqlAdapterKeys.KEY_ID + " integer primary key autoincrement, ");
			sb.append(ReminderSqlAdapterKeys.KEY_NAME + " text, ");
			sb.append(ReminderSqlAdapterKeys.KEY_YEAR + " integer, ");
			sb.append(ReminderSqlAdapterKeys.KEY_MONTH + " integer, ");
			sb.append(ReminderSqlAdapterKeys.KEY_DAY + " integer, ");
			sb.append(ReminderSqlAdapterKeys.KEY_HOUR + " integer, ");
			sb.append(ReminderSqlAdapterKeys.KEY_MINUTE + " integer, ");
			sb.append(ReminderSqlAdapterKeys.KEY_NOTIFY + " integer ");
			sb.append(")");
			CREATE_STATEMENT1 = sb.toString();
		}
		
		private static final String CREATE_STATEMENT2;
		static {
			StringBuilder sb = new StringBuilder();
			sb.append("CREATE TABLE " + EXP_TABLE + "(");
			sb.append(ReminderSqlAdapterKeys.KEY_ID + " integer primary key autoincrement, ");
			sb.append(ReminderSqlAdapterKeys.KEY_NAME + " text, ");
			sb.append(ReminderSqlAdapterKeys.KEY_YEAR + " integer, ");
			sb.append(ReminderSqlAdapterKeys.KEY_MONTH + " integer, ");
			sb.append(ReminderSqlAdapterKeys.KEY_DAY + " integer, ");
			sb.append(ReminderSqlAdapterKeys.KEY_HOUR + " integer, ");
			sb.append(ReminderSqlAdapterKeys.KEY_MINUTE + " integer, ");
			sb.append(ReminderSqlAdapterKeys.KEY_NOTIFY + " integer ");
			sb.append(")");
			CREATE_STATEMENT2 = sb.toString();
		}

		public TaskDbHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(CREATE_STATEMENT1);
			db.execSQL(CREATE_STATEMENT2);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL(DROP_STATEMENT1);
			db.execSQL(CREATE_STATEMENT1);
			db.execSQL(DROP_STATEMENT2);
			db.execSQL(CREATE_STATEMENT2);
		}

	}
}
