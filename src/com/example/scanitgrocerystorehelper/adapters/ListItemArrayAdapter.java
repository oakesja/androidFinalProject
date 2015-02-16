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
		super(context, R.layout.custom_row_view, R.id.customRowItem1, listItems);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View v = super.getView(position, convertView, parent);
		ListItem item = getItem(position);
		
		//Truncate item names if too long
		String name = item.getName();
		if(name.length() > 40){
			name = name.substring(0, 37) + "...";
		}
		((TextView) v.findViewById(R.id.customRowItem1))
				.setText(name);
		String quant = (item.getQuantity() == -1) ? "-" : "" + item.getQuantity();
		((TextView) v.findViewById(R.id.customRowItem2)).setText(quant);
		String price = (item.getPrice().equals(new BigDecimal(0))) ? "-" : "$"
				+ item.getPrice().setScale(2, RoundingMode.HALF_UP);
		((TextView) v.findViewById(R.id.customRowItem3)).setText(price);
		return v;
	}
}
