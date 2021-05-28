package com.alarees.tailoruserapp.unstitched.selector;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alarees.tailoruserapp.R;
import com.alarees.tailoruserapp.cart.CartFragment;
import com.alarees.tailoruserapp.home.HomeAdapter;
import com.alarees.tailoruserapp.measurement.measurementlist.ChildModel;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

public class SelectorAdabter extends RecyclerView.Adapter<SelectorAdabter.ViewHolder> {
    ArrayList<ChildModel> models;
    Context context;
    FragmentManager manager;
    Dialog dialog;

    public SelectorAdabter(ArrayList<ChildModel> models, Context context, FragmentManager manager, Dialog dialog) {
        this.context = context;
        this.models = models;
        this.manager = manager;
        this.dialog = dialog;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.selector_view, parent, false);
        return new SelectorAdabter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.childname.setText(models.get(position).getName());
        holder.childname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadfragment(models.get(position).getNodekey(), models.get(position).getHeight());
            }
        });
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView childname;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            childname = itemView.findViewById(R.id.child_selector_text);
        }
    }

    public void loadfragment(String nodekey, String height) {
        FragmentTransaction ft = manager.beginTransaction();
        CartFragment cf = new CartFragment();
        Bundle bundle = new Bundle();
        bundle.putString("fromUnstitched", "un");
        bundle.putString("nodekey", nodekey);
        bundle.putString("height", height);
        cf.setArguments(bundle);
        ft.replace(R.id.nav_host_fragment, cf);
        ft.addToBackStack("CuffFragment");
        ft.commit();
        dialog.dismiss();
    }
}
