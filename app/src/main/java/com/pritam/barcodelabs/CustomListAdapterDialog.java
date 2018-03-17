package com.pritam.barcodelabs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Pritam on 3/17/2018.
 */

public class CustomListAdapterDialog extends BaseAdapter {

    private ArrayList<HashMap<String,String>> listData;

    private LayoutInflater layoutInflater;

    public CustomListAdapterDialog(Context context, ArrayList<HashMap<String,String>> listData) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_row_dialog, null);
            holder = new ViewHolder();
            holder.barcodeData = (TextView) convertView.findViewById(R.id.data);
            holder.barcodeType = (TextView) convertView.findViewById(R.id.type);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.barcodeData.setText(listData.get(position).get("data"));
        holder.barcodeType.setText(listData.get(position).get("type"));

        return convertView;
    }

    static class ViewHolder {
        TextView barcodeData;
        TextView barcodeType;
    }

}
