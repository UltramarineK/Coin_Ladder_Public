package com.example.real_laddergame;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.app.Application;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;


public class LineViewArrayAdapter  extends ArrayAdapter<ResultListitem> {
    ArrayList<ResultListitem> items;
    Context mCtx;
    int layout;

    public LineViewArrayAdapter(Context context, int layout, int textViewResourceId, ArrayList<ResultListitem> datas) {
        super(context, layout, textViewResourceId, datas);
        this.items = datas;
        this.mCtx = context;
        this.layout = layout;
    }

    public LineViewArrayAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) mCtx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(layout, null);
        }
        ResultListitem list_items = items.get(position);
        if (list_items != null) {
            ((TextView)v.findViewById(R.id.res_name)).setText(list_items.getName());
            ((TextView)v.findViewById(R.id.res_result)).setText(list_items.getResult());
        }
        return v;
    }
}

