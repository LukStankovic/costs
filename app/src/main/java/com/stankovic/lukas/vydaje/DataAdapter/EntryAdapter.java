package com.stankovic.lukas.vydaje.DataAdapter;

import java.util.List;
import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.stankovic.lukas.vydaje.Model.Entry;
import com.stankovic.lukas.vydaje.R;

public class EntryAdapter extends ArrayAdapter<Entry>{

    Context context;
    int layoutResourceId;
    List<Entry> data = null;


    public EntryAdapter(Context context, int layoutResourceId, List<Entry> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        EntryHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new EntryHolder();
            holder.txtName = (TextView)row.findViewById(R.id.txtName);
            holder.txtAmount = (TextView)row.findViewById(R.id.txtAmount);
            holder.txtDateTime = (TextView)row.findViewById(R.id.txtDateTime);
            holder.txtCategory = (TextView) row.findViewById(R.id.txtCategory);

            row.setTag(holder);
        }
        else
        {
            holder = (EntryHolder)row.getTag();
        }
        Entry entry = data.get(position);
        holder.txtName.setText(entry.name);
        holder.txtCategory.setText(entry.categoryName);
        holder.txtAmount.setText(Double.toString(entry.amount));
        holder.txtDateTime.setText(entry.datetime);

        return row;
    }


    static class EntryHolder
    {
        TextView txtAmount;
        TextView txtName;
        TextView txtDateTime;
        TextView txtCategory;
    }
}
