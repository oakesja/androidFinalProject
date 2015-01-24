package com.example.scanitgrocerystorehelper;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.List;


public class DrawerArrayAdapter extends ArrayAdapter<String> {
	TypedArray mDrawableIds;
			
	public DrawerArrayAdapter(Context context, String[] options, TypedArray drawableIds) {
		super(context, R.layout.drawer_list_item, R.id.drawerItemName, options);
		mDrawableIds = drawableIds;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v =  super.getView(position, convertView, parent);
		ImageView iv = (ImageView)v.findViewById(R.id.drawerItemImage);
		iv.setImageResource(mDrawableIds.getResourceId(position, -1));
		return v;
	}

}
