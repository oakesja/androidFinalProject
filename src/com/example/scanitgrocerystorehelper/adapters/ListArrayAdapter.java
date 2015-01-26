package com.example.scanitgrocerystorehelper.adapters;

import java.util.ArrayList;

import com.example.scanitgrocerystorehelper.R;
import com.example.scanitgrocerystorehelper.models.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListArrayAdapter extends BaseAdapter {
    private static ArrayList<List> searchArrayList;
    
    private LayoutInflater mInflater;
 
    public ListArrayAdapter(Context context, ArrayList<List> results) {
        searchArrayList = results;
        mInflater = LayoutInflater.from(context);
    }
 
    public int getCount() {
        return searchArrayList.size();
    }
 
    public Object getItem(int position) {
        return searchArrayList.get(position);
    }
 
    public long getItemId(int position) {
        return position;
    }
 
    @SuppressLint("InflateParams")
	public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.custom_row_view, null);
            holder = new ViewHolder();
            holder.txtName = (TextView) convertView.findViewById(R.id.name);
            holder.txtDescription = (TextView) convertView
                    .findViewById(R.id.cityState);
 
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
 
        holder.txtName.setText(searchArrayList.get(position).getName());
        holder.txtDescription.setText(searchArrayList.get(position).getDescription());
 
        return convertView;
    }
 
    static class ViewHolder {
        TextView txtName;
        TextView txtDescription;
    }
}
