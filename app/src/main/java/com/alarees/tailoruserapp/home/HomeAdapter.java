package com.alarees.tailoruserapp.home;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.alarees.tailoruserapp.R;

import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    Context context;
    ArrayList<Fragment> fragmentArrayList;
    FragmentManager manager;
    ArrayList<String> nameoffragment;
    public HomeAdapter(Context context, FragmentManager manager,ArrayList<Fragment> fragmentArrayList,ArrayList<String> nameoffragment) {
        this.context=context;
        this.fragmentArrayList=fragmentArrayList;
        this.manager=manager;
        this.nameoffragment=nameoffragment;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.home_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //String title= ((TitledFragment)fragmentArrayList.get(position)).getTitle();
        String title=nameoffragment.get(position);
        holder.textView.setText(title);
        holder.cont.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               loadFragment(fragmentArrayList.get(position));
           }
       });
    }

    @Override
    public int getItemCount() {
        return fragmentArrayList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
       TextView textView;
       LinearLayout cont;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.widget_name);
            cont=itemView.findViewById(R.id.container_lin);
        }
    }
    private void loadFragment(Fragment fragment) {
        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(R.id.nav_host_fragment, fragment);
        ft.addToBackStack("HomeFragment");
        ft.commit();
    }
}
