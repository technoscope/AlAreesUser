package com.alarees.tailoruserapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Locale;


public class LanguageFragment extends Fragment {
    Spinner spinner;
    ArrayAdapter<String> spinneradapter;
    ArrayList<String> list;
    SharedPreferences mypref;
    SharedPreferences.Editor shareeditor;
    Button btnconfirm;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_language, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mypref = getContext().getSharedPreferences("language", Context.MODE_PRIVATE);
        list = new ArrayList<>();
        if(mypref.getInt("en",0)==0&&mypref.getInt("ar",0)==0) {
            list = new ArrayList<>();
            list.add("English");
            list.add("Arabic");
        }else if(mypref.getInt("en",0)==1&&mypref.getInt("ar",0)==0){
            list = new ArrayList<>();
            list.add("English");
            list.add("Arabic");
            setApplicationLocale("en");
        }else if(mypref.getInt("ar",0)==1&&mypref.getInt("en",0)==0){
            list = new ArrayList<>();
            list.add("Arabic");
            list.add("English");
            setApplicationLocale("ar");

        }
        spinner = view.findViewById(R.id.languague_category);
        spinneradapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, list);
        spinner.setAdapter(spinneradapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getItemAtPosition(i).toString().equals("English")) {
                    shareeditor=mypref.edit();
                    shareeditor.putInt("en", 1);
                    shareeditor.putInt("ar", 0);
                    shareeditor.apply();
                    setApplicationLocale("en");

                } else if(adapterView.getItemAtPosition(i).toString().equals("Arabic")) {
                    shareeditor=mypref.edit();
                    shareeditor.putInt("en", 0);
                    shareeditor.putInt("ar", 1);
                    shareeditor.apply();
                    setApplicationLocale("ar");
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                if (mypref.getInt("en", 0) == 1) {
                    setApplicationLocale("en");
                } else if (mypref.getInt("ar", 0) == 1) {
                    setApplicationLocale("ar");
                }
            }
        });
        btnconfirm=view.findViewById(R.id.btnconfirm);
        btnconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), getResources().getString(R.string.langaugechangesucess), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void setApplicationLocale(String locale) {
        Resources resources = getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(new Locale(locale.toLowerCase()));
        } else {
            config.locale = new Locale(locale.toLowerCase());
        }
        resources.updateConfiguration(config, dm);
    }

}