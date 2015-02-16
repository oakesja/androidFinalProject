package com.example.scanitgrocerystorehelper.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import com.example.scanitgrocerystorehelper.adapters.sql.SqlAdapterKeys;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class GroceryList implements IContentValueizer, Comparable<GroceryList> {

	private long id;
	private String name;
	private String description;
	private GregorianCalendar modified;

	public GroceryList() {

	}

	public GroceryList(String name, String description) {
		this.modified = new GregorianCalendar();
		this.name = name;
		this.description = description;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public String getDescription() {
		return this.description;
	}

	public GregorianCalendar getDateModified() {
		return this.modified;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setDateModified(GregorianCalendar modified) {
		this.modified = modified;
	}

	public String toString() {
		// This is ugly
		String ret = "";
		ret += name;
		ret += ";" + description;
		ret += ";";
		// for (ListItem l : this.list) {
		// ret += l.toString() + "/";
		// }
		ret.substring(0, ret.length() - 1);
		ret += ";";
		ret += ";" + modified;

		return ret;
	}

	public GroceryList fromString(String s) {
		String[] splitted = s.split(";");
		ArrayList<ListItem> nList = new ArrayList<ListItem>();
		GroceryList ret = new GroceryList(splitted[0], splitted[1]);
		String[] lItems = splitted[2].split("/");
		for (String nItem : lItems) {
			String[] item = nItem.split(",");
			nList.add(new ListItem(item[0], Integer.parseInt(item[1]),
					new BigDecimal(item[2]), this.getId()));
		}
		// ret.setDateCreated(new Date(splitted[3]));
		// ret.setDateModified(new Date(splitted[4]));
		// ret.setAuthor(splitted[5]);
		return ret;
	}

	@Override
	public ContentValues getContentValue() {
		ContentValues row = new ContentValues();
		row.put(SqlAdapterKeys.KEY_NAME, this.name);
		row.put(SqlAdapterKeys.KEY_DESC, this.description);
		row.put(SqlAdapterKeys.KEY_YEAR,
				this.modified.get(GregorianCalendar.YEAR));
		row.put(SqlAdapterKeys.KEY_MONTH,
				this.modified.get(GregorianCalendar.MONTH));
		row.put(SqlAdapterKeys.KEY_DAY,
				this.modified.get(GregorianCalendar.DATE));
		row.put(SqlAdapterKeys.KEY_HOUR,
				this.modified.get(GregorianCalendar.HOUR));
		row.put(SqlAdapterKeys.KEY_MINUTE,
				this.modified.get(GregorianCalendar.MINUTE));
		return row;
	}

	@Override
	public Object getFromCursor(Context context, Cursor cursor) {
		this.name = cursor.getString(cursor
				.getColumnIndexOrThrow(SqlAdapterKeys.KEY_NAME));
		this.description = cursor.getString(cursor
				.getColumnIndexOrThrow(SqlAdapterKeys.KEY_DESC));
		int year = cursor.getInt(cursor
				.getColumnIndexOrThrow(SqlAdapterKeys.KEY_YEAR));
		int month = cursor.getInt(cursor
				.getColumnIndexOrThrow(SqlAdapterKeys.KEY_MONTH));
		int day = cursor.getInt(cursor
				.getColumnIndexOrThrow(SqlAdapterKeys.KEY_DAY));
		int hour = cursor.getInt(cursor
				.getColumnIndexOrThrow(SqlAdapterKeys.KEY_HOUR));
		int minute = cursor.getInt(cursor
				.getColumnIndexOrThrow(SqlAdapterKeys.KEY_MINUTE));
		this.id = cursor.getLong(cursor
				.getColumnIndexOrThrow(SqlAdapterKeys.KEY_ID));
		this.modified = new GregorianCalendar(year, month, day, hour, minute);
		return this;
	}

	@Override
	public int compareTo(GroceryList another) {
		int comp = this.getDateModified().compareTo(another.getDateModified());
		return (comp == 0) ? this.name.compareTo(another.name) : comp;
	}
}
