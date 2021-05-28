package com.alarees.tailoruserapp.measurement.measurementlist;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alarees.tailoruserapp.R;
import com.alarees.tailoruserapp.more.MyItemRecyclerViewAdapter;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
public class MAdabter extends RecyclerView.Adapter<MAdabter.ViewHolder> {
    ArrayList<ChildModel> childModels;
    Context context;
    FragmentManager manager;

    public MAdabter(Context context,FragmentManager manager,ArrayList<ChildModel> childmodels) {
        this.childModels=childmodels;
        this.manager=manager;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mlistview, parent, false);
        return new MAdabter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.nameofchild.setText(childModels.get(position).getName());
        holder.childcontainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChildMeasurementDetail fragment=new ChildMeasurementDetail();
                Bundle bundle=new Bundle();
                bundle.putInt("userget",1);
                fragment.setArguments(bundle);
                loadFragment(fragment);
            }
        });
    }

    @Override
    public int getItemCount() {
        return childModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameofchild;
        LinearLayout childcontainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameofchild = itemView.findViewById(R.id.id_child_measurement);
            childcontainer = itemView.findViewById(R.id.id_child_container);
        }
    }
    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.nav_host_fragment, fragment);
        transaction.addToBackStack("MeasurementListFragment");
        transaction.commit();
    }

}
