package com.alarees.tailoruserapp.more;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.alarees.tailoruserapp.R;
import com.alarees.tailoruserapp.account.SigninFragment;
import com.alarees.tailoruserapp.home.HomeFragment;
import com.alarees.tailoruserapp.measurement.MyDetialFragment;
import com.alarees.tailoruserapp.measurement.measurementlist.MeasurementListFragment;
import com.alarees.tailoruserapp.more.language.LanguageFragment;
import com.alarees.tailoruserapp.more.orders.MyOrders;
import com.alarees.tailoruserapp.TitledFragment;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 */
public class MoreItemFragment extends Fragment implements TitledFragment {
    private int mColumnCount = 1;
    MyItemRecyclerViewAdapter adapter;
    ArrayList<Integer> moduleicons;
    FragmentManager manager;
    RecyclerView recyclerView;
    LinearLayout container;
    Switch mode;
    SharedPreferences modepref;
    SharedPreferences.Editor prefeditor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("CommitPrefEdits")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more_item_list, container, false);
        modepref=getActivity().getSharedPreferences("Mode", Context.MODE_PRIVATE);
        prefeditor=modepref.edit();
        moduleicons = new ArrayList<>();
        moduleicons.add(R.mipmap.myaccount);
        moduleicons.add(R.mipmap.measurement2);
        moduleicons.add(R.mipmap.myorders);
        moduleicons.add(R.drawable.ic_baseline_language_24);
//        moduleicons.add(R.drawable.ic_baseline_dark_mode_24);
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new SigninFragment());
        fragments.add(new MeasurementListFragment());
        fragments.add(new MyOrders());
        fragments.add(new LanguageFragment());
//        fragments.add(new Darkmode_fragment());
        ArrayList<String> fragmentname = new ArrayList<>();
        fragmentname.add(getResources().getString(R.string.account));
        fragmentname.add(getResources().getString(R.string.mymeasurement));
        fragmentname.add(getResources().getString(R.string.my_orders));
        fragmentname.add(getResources().getString(R.string.language));
//        fragmentname.add(getResources().getString(R.string.Darkmode));
        recyclerView = view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // Set the adapter
        manager = getFragmentManager();
        recyclerView.setAdapter(new MyItemRecyclerViewAdapter(getContext(), fragments, moduleicons, manager, fragmentname));
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView logo = view.findViewById(R.id.logomore);
        container = view.findViewById(R.id.more_container);
        mode=view.findViewById(R.id.swith_mode);
        if (modepref.getInt("darkmode", 0) == 0) {

            mode.setChecked(false);
        } else if (modepref.getInt("darkmode", 1) == 1) {
            mode.setChecked(true);

        }
        mode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    prefeditor.putInt("darkmode",1);
                    prefeditor.commit();
                    prefeditor.apply();
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }else{
                    prefeditor.putInt("darkmode",0);
                    prefeditor.commit();
                    prefeditor.apply();
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
            }
        });
        int nightModeFlags =
                this.getResources().getConfiguration().uiMode &
                        Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                container.setBackgroundResource(R.drawable.background);
                logo.setBackgroundResource(R.drawable.ic_logo1);
                break;
            case Configuration.UI_MODE_NIGHT_NO:
                container.setBackgroundResource(R.drawable.background_white);
                logo.setBackgroundResource(R.drawable.logonew);
                break;
            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                logo.setBackgroundResource(R.drawable.ic_logo1);
                break;
        }
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    backtoparent();
                    return true;
                }
                return false;
            }
        });
    }

    private void backtoparent() {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(R.id.nav_host_fragment, new HomeFragment());
        ft.addToBackStack("HomeFragment");
        ft.commit();
    }

    @Override
    public String getTitle() {
        return "More";
    }
}