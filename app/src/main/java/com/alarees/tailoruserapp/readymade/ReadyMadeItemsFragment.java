package com.alarees.tailoruserapp.readymade;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import devlight.io.library.ntb.NavigationTabBar;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alarees.tailoruserapp.R;
import com.alarees.tailoruserapp.TitledFragment;
import com.alarees.tailoruserapp.home.HomeFragment;
import com.alarees.tailoruserapp.readymade.model.ReadymadeModel;
import com.alarees.tailoruserapp.unstitched.model.UnstitchedModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ReadyMadeItemsFragment extends Fragment implements TitledFragment {
    RecyclerView recyclerView;
    ArrayList<ReadymadeModel> models;
    DatabaseReference mRef;
    FragmentManager manager;
    ReadyMadeAdapter adapter;
    ImageView readymade;
    LinearLayout container;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ready_made_items, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        manager = getFragmentManager();
        models = new ArrayList<>();
        readymade=view.findViewById(R.id.back_readylist);
        readymade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeFragment pf = new HomeFragment();
                FragmentTransaction ft = manager.beginTransaction();
                ft.replace(R.id.nav_host_fragment, pf);
                ft.disallowAddToBackStack();
                ft.commit();
            }
        });
        container=view.findViewById(R.id.container_readymade);
        int nightModeFlags =
                this.getResources().getConfiguration().uiMode &
                        Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                container.setBackgroundResource(R.drawable.background);
                break;
            case Configuration.UI_MODE_NIGHT_NO:
                container.setBackgroundResource(R.drawable.background_white);
                break;
            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                container.setBackgroundResource(R.drawable.background_white);
                break;
        }
        mRef = FirebaseDatabase.getInstance().getReference("AdminDatabase");
        mRef.child("ReadymadeItems").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    ReadymadeModel readymadeModel = data.getValue(ReadymadeModel.class);
                    models.add(readymadeModel);
                }
                recyclerView = view.findViewById(R.id.id_readymade_list);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                adapter = new ReadyMadeAdapter(manager,getContext(), models);
                recyclerView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        final NavigationTabBar ntbSample = (NavigationTabBar) view.findViewById(R.id.s);
        final int bgColor = Color.parseColor("#FFFFFFFF");
        final ArrayList<NavigationTabBar.Model> models4 = new ArrayList<>();
        models4.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_sun), bgColor
                ).build()
        );
        models4.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_snowflake), bgColor
                ).build()
        );
        ntbSample.setModels(models4);
        ntbSample.setModelIndex(1, true);
        ntbSample.setOnTabBarSelectedIndexListener(new NavigationTabBar.OnTabBarSelectedIndexListener() {
            @Override
            public void onStartTabSelected(NavigationTabBar.Model model, int index) {
            }
            @Override
            public void onEndTabSelected(NavigationTabBar.Model model, int index) {

            }
        });
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    backtoparent();
                    return true;
                }
                return false;
            }
        });
    }

    private void backtoparent() {
        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(R.id.nav_host_fragment, new HomeFragment());
        ft.addToBackStack("HomeFragment");
        ft.commit();
    }
    @Override
    public String getTitle() {
        return "Readymade";
    }
}