package com.alarees.tailoruserapp.unstitched.placket;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alarees.tailoruserapp.R;
import com.bumptech.glide.Glide;
import com.alarees.tailoruserapp.unstitched.model.TypeModel;

import java.util.ArrayList;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

class PlacketAdapter extends BaseAdapter {
    Context context;
    PlacketViewModel livedata;
    ArrayList<TypeModel> models;
    LayoutInflater inflter;
    public PlacketAdapter(Context context, ArrayList<TypeModel> models) {
        this.context=context;
        this.models=models;
        inflter = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.type_view, null); // inflate the layout

        TextView itemname = view.findViewById(R.id.type_name);
        itemname.setText(models.get(i).getTypeName());
        ImageView imageView=view.findViewById(R.id.type_img);
        Glide.with(context)
                .load(Uri.parse(models.get(i).getImgBaseUrl()))
                .into(imageView);
        imageView.getLayoutParams().width = 40;
        CardView viewclick=view.findViewById(R.id.id_type_view);
        viewclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                livedata = ViewModelProviders.of((FragmentActivity) context).get(PlacketViewModel.class);
                livedata.setLivemodel(models.get(i));
            }
        });
        return view;
    }
}
