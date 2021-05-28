package com.alarees.tailoruserapp.unstitched;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import devlight.io.library.ntb.NavigationTabBar;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alarees.tailoruserapp.R;
import com.alarees.tailoruserapp.home.HomeFragment;
import com.alarees.tailoruserapp.unstitched.model.UnstitchedModel;
import com.alarees.tailoruserapp.TitledFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UnstitchedItemsFragment extends Fragment implements TitledFragment {
    UnstitichedAdapter adapter;
    @BindView(R.id.id_unstiched_list)
    RecyclerView recyclerView;
    ArrayList<UnstitchedModel> models;
    DatabaseReference mRef;
    FragmentManager manager;
    private Unbinder unbinder;
    UnstitchedModel model;
    String catogory = "";
    ImageView backpres;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_unstitched_items, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        models = new ArrayList<>();
        manager = getFragmentManager();
        unbinder = ButterKnife.bind(this, view);
        backpres=view.findViewById(R.id.back_unstitched);
        backpres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeFragment pf = new HomeFragment();
                FragmentTransaction ft = manager.beginTransaction();
                ft.replace(R.id.nav_host_fragment, pf);
                ft.disallowAddToBackStack();
                ft.commit();
            }
        });
        mRef = FirebaseDatabase.getInstance().getReference("AdminDatabase");
        mRef.child("UnstitchedItems").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    UnstitchedModel model = data.getValue(UnstitchedModel.class);
                    if (model.getCategory().equals("Summer")) {
                        models.add(model);
                    }
                }
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                adapter = new UnstitichedAdapter(manager, getContext(), models);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        final NavigationTabBar ntbSample = (NavigationTabBar) view.findViewById(R.id.s);
        final int bgColor = Color.parseColor("#FFFFFFFF");
        final ArrayList<NavigationTabBar.Model> modelss = new ArrayList<>();
        modelss.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_sun), bgColor
                ).build()
        );
        modelss.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_snowflake), bgColor
                ).build()
        );
        ntbSample.setModels(modelss);
        ntbSample.setModelIndex(0, true);
        ntbSample.setOnTabBarSelectedIndexListener(new NavigationTabBar.OnTabBarSelectedIndexListener() {
            @Override
            public void onStartTabSelected(NavigationTabBar.Model model, int index) {
                if (index == 1) {
                    models = new ArrayList<>();
                    mRef = FirebaseDatabase.getInstance().getReference("AdminDatabase");
                    mRef.child("UnstitchedItems").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot data : snapshot.getChildren()) {
                                UnstitchedModel model = data.getValue(UnstitchedModel.class);
                                if (model.getCategory().equals("Winter")) {
                                    models.add(model);
                                }
                            }
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            adapter = new UnstitichedAdapter(manager, getContext(), models);
                            recyclerView.setAdapter(adapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                } else if (index == 0) {
                    models = new ArrayList<>();
                    mRef = FirebaseDatabase.getInstance().getReference("AdminDatabase");
                    mRef.child("UnstitchedItems").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot data : snapshot.getChildren()) {
                                UnstitchedModel model = data.getValue(UnstitchedModel.class);
                                if (model.getCategory().equals("Summer")) {
                                    models.add(model);
                                }
                            }
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            adapter = new UnstitichedAdapter(manager, getContext(), models);
                            recyclerView.setAdapter(adapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });

                }
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
        return "Unstitched";
    }
}