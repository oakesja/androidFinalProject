package com.example.scanitgrocerystorehelper.adapters;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.example.scanitgrocerystorehelper.R;
import com.example.scanitgrocerystorehelper.models.ListItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListItemArrayAdapter extends BaseAdapter {
    private static ArrayList<ListItem> searchArrayList;
    
    private LayoutInflater mInflater;
 
    public ListItemArrayAdapter(Context context, ArrayList<ListItem> results) {
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
 
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.custom_row_view, null);
            holder = new ViewHolder();
            holder.txtName = (TextView) convertView.findViewById(R.id.customRowItem1);
            holder.txtQuantity = (TextView) convertView
                    .findViewById(R.id.customRowItem2);
            holder.txtPrice = (TextView) convertView.findViewById(R.id.customRowItem3);
 
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
 
        holder.txtName.setText(searchArrayList.get(position).getName());
        holder.txtQuantity.setText("" + searchArrayList.get(position).getQuantity());
        if(searchArrayList.get(position).getPrice().equals(new BigDecimal(0))){
            holder.txtPrice.setText("-");
        }else{
            holder.txtPrice.setText("$" + searchArrayList.get(position).getPrice());
        }
 
        return convertView;
    }
 
    static class ViewHolder {
        TextView txtName;
        TextView txtQuantity;
        TextView txtPrice;
    }
}
