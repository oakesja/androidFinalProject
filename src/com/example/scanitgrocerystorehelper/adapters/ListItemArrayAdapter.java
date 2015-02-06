package com.example.scanitgrocerystorehelper.adapters;

import java.math.BigDecimal;
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
		super(context, R.layout.custom_row_view, R.id.customRowItem1, listItems);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View v = super.getView(position, convertView, parent);
		ListItem item = getItem(position);
		((TextView) v.findViewById(R.id.customRowItem1))
				.setText(item.getName());
		((TextView) v.findViewById(R.id.customRowItem2)).setText(""
				+ item.getQuantity());
		String price = (item.getPrice().equals(new BigDecimal(0))) ? "-" : "$"
				+ item.getPrice();
		((TextView) v.findViewById(R.id.customRowItem3)).setText(price);
		return v;
	}
}
