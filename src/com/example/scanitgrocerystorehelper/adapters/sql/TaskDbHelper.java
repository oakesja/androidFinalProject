package com.example.scanitgrocerystorehelper.adapters.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class TaskDbHelper extends SQLiteOpenHelper {
	private static final String DROP_STATEMENT1 = "DROP TABLE IF EXISTS "
			+ SqlAdapterKeys.GENERAL_TABLE;
	private static final String DROP_STATEMENT2 = "DROP TABLE IF EXISTS "
			+ SqlAdapterKeys.EXP_TABLE;
	private static final String DROP_STATEMENT3 = "DROP TABLE IF EXISTS "
			+ SqlAdapterKeys.LIST_TABLE;
	private static final String DROP_STATEMENT4 = "DROP TABLE IF EXISTS "
			+ SqlAdapterKeys.LIST_ITEMS_TABLE;
	private static final String CREATE_STATEMENT1;
	static {
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE " + SqlAdapterKeys.GENERAL_TABLE + "(");
		sb.append(SqlAdapterKeys.KEY_ID
				+ " integer primary key autoincrement, ");
		sb.append(SqlAdapterKeys.KEY_NAME + " text, ");
		sb.append(SqlAdapterKeys.KEY_YEAR + " integer, ");
		sb.append(SqlAdapterKeys.KEY_MONTH + " integer, ");
		sb.append(SqlAdapterKeys.KEY_DAY + " integer, ");
		sb.append(SqlAdapterKeys.KEY_HOUR + " integer, ");
		sb.append(SqlAdapterKeys.KEY_MINUTE + " integer, ");
		sb.append(SqlAdapterKeys.KEY_NOTIFY + " integer, ");
		sb.append(SqlAdapterKeys.KEY_PENDINGINTENTID + " integer ");
		sb.append(")");
		CREATE_STATEMENT1 = sb.toString();
	}

	private static final String CREATE_STATEMENT2;
	static {
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE " + SqlAdapterKeys.EXP_TABLE + "(");
		sb.append(SqlAdapterKeys.KEY_ID
				+ " integer primary key autoincrement, ");
		sb.append(SqlAdapterKeys.KEY_NAME + " text, ");
		sb.append(SqlAdapterKeys.KEY_YEAR + " integer, ");
		sb.append(SqlAdapterKeys.KEY_MONTH + " integer, ");
		sb.append(SqlAdapterKeys.KEY_DAY + " integer, ");
		sb.append(SqlAdapterKeys.KEY_HOUR + " integer, ");
		sb.append(SqlAdapterKeys.KEY_MINUTE + " integer, ");
		sb.append(SqlAdapterKeys.KEY_NOTIFY + " integer, ");
		sb.append(SqlAdapterKeys.KEY_PENDINGINTENTID + " integer ");
		sb.append(")");
		CREATE_STATEMENT2 = sb.toString();
	}

	private static final String CREATE_STATEMENT3;
	static {
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE " + SqlAdapterKeys.LIST_TABLE + "(");
		sb.append(SqlAdapterKeys.KEY_ID
				+ " integer primary key autoincrement, ");
		sb.append(SqlAdapterKeys.KEY_NAME + " text, ");
		sb.append(SqlAdapterKeys.KEY_DESC + " text, ");
		sb.append(SqlAdapterKeys.KEY_YEAR + " integer, ");
		sb.append(SqlAdapterKeys.KEY_MONTH + " integer, ");
		sb.append(SqlAdapterKeys.KEY_DAY + " integer, ");
		sb.append(SqlAdapterKeys.KEY_HOUR + " integer, ");
		sb.append(SqlAdapterKeys.KEY_MINUTE + " integer");
		sb.append(")");
		CREATE_STATEMENT3 = sb.toString();
	}

	private static final String CREATE_STATEMENT4;
	static {
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE " + SqlAdapterKeys.LIST_ITEMS_TABLE + "(");
		sb.append(SqlAdapterKeys.KEY_ID
				+ " integer primary key autoincrement, ");
		sb.append(SqlAdapterKeys.KEY_LIST_ID + " integer, ");
		sb.append(SqlAdapterKeys.KEY_NAME + " text, ");
		sb.append(SqlAdapterKeys.KEY_QUANTITY + " integer, ");
		sb.append(SqlAdapterKeys.KEY_PRICE + " real, ");
		sb.append(SqlAdapterKeys.KEY_YEAR + " integer, ");
		sb.append(SqlAdapterKeys.KEY_MONTH + " integer, ");
		sb.append(SqlAdapterKeys.KEY_DAY + " integer, ");
		sb.append(SqlAdapterKeys.KEY_HOUR + " integer, ");
		sb.append(SqlAdapterKeys.KEY_MINUTE + " integer, ");
		sb.append(SqlAdapterKeys.KEY_SECOND + " integer");
		sb.append(")");
		CREATE_STATEMENT4 = sb.toString();
	}
	public TaskDbHelper(Context context) {
		super(context, SqlAdapterKeys.DATABASE_NAME, null,
				SqlAdapterKeys.DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_STATEMENT1);
		db.execSQL(CREATE_STATEMENT2);
		db.execSQL(CREATE_STATEMENT3);
		db.execSQL(CREATE_STATEMENT4);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(DROP_STATEMENT1);
		db.execSQL(CREATE_STATEMENT1);
		db.execSQL(DROP_STATEMENT2);
		db.execSQL(CREATE_STATEMENT2);
		db.execSQL(DROP_STATEMENT3);
		db.execSQL(CREATE_STATEMENT3);
		db.execSQL(DROP_STATEMENT4);
		db.execSQL(CREATE_STATEMENT4);
	}
}
