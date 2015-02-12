package com.example.scanitgrocerystorehelper.adapters;

import java.util.List;

import com.example.scanitgrocerystorehelper.models.ListItem;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ShoppingActivityArrayAdapter extends ArrayAdapter<ListItem> {

	public ShoppingActivityArrayAdapter(Context context, List<ListItem> items) {
		super(context, android.R.layout.simple_list_item_1, android.R.id.text1,
				items);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = super.getView(position, convertView, parent);
		ListItem item = getItem(position);
		String s = item.getQuantity() + " - " + item.getName();
		TextView tv = (TextView) v.findViewById(android.R.id.text1);
		tv.setText(s);
		if (item.isCheckedOff()) {
			tv.setPaintFlags(tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
		} else {
			tv.setPaintFlags(0);
		}
		return v;
	}

}
