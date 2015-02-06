package com.example.scanitgrocerystorehelper.adapters;

import java.text.SimpleDateFormat;
import java.util.List;

import com.example.scanitgrocerystorehelper.R;
import com.example.scanitgrocerystorehelper.models.GroceryList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ListArrayAdapter extends ArrayAdapter<GroceryList> {

	public ListArrayAdapter(Context context, List<GroceryList> lists) {
		super(context, R.layout.custom_row_view, R.id.customRowItem1, lists);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View v = super.getView(position, convertView, parent);
		GroceryList gl = getItem(position);
		((TextView) v.findViewById(R.id.customRowItem1)).setText(gl.getName());
		((TextView) v.findViewById(R.id.customRowItem2)).setText(gl.getDescription());
//		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm aaa");
//		sdf.setCalendar(gl.getDateModified());
//		String time = sdf.format(gl.getDateModified().getTime());
//		String s = getContext().getString(R.string.last_modified, time);
//		((TextView) v.findViewById(R.id.customRowItem3)).setText(s);
		return v;
	}
}
