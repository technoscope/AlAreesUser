package com.alarees.tailoruserapp.home;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alarees.tailoruserapp.R;
import com.alarees.tailoruserapp.contact.ContactFragment;
import com.alarees.tailoruserapp.deals.DealsFragment;
import com.alarees.tailoruserapp.measurement.MyDetialFragment;
import com.alarees.tailoruserapp.measurement.measurementlist.MeasurementListFragment;
import com.alarees.tailoruserapp.readymade.ReadyMadeItemsFragment;
import com.alarees.tailoruserapp.unstitched.UnstitchedItemsFragment;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.home_logo)
    ImageView logoimage;
    @BindView(R.id.home_container)
    LinearLayout container;
    @BindView(R.id.layout_deals)
    LinearLayout deals_lyout;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.layout_measurement)
    LinearLayout measurement_lyout;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.layout_readymade)
    LinearLayout readymade_lyout;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.layout_unstitched)
    LinearLayout unstitched_lyout;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.layout_aboutus)
    LinearLayout aboutus_lyout;

    Unbinder unbinder;
    //  @BindView(R.id.recycle_home)
    RecyclerView recyclerView;
    FragmentManager manager;
    ArrayList<Fragment> fragmentArrayList;
    ArrayList<String> nameoffragments;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        manager = getFragmentManager();
        int nightModeFlags =
                this.getResources().getConfiguration().uiMode &
                        Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                container.setBackgroundResource(R.drawable.background);
                logoimage.setBackgroundResource(R.drawable.ic_logo1);
                break;
            case Configuration.UI_MODE_NIGHT_NO:
                container.setBackgroundResource(R.drawable.background_white);
                logoimage.setBackgroundResource(R.drawable.logonew);
                break;
            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                container.setBackgroundResource(R.drawable.background);
                logoimage.setBackgroundResource(R.drawable.ic_logo1);
                break;
        }


        deals_lyout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.nav_host_fragment, new DealsFragment());
                ft.addToBackStack("HomeFragment");
                ft.commit();
            }
        });
        measurement_lyout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.nav_host_fragment, new MeasurementListFragment());
                ft.addToBackStack("HomeFragment");
                ft.commit();
            }
        });
        unstitched_lyout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.nav_host_fragment, new UnstitchedItemsFragment());
                ft.addToBackStack("HomeFragment");
                ft.commit();
            }
        });
        readymade_lyout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.nav_host_fragment, new ReadyMadeItemsFragment());
                ft.addToBackStack("HomeFragment");
                ft.commit();
            }
        });

        aboutus_lyout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.nav_host_fragment, new ContactFragment());
                ft.addToBackStack("HomeFragment");
                ft.commit();
            }
        });
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
        showDialog();
    }

    private void showDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setMessage("Do you want to close Al Arees").setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getActivity().finish();
            }
        });
        dialog.show();
    }

}