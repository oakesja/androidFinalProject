package com.example.scanitgrocerystorehelper.models;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.GregorianCalendar;

import com.example.scanitgrocerystorehelper.adapters.sql.SqlAdapterKeys;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class ListItem implements Comparable<ListItem>, IContentValueizer {

	private long id;
	private long listId;
	private String itemName;
	private int quantity;
	private BigDecimal price;
	private GregorianCalendar addTime;
	private boolean checkedOff;

	public ListItem(String name, int quantity, long listId) {
		this.itemName = name;
		this.quantity = quantity;
		this.price = new BigDecimal(0).setScale(2, RoundingMode.HALF_UP);
		this.listId = listId;
		this.addTime = new GregorianCalendar();
		this.checkedOff = false;
	}

	public ListItem(String name, int quantity, BigDecimal price, long listId) {
		this.itemName = name;
		this.quantity = quantity;
		this.price = price.setScale(2, RoundingMode.HALF_UP);
		this.listId = listId;
		this.addTime = new GregorianCalendar();
		this.checkedOff = false;
	}

	public ListItem() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getListId() {
		return listId;
	}

	public void setListId(long listId) {
		this.listId = listId;
	}

	public GregorianCalendar getAddTime() {
		return addTime;
	}

	public void setAddTime(GregorianCalendar addTime) {
		this.addTime = addTime;
	}

	public void setName(String name) {
		this.itemName = name;
	}

	public String getName() {
		return this.itemName;
	}

	public void setQuantity(int qty) {
		this.quantity = qty;
	}

	public int getQuantity() {
		return this.quantity;
	}

	public void setPrice(BigDecimal price) {
		this.price = price.setScale(2, RoundingMode.HALF_UP);
	}

	public BigDecimal getPrice() {
		return this.price;
	}

	public String toString() {
		return itemName + "," + quantity + "," + price;
	}

	public boolean isCheckedOff() {
		return checkedOff;
	}

	public void setCheckedOff(boolean checkedOff) {
		this.checkedOff = checkedOff;
	}

	@Override
	public ContentValues getContentValue() {
		ContentValues row = new ContentValues();
		row.put(SqlAdapterKeys.KEY_NAME, this.itemName);
		row.put(SqlAdapterKeys.KEY_QUANTITY, this.quantity);
		row.put(SqlAdapterKeys.KEY_PRICE, this.price.doubleValue());
		row.put(SqlAdapterKeys.KEY_LIST_ID, this.listId);
		row.put(SqlAdapterKeys.KEY_YEAR,
				this.addTime.get(GregorianCalendar.YEAR));
		row.put(SqlAdapterKeys.KEY_MONTH,
				this.addTime.get(GregorianCalendar.MONTH));
		row.put(SqlAdapterKeys.KEY_DAY,
				this.addTime.get(GregorianCalendar.DATE));
		row.put(SqlAdapterKeys.KEY_HOUR,
				this.addTime.get(GregorianCalendar.HOUR));
		row.put(SqlAdapterKeys.KEY_MINUTE,
				this.addTime.get(GregorianCalendar.MINUTE));
		row.put(SqlAdapterKeys.KEY_SECOND,
				this.addTime.get(GregorianCalendar.SECOND));
		return row;
	}

	@Override
	public Object getFromCursor(Context context, Cursor cursor) {
		this.itemName = cursor.getString(cursor
				.getColumnIndexOrThrow(SqlAdapterKeys.KEY_NAME));
		this.quantity = cursor.getInt(cursor
				.getColumnIndexOrThrow(SqlAdapterKeys.KEY_QUANTITY));
		double price = cursor.getDouble(cursor
				.getColumnIndexOrThrow(SqlAdapterKeys.KEY_PRICE));
		this.price = new BigDecimal(price);
		this.listId = cursor.getLong(cursor
				.getColumnIndexOrThrow(SqlAdapterKeys.KEY_LIST_ID));
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
		int second = cursor.getInt(cursor
				.getColumnIndexOrThrow(SqlAdapterKeys.KEY_SECOND));
		this.id = cursor.getLong(cursor
				.getColumnIndexOrThrow(SqlAdapterKeys.KEY_ID));
		this.addTime = new GregorianCalendar(year, month, day, hour, minute,
				second);
		this.checkedOff = false;
		return this;
	}

	@Override
	public int compareTo(ListItem another) {
		return this.addTime.compareTo(another.addTime);
	}
}
