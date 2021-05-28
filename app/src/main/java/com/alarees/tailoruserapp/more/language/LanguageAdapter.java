package com.alarees.tailoruserapp.more.language;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.alarees.tailoruserapp.R;

import java.util.ArrayList;

public class LanguageAdapter extends BaseAdapter {
    ArrayList<String> list;
    LayoutInflater inflater;

    LanguageAdapter(ArrayList<String> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.spinner_item_view, null);
       // TextView item = convertView.findViewById(R.id.itemname);
      //  item.setText(list.get(position));
        return convertView;

    }
}
