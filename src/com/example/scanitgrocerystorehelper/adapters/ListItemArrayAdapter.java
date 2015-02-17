package com.example.scanitgrocerystorehelper.adapters;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import com.example.scanitgrocerystorehelper.R;
import com.example.scanitgrocerystorehelper.models.ListItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListItemArrayAdapter extends ArrayAdapter<ListItem> {

	public ListItemArrayAdapter(Context context, List<ListItem> listItems) {
		super(context, R.layout.item_row_view, R.id.titleRow, listItems);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View v = super.getView(position, convertView, parent);
		ListItem item = getItem(position);

		String s = (item.getQuantity() == -1) ? item.getName() : item
				.getQuantity() + " - " + item.getName();
		((TextView) v.findViewById(R.id.titleRow)).setText(s);

		TextView price = (TextView) v.findViewById(R.id.subtitleRow);
		if (item.getPrice().equals(new BigDecimal(0))) {
			price.setText("");
		} else {
			price.setText("$"
					+ item.getPrice().setScale(2, RoundingMode.HALF_UP));
		}
		return v;
	}
}
